package com.iankoski.financas.services;

import com.iankoski.financas.entities.Usuario;
import com.iankoski.financas.exceptions.AutenticacaoException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.iankoski.financas.repositories.UsuarioRepository;
import com.iankoski.financas.exceptions.RegrasException;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public Usuario autenticar(String email, String senha){
        Optional<Usuario> usuario = repository.findByEmail(email);
        if(!usuario.isPresent() || !usuario.get().getSenha().equals(senha)){
            throw new AutenticacaoException("E-mail ou senha inválida");
        }
        return usuario.get();
    }

    @Transactional
    public Usuario salvarUsuario(Usuario usuario){
        validarEmail(usuario.getEmail());
        return repository.save(usuario);
    }

    public void validarEmail(String email){
        boolean emailJaExistente = repository.existsByEmail(email);
        if(emailJaExistente){
            throw new RegrasException("Esse e-mail já está sendo utilizado.");
        }

    }

    public Optional<Usuario> encontrarPorId(Long id){
        return repository.findById(id);
    }
}
