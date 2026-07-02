package com.bernardo.moneywise.service;

import com.bernardo.moneywise.dto.CategoryRequestDTO;
import com.bernardo.moneywise.dto.CategoryResponseDTO;
import com.bernardo.moneywise.exceptions.ResourceNotFoundException;
import com.bernardo.moneywise.model.Category;
import com.bernardo.moneywise.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryResponseDTO> findAll(){
        return categoryRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }
    public CategoryResponseDTO findById(Long id){
        Category category = getCategoryOrThrow(id);
        return toResponseDTO(category);
    }

    public CategoryResponseDTO create(CategoryRequestDTO categoryRequestDTO){
        Category category = new Category();
        category.setName(categoryRequestDTO.getCategoryName());
        return toResponseDTO(categoryRepository.save(category));
    }

    public CategoryResponseDTO update(Long id,CategoryRequestDTO categoryRequestDTO){
        Category category = getCategoryOrThrow(id);
        category.setName(categoryRequestDTO.getCategoryName());
        return toResponseDTO(categoryRepository.save(category));
    }

    public void delete(Long id){
        Category category = getCategoryOrThrow(id);
        categoryRepository.delete(category);
    }
    public Category getCategoryOrThrow(Long id){
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não foi Encontrada com o ID:" + id));
    }
    private CategoryResponseDTO toResponseDTO(Category category){
        return new CategoryResponseDTO(category.getId(), category.getName());
    }
}
