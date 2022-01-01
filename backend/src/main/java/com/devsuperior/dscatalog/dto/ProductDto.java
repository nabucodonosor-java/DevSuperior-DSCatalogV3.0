package com.devsuperior.dscatalog.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

public class ProductDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	private String description;
	private BigDecimal price;
	private String imgUrl;
	private Instant date;
	private List<CategoryDto> categories = new ArrayList<>();
	
	public ProductDto() {}

	public ProductDto(Long id, String name, String description, BigDecimal price, String imgUrl, Instant date) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.imgUrl = imgUrl;
		this.date = date;
	}

	public ProductDto(Product entity) {
		id = entity.getId();
		name = entity.getName();
		description = entity.getDescription();
		price = entity.getPrice();
		imgUrl = entity.getImgUrl();
		date = entity.getDate();
	}
	
	// Este construtor tem o objetivo de adicionar ao produto uma lista de categorias
	// This constructor aims to add a list of categories to the product
	public ProductDto(Product entity, Set<Category> categories) {
		this(entity);
		// adicionando no Set da entidade as categorias que estão no DTO 
		// função de alta ordem - expressões lambdas
		categories.forEach(cat -> this.categories.add(new CategoryDto(cat)));
	}
	
	public Instant getDate() {
		return date;
	}

	public void setDate(Instant date) {
		this.date = date;
	}

	public List<CategoryDto> getCategories() {
		return categories;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public static Page<ProductDto> converter(Page<Product> page) {
		return page.map(ProductDto::new);
	}
	
}
