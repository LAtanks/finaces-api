package br.com.latanks.finances.services;

import br.com.latanks.finances.exception.impl.CategoryNotFoundException;
import br.com.latanks.finances.exception.impl.InvalidCredentialsException;
import br.com.latanks.finances.models.Category;
import br.com.latanks.finances.models.User;
import br.com.latanks.finances.repositories.ICategoryRepository;
import br.com.latanks.finances.utils.AuthUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "CATEGORY_SERVICE")
public class CategoryService {

    private final ICategoryRepository categoryRepository;

    @Transactional
    public Category create(Category category) {
        categoryRepository.findByDescription(category.getDescription()).ifPresent(
                c -> {
                    throw new IllegalArgumentException("Category already exists!");
                }
        );

        for(Character caracter : category.getDescription().toCharArray()){
            if(Character.isDigit(caracter)){
                throw new InvalidCredentialsException("Descrição não pode conter numero");
            }
        }

        Category newCategory = categoryRepository.save(category);


        log.debug("Created category {} By {}", newCategory, AuthUtil.getAuthenticatedEmail());

        return newCategory;
    }

    public Category getByDescription(String description) {
        return categoryRepository.findByDescription(description).orElseThrow(
                () -> new CategoryNotFoundException("Categoria não encontrado.")
        );
    }

    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    public Category getById(Integer id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new CategoryNotFoundException("Categoria não encontrado.")
        );
    }

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Transactional
    public void  deleteById(Integer id) {
        this.categoryRepository.findById(Math.toIntExact(id)).orElseThrow(
                () -> new InvalidCredentialsException("User with id " + id + " not found")
        );

        log.debug("Deleted category {} By {}", getById(id).getDescription(), AuthUtil.getAuthenticatedEmail());
        categoryRepository.deleteById(Math.toIntExact(id));
    }

    @Transactional
    public Category update(Integer id, Category category) {
        Category updatedCategory = categoryRepository.findById(Math.toIntExact(id)).orElseThrow(
                () -> new InvalidCredentialsException("User with id " + id + " not found")
        );

        updatedCategory.setId(category.getId());
        updatedCategory.setDescription(category.getDescription());

        log.debug("Updated category {} to {} By {}",
                getById(id).getDescription(),
                updatedCategory.getDescription(),
                AuthUtil.getAuthenticatedEmail()
        );
        return categoryRepository.save(updatedCategory);
    }
}
