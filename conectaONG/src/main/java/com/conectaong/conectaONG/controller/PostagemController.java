package com.conectaong.conectaONG.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.conectaong.conectaONG.model.Postagem;
import com.conectaong.conectaONG.repository.PostagemRepository;

@RestController
@CrossOrigin("*")
@RequestMapping("/postagem")
public class PostagemController {

	@Autowired
	private PostagemRepository repository;
	
	@GetMapping
	public ResponseEntity<List<Postagem>> getAll(){
		return ResponseEntity.ok(repository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Postagem> getById(@PathVariable Long id){
		return repository.findById(id).map(resposta->ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.notFound().build());
	}
	
	@GetMapping("/titulo={keyword}")
	public ResponseEntity<List<Postagem>> getByTitulo(@PathVariable String keyword){
		return ResponseEntity.ok(repository.findAllByTituloContainingIgnoreCase(keyword));
	}
	
	@GetMapping("/mensagem={keyword}")
	public ResponseEntity<List<Postagem>> getByMensagem(@PathVariable String keyword){
		return ResponseEntity.ok(repository.findAllByMensagemContainingIgnoreCase(keyword));
	}
	
	@PostMapping
	public ResponseEntity<Postagem> post(@Valid @PathVariable Postagem postagem) {
		return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(postagem));
	}
	
	@PutMapping
	public ResponseEntity<Postagem> put(@Valid @PathVariable Postagem postagem){
		return ResponseEntity.status(HttpStatus.OK).body(repository.save(postagem));
	}
	
	@DeleteMapping("/{id}")
	void delete(@PathVariable Long id) {
		repository.deleteById(id);
	}
}
