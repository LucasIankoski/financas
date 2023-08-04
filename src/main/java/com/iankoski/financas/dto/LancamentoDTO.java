package com.iankoski.financas.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class LancamentoDTO {
    private Long id;
    private String descricao;
    private Integer mes;
    private Integer ano;
    private BigDecimal valor;
    private String tipoLancamento;
    private Long usuario;
    private String status;

}
