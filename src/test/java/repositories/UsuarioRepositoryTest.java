package repositories;

import com.iankoski.financas.entities.Usuario;
import com.iankoski.financas.repositories.UsuarioRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;



@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
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

    @Test
    public void deveRetornarFalsoSeEmailNaoExisteNaBase(){
        repository.deleteAll();

        boolean resultado = repository.existsByEmail("lucas.iankoski@outlook.com");

        Assertions.assertThat(resultado).isFalse();
    }
}
