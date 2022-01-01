package com.devsuperior.dscatalog.controllers.exceptions;

import java.io.Serializable;
import java.time.Instant;

public class StandardError implements Serializable {
	private static final long serialVersionUID = 1L;

	private Instant dataAtual;
	private Integer status;
	private String erro;
	private String mensagem;
	private String caminho;

	public StandardError() {
	}

	public Instant getDataAtual() {
		return dataAtual;
	}

	public void setDataAtual(Instant dataAtual) {
		this.dataAtual = dataAtual;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getErro() {
		return erro;
	}

	public void setErro(String erro) {
		this.erro = erro;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getCaminho() {
		return caminho;
	}

	public void setCaminho(String caminho) {
		this.caminho = caminho;
	}

}
