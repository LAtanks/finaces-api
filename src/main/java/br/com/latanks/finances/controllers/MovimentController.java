package br.com.latanks.finances.controllers;


import br.com.latanks.finances.models.dto.request.CreateMovimentDTO;
import br.com.latanks.finances.models.dto.response.CreatedMovimentDTO;
import br.com.latanks.finances.models.dto.response.TotalBalanceFromUserDTO;
import br.com.latanks.finances.models.dto.request.UpdateMovimentDTO;
import br.com.latanks.finances.services.MovimentService;
import br.com.latanks.finances.utils.csv.Banks;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/moviment")
@RequiredArgsConstructor
public class MovimentController {

    private final MovimentService service;

    @PostMapping
    public ResponseEntity<CreatedMovimentDTO> create(@RequestBody CreateMovimentDTO moviment){
        return ResponseEntity.ok(service.create(moviment));
    }

    @PostMapping(value = "/loadcsvfile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CreatedMovimentDTO>> update(@RequestPart("csvFile") MultipartFile csvFile, @RequestPart("bank")String bank) throws IOException {
        System.out.println(csvFile.getOriginalFilename());


        return ResponseEntity.ok(service.createMovimentsFromCSVFile(csvFile, Banks.fromText(bank)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreatedMovimentDTO> getById(@PathVariable Long id){
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/my")
    public ResponseEntity<List<CreatedMovimentDTO>> getMovimentsListFromUserId(){
        return ResponseEntity.ok(service.getMovimentsListFromUserId());
    }

    @GetMapping("/my/monthly")
    public ResponseEntity<TotalBalanceFromUserDTO> getMonthlyBalanceFromUser(
            @RequestParam("year")  Integer year,
            @RequestParam("month") Integer month){
        return ResponseEntity.ok(service.getMonthlyBalanceFromUser(year, month));
    }

    @GetMapping("/my/category")
    public ResponseEntity<Map<String, String>> getBalanceByCategoryFromUser(
            @RequestParam("categoryId")  Integer categoryId){
        return ResponseEntity.ok(service.getBalanceByCategoryFromUser(categoryId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CreatedMovimentDTO> update(@PathVariable Long id, @RequestBody UpdateMovimentDTO moviment){
        return ResponseEntity.ok(service.update(moviment, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }


}
