package com.conectaong.conectaONG.service;

import java.nio.charset.Charset;
import java.util.Optional;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.conectaong.conectaONG.model.UserLogin;
import com.conectaong.conectaONG.model.Usuario;
import com.conectaong.conectaONG.repository.UsuarioRepository;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository repository;
	
	public Optional<Usuario> cadastrarUsuario(Usuario usuario) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		if(repository.findByUsuario(usuario.getUsuario()).isPresent()) {
			return Optional.empty();
		}
		
		String senhaEncoder = encoder.encode(usuario.getSenha());
		usuario.setSenha(senhaEncoder);
		
		return Optional.of(repository.save(usuario));
		//return repository.save(usuario);
	}
	
	public Optional<UserLogin> autenticarUsuario(Optional<UserLogin> user){
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		Optional<Usuario> usuario = repository.findByUsuario(user.get().getUsuario());
		
		if(usuario.isPresent()) {
			if(encoder.matches(user.get().getSenha(),usuario.get().getSenha())) {
				String auth = user.get().getUsuario() + ":" + user.get().getSenha();
				byte[] encodedAuth  = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
				String authHeader = "Basic " + new String(encodedAuth);
				
				user.get().setToken(authHeader);
				user.get().setNome(usuario.get().getNome());
				
				return user;
			}
		}
		
		return Optional.empty();
		//return null;
	}
	
	public Optional<Usuario> atualizarUsuario(Usuario usuario){
		if(repository.findById(usuario.getId()).isPresent()) {
			
			Optional<Usuario> buscaUsuario = repository.findByUsuario(usuario.getUsuario());
			
			if(buscaUsuario.isPresent()) {
				if(buscaUsuario.get().getId()!= usuario.getId()) {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O Usuário já existe", null);
				}
			}
			
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			
			String senhaEncoder = encoder.encode(usuario.getSenha());
			usuario.setSenha(senhaEncoder);
			
			return Optional.of(repository.save(usuario));
		}
	
		return Optional.empty();
	}
}
