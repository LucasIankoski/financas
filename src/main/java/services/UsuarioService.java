package services;

import entities.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.UsuarioRepository;
import exceptions.RegrasException;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public Usuario autenticar(String email, String senha){

        return null;
    }

    public Usuario salvar(Usuario usuario){

        return null;
    }

    public void validarEmail(String email){
        boolean emailJaExistente = repository.existsByEmail(email);
        if(emailJaExistente){
            throw new RegrasException("Esse e-mail já está sendo utilizado.");
        }

    }

}
