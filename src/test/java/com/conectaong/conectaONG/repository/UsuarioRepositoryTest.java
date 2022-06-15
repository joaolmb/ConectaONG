package com.conectaong.conectaONG.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.conectaong.conectaONG.model.Usuario;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioRepositoryTest {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@BeforeAll
	void start() {
		usuarioRepository.deleteAll();
		
		usuarioRepository.save(new Usuario(0L, "João Lucas", "joao@lucas", "fotoAqui", "123456789"));
		usuarioRepository.save(new Usuario(0L, "João Vitor", "joao@vitor", "fotoAqui", "123456789"));
		usuarioRepository.save(new Usuario(0L, "João Gabriel", "joao@gabriel", "fotoAqui", "123456789"));
		usuarioRepository.save(new Usuario(0L, "David Lucas", "david@lucas", "fotoAqui", "123456789"));
	}
	
	@Test
	@DisplayName("Retorna um usuário")
	public void deveRetornarUmUsuario() {
		Optional<Usuario> usuario = usuarioRepository.findByUsuario("joao@lucas");
		assertTrue(usuario.get().getUsuario().equals("joao@lucas"));
	}
	
	@Test
	@DisplayName("Retorna 3 usuários")
	public void deveretornarTresUsuarios() {
		List<Usuario> listaDeUsuarios = usuarioRepository.findAllByNomeContainingIgnoreCase("João");
		
		assertEquals(3, listaDeUsuarios.size());
		
		assertTrue(listaDeUsuarios.get(0).getNome().equals("João Lucas"));
		assertTrue(listaDeUsuarios.get(1).getNome().equals("João Vitor"));
		assertTrue(listaDeUsuarios.get(2).getNome().equals("João Gabriel"));
	}
	
	@AfterAll
	public void end() {
		usuarioRepository.deleteAll();
	}

}
