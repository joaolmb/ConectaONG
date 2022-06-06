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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.conectaong.conectaONG.model.Tema;
import com.conectaong.conectaONG.repository.TemaRepository;

@RestController
@CrossOrigin("*") //precisa colocar o "allowedHeaders"?
@RequestMapping("/tema")
public class TemaController {
	
	@Autowired
	private TemaRepository repository;
	
	@GetMapping
	public ResponseEntity<List<Tema>> getAll(){
		return ResponseEntity.ok(repository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Tema> getById(@PathVariable Long id){
		return repository.findById(id).map(resposta->ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.notFound().build());
		//Build? Map?
	}
	
		
	@GetMapping("/tema={keyword}")
	public ResponseEntity<List<Tema>> getByTema(@PathVariable String keyword){
		return ResponseEntity.ok(repository.findAllByTemaContainingIgnoreCase(keyword));
	}
	
	@PostMapping
	public ResponseEntity<Tema> post(@Valid @RequestBody Tema tema){
		return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(tema));
	}
	
	@PutMapping
	public ResponseEntity<Tema> put(@Valid @RequestBody Tema tema){
		return ResponseEntity.status(HttpStatus.OK).body(repository.save(tema)); 
	}
	
	@DeleteMapping("/{id}")
	void delete(@PathVariable Long id){
		repository.deleteById(id);
	}
	//
}
