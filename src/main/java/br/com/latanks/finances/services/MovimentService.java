package br.com.latanks.finances.services;

import br.com.latanks.finances.exception.impl.*;
import br.com.latanks.finances.models.Category;
import br.com.latanks.finances.models.Moviment;
import br.com.latanks.finances.models.User;
import br.com.latanks.finances.models.dto.*;
import br.com.latanks.finances.models.dto.request.CreateMovimentDTO;
import br.com.latanks.finances.models.dto.request.UpdateMovimentDTO;
import br.com.latanks.finances.models.dto.response.CreatedMovimentDTO;
import br.com.latanks.finances.models.dto.response.TotalBalanceFromUserDTO;
import br.com.latanks.finances.repositories.ICategoryRepository;
import br.com.latanks.finances.repositories.IMovimentRepository;
import br.com.latanks.finances.repositories.IUserRepository;
import br.com.latanks.finances.utils.AuthUtil;
import br.com.latanks.finances.utils.PropertiesUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j(topic = "MOVIMENT_SERVICE")
public class MovimentService {

    private final IMovimentRepository repository;
    private final IUserRepository userRepository;
    private final ICategoryRepository  categoryRepository;
    private final CategoryService categoryService;
    private final MovimentMapper mapper;

    public CreatedMovimentDTO create(CreateMovimentDTO moviment) {
        if(moviment.date().isAfter(LocalDate.now()) || moviment.date().isBefore(LocalDate.now().plusMonths(-6))) {
            throw new InvalidMovimentDateException("A data não pode ser menos que 6 meses ou maior que a data atual");
        }

        User movimentCreatedBy = userRepository.findByEmail(AuthUtil.getAuthenticatedEmail()).orElseThrow(
                () -> new UserNotFoundException("Usuario não encontrado")
        );

        Moviment newMoviment = mapper.toEntity(moviment);

        categoryRepository.findByDescription(moviment.categoryDescription()).ifPresentOrElse(
                category -> {
                    newMoviment.setCategory(category);
                },
                () -> {
                    newMoviment.setCategory(categoryService.create(new Category(null, moviment.categoryDescription())));
                }
        );

        PropertiesUtil.copyProperties(moviment, newMoviment);
        newMoviment.setUser(movimentCreatedBy);

        log.debug("Created moviment {} By {}", newMoviment, AuthUtil.getAuthenticatedEmail());

        return mapper.toDTO(repository.save(newMoviment));
    }

    public Map<String, String> getBalanceByCategoryFromUser(Integer categoryId) {
        double totalIncome = 0D;
        double totalExpense = 0D;
        double totalBalance = 0D;

        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new CategoryNotFoundException("Categoria não encontrada")
        );

        User user = userRepository.findByEmail(AuthUtil.getAuthenticatedEmail()).orElseThrow(
                () -> new UserNotFoundException("Usuario não encontrado")
        );

        for (Moviment moviment : user.getMoviments()) {
            if(moviment.getDate() == null || moviment.getType() == null || moviment.getCategory() == null) continue;
            if(moviment.getCategory().equals(category)) {
                switch (moviment.getType()){
                    case IN -> totalIncome += moviment.getValue();
                    case OUT -> totalExpense += moviment.getValue();
                }
            }
        }

        totalBalance = totalIncome + totalExpense;

        Map<String, String> details = new HashMap<>();
        details.put("category", category.getDescription());
        details.put("income", String.valueOf(totalIncome));
        details.put("expense", String.valueOf(totalExpense));
        details.put("balance", String.valueOf(totalBalance));

        return details;

    }

    public TotalBalanceFromUserDTO getMonthlyBalanceFromUser(final Integer year, final Integer month){
        double totalIncome = 0D;
        double totalExpense = 0D;
        double totalBalance = 0D;

        if(Year.now().isAfter(Year.of(year)) || LocalDate.now().isAfter(LocalDate.now().plusMonths(month)) || LocalDate.of(year, month, 1).isBefore(LocalDate.now().plusMonths(-6))) {
            throw new InvalidMovimentDateException("A data não pode ser menos que 6 meses ou maior que a data atual");
        }

        User user = userRepository.findByEmail(AuthUtil.getAuthenticatedEmail()).orElseThrow(
                () -> new UserNotFoundException("Usuario não encontrado")
        );

        for (Moviment moviment : user.getMoviments()) {
            if(moviment.getDate() == null || moviment.getType() == null) continue;
            if(moviment.getDate().getMonth() == Month.of(month) && moviment.getDate().getYear() == year) {
                switch (moviment.getType()){
                    case IN -> totalIncome += moviment.getValue();
                    case OUT -> totalExpense += moviment.getValue();
                }
            }
        }

        totalBalance = totalIncome - totalExpense;

        var dto = new TotalBalanceFromUserDTO(Year.of(year).toString(), Month.of(month).name(), totalIncome, totalExpense, totalBalance);

        return dto;

    }

    public CreatedMovimentDTO update(UpdateMovimentDTO moviment, Long id) {
        Moviment updateMoviment = repository.findById(id).orElseThrow(
                () -> new MovimentNotFoundException("Moviment with id " + id + " not found")
        );

        log.debug("Updated moviment {} to {} By {}", updateMoviment, moviment, AuthUtil.getAuthenticatedEmail());

        PropertiesUtil.copyProperties(moviment, updateMoviment);


        return mapper.toDTO(repository.save(updateMoviment));
    }

    public void delete(Long id) {
        log.debug("Deleted moviment {} By {}", getById(id), AuthUtil.getAuthenticatedEmail());

        repository.deleteById(id);
    }

    public CreatedMovimentDTO getById(Long id){
        Moviment moviment = repository.findById(id).orElseThrow(
                () -> new MovimentNotFoundException("User with id " + id + " not found")
        );

        return mapper.toDTO(moviment);
    }

    public List<CreatedMovimentDTO> getMovimentsListFromUserId(){
        User user = userRepository.findByEmail(AuthUtil.getAuthenticatedEmail()).orElseThrow(
                () -> new UserNotFoundException("Usuario não encontrado")
        );

        List<CreatedMovimentDTO> fromUser = repository
                .findAll()
                .stream()
                .filter(e -> e.getUser().getId() == user.getId())
                .toList().stream().map(mapper::toDTO).toList();

        return fromUser;
    }
}
