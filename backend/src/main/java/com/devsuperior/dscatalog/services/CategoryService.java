package com.devsuperior.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDto;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ObjectNotFoundException;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository repository;
	
	@Transactional(readOnly = true)
	public Page<CategoryDto> findAllPaged(PageRequest page) {
		return CategoryDto.converter(repository.findAll(page));
	}
	
	@Transactional(readOnly = true)
	public CategoryDto findById(Long id) {
		Optional<Category> obj = repository.findById(id);
		return new CategoryDto(obj.orElseThrow(() -> new ObjectNotFoundException("Categoria não encontrada!")));
	}
	
	@Transactional
	public CategoryDto insert(CategoryDto dto) {
		Category entity = new Category();
		copyToEntity(dto, entity);
		entity = repository.save(entity);
		return new CategoryDto(entity);
	}
	
	@Transactional
	public CategoryDto update(Long id, CategoryDto dto) {
		try {
		Category entity = repository.getOne(id);
		copyToEntity(dto, entity);
		entity = repository.save(entity);
		return new CategoryDto(entity);
		} catch (EntityNotFoundException e) {
			throw new ObjectNotFoundException("Categoria não encontrada!");
		}
	}
	
	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ObjectNotFoundException("Categoria não encontrada!");
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Violação crítica no DB");
		}
		
	}

	private void copyToEntity(CategoryDto dto, Category entity) {
		
		entity.setName(dto.getName());
	}

}
