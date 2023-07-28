package services;

import entities.Usuario;
import exceptions.AutenticacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.UsuarioRepository;
import exceptions.RegrasException;

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
        if(!usuario.isPresent()){
            throw new AutenticacaoException("E-mail inválido");
        }

        if(!usuario.get().getSenha().equals(senha)){
            throw new AutenticacaoException("Senha inválida");
        }

        return usuario.get();
    }

    public Usuario salvar(Usuario usuario){
        validarEmail(usuario.getEmail());
        return repository.save(usuario);
    }

    public void validarEmail(String email){
        boolean emailJaExistente = repository.existsByEmail(email);
        if(emailJaExistente){
            throw new RegrasException("Esse e-mail já está sendo utilizado.");
        }

    }

}
