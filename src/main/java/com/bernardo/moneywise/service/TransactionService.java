package com.bernardo.moneywise.service;

import com.bernardo.moneywise.dto.TransactionRequestDTO;
import com.bernardo.moneywise.dto.TransactionResponseDTO;
import com.bernardo.moneywise.enums.TransactionType;
import com.bernardo.moneywise.exceptions.ResourceNotFoundException;
import com.bernardo.moneywise.model.Category;
import com.bernardo.moneywise.model.Transaction;
import com.bernardo.moneywise.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryService categoryService;

    public Page<TransactionResponseDTO> findAll(Pageable pageable){
        return transactionRepository.findAll(pageable)
                .map(this::toResponseDTO);
    }

    public TransactionResponseDTO findById(Long id){
        Transaction transaction = getTransactionOrThrow(id);
        return toResponseDTO(transaction);
    }

    public TransactionResponseDTO create(TransactionRequestDTO transactionRequestDTO){
        Transaction transaction = toEntity(transactionRequestDTO);
        return toResponseDTO(transactionRepository.save(transaction));
    }

    public TransactionResponseDTO update(Long id, TransactionRequestDTO transactionRequestDTO){
        Transaction transaction = getTransactionOrThrow(id);
        Category category = categoryService.getCategoryOrThrow(transactionRequestDTO.getCategoryId());

        transaction.setDescription(transactionRequestDTO.getDescription());
        transaction.setAmount(transactionRequestDTO.getAmount());
        transaction.setType(transactionRequestDTO.getTransactionType());
        transaction.setTransactionDate(transactionRequestDTO.getTransactionDate());
        transaction.setCategory(category);

        return toResponseDTO(transactionRepository.save(transaction));
    }

    public void delete(Long id){
        Transaction transaction = getTransactionOrThrow(id);
        transactionRepository.delete(transaction);
    }

    public Page<TransactionResponseDTO> searchByCategory(String categoryName, Pageable pageable) {
        return transactionRepository.findByCategory_NameContainingIgnoreCase(categoryName, pageable)
                .map(this::toResponseDTO);
    }

    public Page<TransactionResponseDTO> searchByType(TransactionType transactionType, Pageable pageable){
        return transactionRepository.findByType(transactionType, pageable)
                .map(this::toResponseDTO);
    }

    private Transaction toEntity(TransactionRequestDTO dto) {
        Transaction transaction = new Transaction();
        Category category = categoryService.getCategoryOrThrow(dto.getCategoryId());

        transaction.setDescription(dto.getDescription());
        transaction.setAmount(dto.getAmount());
        transaction.setType(dto.getTransactionType());
        transaction.setTransactionDate(dto.getTransactionDate());
        transaction.setCategory(category);
        return transaction;
    }

    private Transaction getTransactionOrThrow(Long id){
        return transactionRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Transação com o ID: " + id + " não encontrada."));
    }

    private TransactionResponseDTO toResponseDTO(Transaction transaction){
            return new TransactionResponseDTO(
                    transaction.getId(),
                    transaction.getDescription(),
                    transaction.getAmount(),
                    transaction.getType(),
                    transaction.getTransactionDate(),
                    transaction.getCategory().getName()
            );
    }
}
