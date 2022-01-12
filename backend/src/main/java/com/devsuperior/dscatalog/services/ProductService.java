package com.devsuperior.dscatalog.services;

import java.net.URL;
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
import org.springframework.web.multipart.MultipartFile;

import com.devsuperior.dscatalog.dto.CategoryDto;
import com.devsuperior.dscatalog.dto.ProductDto;
import com.devsuperior.dscatalog.dto.UriDto;
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
	
	@Autowired
	private S3Service s3Service;
	
	@Transactional(readOnly = true)
	public Page<ProductDto> findAllPaged(PageRequest pageRequest, Long categoryId, String name) {
		List<Category> categories = (categoryId == 0) ? null : Arrays.asList(categoryRepository.getOne(categoryId));
		Page<Product> page = repository.find(categories, name, pageRequest);
		repository.find(page.toList());
		return page.map(x -> new ProductDto(x, x.getCategories()));
	}
	
	@Transactional(readOnly = true)
	public ProductDto findById(Long id) {
		Optional<Product> optional = repository.findById(id);
		Product product = optional.orElseThrow(() -> new ObjectNotFoundException("Entity Not Found"));
		return new ProductDto(product, product.getCategories());
	}
	
	@Transactional
	public ProductDto insert(ProductDto dto) {
		Product product = new Product();
		copyDtoToEntity(dto, product);
		if (product.getCategories().size() == 0) {
			Category cat = categoryRepository.getOne(1L);
			product.getCategories().add(cat);
		}
		product = repository.save(product);
		return new ProductDto(product);
	
	}

	@Transactional
	public ProductDto update(Long id, ProductDto dto) {
		try {
		Product product = repository.getOne(id);
		copyDtoToEntity(dto, product);
		if (product.getCategories().size() == 0) {
			Category cat = categoryRepository.getOne(1L);
			product.getCategories().add(cat);
		}
		product = repository.save(product);
		return new ProductDto(product);
		} catch (EntityNotFoundException e) {
			throw new ObjectNotFoundException("Id Not Found " + id);
		}
	}
	
	public void delete(Long id) {
		try {
			
			repository.deleteById(id);
			
		} catch (EmptyResultDataAccessException e) {
			throw new ObjectNotFoundException("Id Not Found " + id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integraty Violation");
		}
	}
	
	private void copyDtoToEntity(ProductDto dto, Product product) {
		product.setName(dto.getName());
		product.setDescription(dto.getDescription());
		product.setDate(dto.getDate());
		product.setImgUrl(dto.getImgUrl());
		product.setPrice(dto.getPrice());
		
		product.getCategories().clear();
		
		for (CategoryDto catDto : dto.getCategories()) {
			Category category = categoryRepository.getOne(catDto.getId());
			product.getCategories().add(category);
		}
	}

	public UriDto uploadFile(MultipartFile file) {
		
		URL url = s3Service.uploadFile(file);
		
		return new UriDto(url.toString());
	}
}
