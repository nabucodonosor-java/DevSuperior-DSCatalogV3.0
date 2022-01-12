package com.devsuperior.dscatalog.dto;

import java.io.Serializable;

import org.springframework.data.domain.Page;

import com.devsuperior.dscatalog.entities.Category;

public class CategoryDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;

	public CategoryDto() {
	}

	public CategoryDto(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public CategoryDto(Category entity) {
		id = entity.getId();
		name = entity.getName();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static Page<CategoryDto> converter(Page<Category> list) {
		return list.map(CategoryDto::new);
	}

}