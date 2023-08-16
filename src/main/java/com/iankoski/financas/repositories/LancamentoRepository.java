package com.iankoski.financas.repositories;

import com.iankoski.financas.entities.Lancamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

    @Query(value =
            "SELECT SUM(L.VALOR) " +
                    "FROM Lancamento L JOIN L.Usuario U" +
                    "WHERE U.id = :idUsuario AND L.tipoLancamento = :tipo GROUP BY U ")
    BigDecimal consultarSaldoPorUsuarioETipoLancamento(@Param("idUsuario") Long idUsuario,
                                                       @Param("tipo") String tipo);
}
