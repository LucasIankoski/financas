package repositories;

import entities.Usuario;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
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
