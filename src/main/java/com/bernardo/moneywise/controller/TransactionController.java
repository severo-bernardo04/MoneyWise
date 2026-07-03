package com.bernardo.moneywise.controller;

import com.bernardo.moneywise.dto.TransactionRequestDTO;
import com.bernardo.moneywise.dto.TransactionResponseDTO;
import com.bernardo.moneywise.enums.TransactionType;
import com.bernardo.moneywise.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<Page<TransactionResponseDTO>> findAll(Pageable pageable) {
        return ResponseEntity.ok(transactionService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.findById(id));
    }

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> create(@Valid @RequestBody TransactionRequestDTO dto) {
        TransactionResponseDTO created = transactionService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> update(@PathVariable Long id, @Valid @RequestBody TransactionRequestDTO dto) {
        return ResponseEntity.ok(transactionService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        transactionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<TransactionResponseDTO>> search(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) TransactionType type,
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {

        if (category != null) {
            return ResponseEntity.ok(transactionService.searchByCategory(category, pageable));
        }
        if (type != null) {
            return ResponseEntity.ok(transactionService.searchByType(type, pageable));
        }
        return ResponseEntity.ok(transactionService.findAll(pageable));
    }

}
