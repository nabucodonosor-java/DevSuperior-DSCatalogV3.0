package com.devsuperior.dscatalog.dto;

import java.io.Serializable;

import org.springframework.data.domain.Page;

import com.devsuperior.dscatalog.entities.Category;

public class CategoryDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	
	public CategoryDto() {}
	
	public CategoryDto(Long id, String name) {
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
	
	public static Page<CategoryDto> converter(Page<Category> page) {
		// return page.map((cat) -> new CategoryDto(cat));
		return page.map(CategoryDto::new);
	}

	/*
	 * método findAll sem paginação
	public static List<CategoryDto> converter(List<Category> list) {
		return list.stream().map((cat) -> new CategoryDto(cat)).collect(Collectors.toList());
		return list.stream().map(CategoryDto::new).collect(Collectors.toList());
	}
	*/
}
