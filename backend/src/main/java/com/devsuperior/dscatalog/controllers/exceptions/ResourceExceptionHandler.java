package com.devsuperior.dscatalog.controllers.exceptions;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.devsuperior.dscatalog.services.exceptions.ObjectNotFoundException;

@ControllerAdvice // anotação para criar um "controlador de exceções"
public class ResourceExceptionHandler {
	
	// HttpServletRequest -> info das requisições
	@ExceptionHandler(ObjectNotFoundException.class) // sempre que ocorrer esse tipo de exceção esse método será invocado
	public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException e, HttpServletRequest req) {
		StandardError error = new StandardError();
		error.setDataAtual(Instant.now());
		error.setStatus(HttpStatus.NOT_FOUND.value());
		error.setErro("Recurso não encontrado!");
		error.setMensagem(e.getMessage());
		error.setCaminho(req.getRequestURI());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}

}
