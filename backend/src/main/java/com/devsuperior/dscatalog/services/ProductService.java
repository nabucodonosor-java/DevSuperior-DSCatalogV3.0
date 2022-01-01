package com.devsuperior.dscatalog.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.ProductDto;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ObjectNotFoundException;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository repository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Transactional(readOnly = true)
	public Page<ProductDto> findAll(PageRequest pageRequest, Long categoryId, String name) {
		List<Category> categories = (categoryId == 0) ? null : Arrays.asList(categoryRepository.getOne(categoryId));
		Page<Product> page = repository.find(categories, name, pageRequest);
		repository.find(page.toList());
		return page.map(x -> new ProductDto(x, x.getCategories()));
	}
	
	@Transactional(readOnly = true)
	public ProductDto findById(Long id) {
		Optional<Product> obj = repository.findById(id);
		return new ProductDto(obj.orElseThrow(() -> new ObjectNotFoundException("Produto não encontrado!")));
	}
	
	@Transactional
	public ProductDto insert(ProductDto dto) {
		Product entity = new Product();
		copyToEntity(entity, dto);
		entity = repository.save(entity);
		return new ProductDto(entity);
	}
	
	@Transactional
	public ProductDto update(Long id, ProductDto dto) {
		try {
		Product entity = repository.getOne(id);
		copyToEntity(entity, dto);
		entity = repository.save(entity);
		return new ProductDto(entity);
		} catch (EntityNotFoundException e) {
			throw new ObjectNotFoundException("Produto não encontrado!");
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

	private void copyToEntity(Product entity, ProductDto dto) {
		entity.setName(dto.getName());
		entity.setImgUrl(dto.getImgUrl());
		entity.setPrice(dto.getPrice());
		entity.setDescription(dto.getDescription());
		entity.setDate(dto.getDate());
		
		entity.getCategories().clear();
		
		/*
		 for (CategoryDto catDto : dto.getCategories()) {
			Category category = categoryRepository.getOne(catDto.getId());
			product.getCategories().add(category);
		}
		 */
		
		dto.getCategories().forEach(cat -> entity.getCategories().add(new Category(cat)));
	}

}
