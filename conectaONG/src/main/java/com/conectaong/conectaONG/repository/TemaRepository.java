package com.conectaong.conectaONG.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.conectaong.conectaONG.model.Tema;

@Repository
public interface TemaRepository extends JpaRepository<Tema, Long>{
	public Lista<Tema> findAllByTemaContainingIgnoreCase(String tema);
}
