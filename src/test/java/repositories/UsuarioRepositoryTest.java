package repositories;

import entities.Usuario;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@SpringBootTest
@ExtendWith(SpringExtension.class)
public class UsuarioRepositoryTest {

    @Autowired
    UsuarioRepository repository;

    @Test
    public void verificaExistenciaEmailTest(){
        Usuario usuario = Usuario.builder().nome("Lucas").email("lucas.iankoski@outlook.com").build();
        repository.save(usuario);

        boolean resultado = repository.existsByEmail("lucas.iankoski@outlook.com");

        Assertions.assertThat(resultado).isTrue();
    }
}
