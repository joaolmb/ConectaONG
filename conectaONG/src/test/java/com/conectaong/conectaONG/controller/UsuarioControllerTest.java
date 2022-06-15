package com.conectaong.conectaONG.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.conectaong.conectaONG.model.UserLogin;
import com.conectaong.conectaONG.model.Usuario;
import com.conectaong.conectaONG.repository.UsuarioRepository;
import com.conectaong.conectaONG.service.UsuarioService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioControllerTest {
	
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@BeforeAll
	void start() {
		usuarioRepository.deleteAll();
	}
	
	@Test
	@Order(1)
	@DisplayName("Cadastrar um Usuário")
	public void deveCriarUmUsuario() {
		
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(new Usuario(0L,
				"Silvio Mauricio","silvio@email.com","silvio123","https://i.imgur.com/JR7kUFU.jpg"));
		
		ResponseEntity<Usuario> resposta = testRestTemplate.exchange(
				"/usuarios/cadastrar", HttpMethod.POST,requisicao,Usuario.class);
		
		assertEquals(HttpStatus.CREATED,resposta.getStatusCode());
		assertEquals(requisicao.getBody().getNome(),resposta.getBody().getNome());
		assertEquals(requisicao.getBody().getUsuario(),resposta.getBody().getUsuario());
		
	}
	
	@Test
	@Order(2)
	@DisplayName("Não deve permitir duplicação do Usuário")
	public void naoDeveDuplicarUsuario() {
		
		usuarioService.cadastrarUsuario(new Usuario(0L,
				"David Lucas","david_lucas@email.com","david123","https://i.imgur.com/Sk5SjWE.jpg"));
		
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(new Usuario(0L,
				"David Lucas","david_lucas@email.com","david123","https://i.imgur.com/Sk5SjWE.jpg"));
		
		ResponseEntity<Usuario> resposta = testRestTemplate.exchange(
				"/usuarios/cadastrar", HttpMethod.POST,requisicao,Usuario.class);
		
		assertEquals(HttpStatus.BAD_REQUEST,resposta.getStatusCode());
	}
	
	@Test
	@Order(3)
	@DisplayName("Alterar um usuario")
	public void deveAtualizarUmUsuario() {
		
		Optional<Usuario> usuarioCreate = usuarioService.cadastrarUsuario(new Usuario(0L,
				"João Vitor","joao_vitor@email.com","joaovitor123","https://i.imgur.com/JR7kUFU.jpg"));
		
		Usuario usuarioUpdate = new Usuario(usuarioCreate.get().getId(),
				"João Vitor Lima","joao_lima@email.com","joaovitor123","https://i.imgur.com/JR7kUFU.jpg");
				
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(usuarioUpdate);
		ResponseEntity<Usuario> resposta = testRestTemplate
				.withBasicAuth("root", "root")
				.exchange("/usuarios/atualizar", HttpMethod.PUT,requisicao,Usuario.class);
		
		assertEquals(HttpStatus.OK, resposta.getStatusCodeValue());
		assertEquals(requisicao.getBody().getNome(),resposta.getBody().getNome());
		assertEquals(requisicao.getBody().getUsuario(),resposta.getBody().getUsuario());
	}
	
	@Test
	@Order(4)
	@DisplayName("Listar todas Usuários")
	public void deveMostrarTodosUsuarios() {
		
		usuarioService.cadastrarUsuario(new Usuario(0L,
				"João Marques","joao_marques@email.com","joaomarques123","https://i.imgur.com/JR7kUFU.jpg"));
		usuarioService.cadastrarUsuario(new Usuario(0L,
				"Gabriel Mendes","gabriel_mendes@email.com","gabriel123","https://i.imgur.com/Sk5SjWE.jpg"));
		
		ResponseEntity<String> resposta = testRestTemplate
				.withBasicAuth("root", "root")
				.exchange("/usuarios/all",HttpMethod.GET,null,String.class);
		
		assertEquals(HttpStatus.OK,resposta.getStatusCode());
	}
	
	@Test
	@Order(5)
	@DisplayName("Listar um Usuário Específico")
	public void deveListarApenasUmUsuario() {
		
		Optional<Usuario> usuarioProcura = usuarioService.cadastrarUsuario(new Usuario(0L,
				"Maria Joaquina", "maria_joaquina@email.com","maria12345","https://i.imgur.com/EcJG8kB.jpg"));
		
		ResponseEntity<String> resposta = testRestTemplate
				.withBasicAuth("root", "root")
				.exchange("/usuarios" + usuarioProcura.get().getId(), HttpMethod.GET, null, String.class);
		
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
	}
	
	@Test
	@Order(6)
	@DisplayName("Login do Usuário")
	public void deveAutenticarUsuario() {
		
		usuarioService.cadastrarUsuario(new Usuario(0L,
				"Matheus Chaves", "matheus_chaves@email.com","creatina","https://i.imgur.com/Sk5SjWE.jpg"));
		
		HttpEntity<UserLogin> requisicao = new HttpEntity<UserLogin>(new UserLogin(0L,
				"","matheus_chaves@email.com","creatina","",""));
	
		ResponseEntity<UserLogin> resposta = testRestTemplate
				.exchange("/usuarios/logar", HttpMethod.POST, requisicao, UserLogin.class);
		
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
	}
}
