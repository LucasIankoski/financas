package com.iankoski.financas.repositories;

import com.iankoski.financas.entities.Lancamento;
import com.iankoski.financas.enums.TipoLancamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

    @Query(value =
            "select sum(l.valor) " +
                    "from Lancamento l join l.usuario u " +
                    "where u.id = :idUsuario and l.tipoLancamento = :tipo group by u ")
    BigDecimal consultarSaldoPorUsuarioETipoLancamento(@Param("idUsuario") Long idUsuario, @Param("tipo") TipoLancamento tipo);

}
