package com.conectaong.conectaONG.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tb_temas")
public class Tema {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	private String tema;
	
	private Long quant_postagens;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTema() {
		return tema;
	}
	public void setTema(String tema) {
		this.tema = tema;
	}
	public Long getQuant_postagens() {
		return quant_postagens;
	}
	public void setQuant_postagens(Long quant_postagens) {
		this.quant_postagens = quant_postagens;
	}
	
}
