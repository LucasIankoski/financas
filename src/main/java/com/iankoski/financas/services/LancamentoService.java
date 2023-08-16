package com.iankoski.financas.services;

import com.iankoski.financas.entities.Lancamento;
import com.iankoski.financas.enums.StatusLancamento;
import com.iankoski.financas.enums.TipoLancamento;
import com.iankoski.financas.exceptions.RegrasException;
import com.iankoski.financas.repositories.LancamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class LancamentoService {

    @Autowired
    private LancamentoRepository repository;

    @Transactional
    public Lancamento salvar(Lancamento lancamento){
        validar(lancamento);
        lancamento.setStatus(StatusLancamento.PENDENTE);
        return repository.save(lancamento);
    }

    @Transactional
    public Lancamento atualizar(Lancamento lancamento){
        validar(lancamento);
        Objects.requireNonNull(lancamento.getId());
        return repository.save(lancamento);
    }

    public void deletar(Lancamento lancamento){
        Objects.requireNonNull(lancamento.getId());
        repository.delete(lancamento);

    }
    @Transactional(readOnly = true)
    public List<Lancamento> filtrar(Lancamento lancamento){
        Example example = Example.of(lancamento,
                ExampleMatcher.matching()
                        .withIgnoreCase()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));
        return repository.findAll(example);
    }

    public void atualizarStatus(Lancamento lancamento, StatusLancamento status){
        lancamento.setStatus(status);
        atualizar(lancamento);
    }

    public void validar(Lancamento lancamento){
        if(lancamento.getDescricao() == null || lancamento.getDescricao().trim().equals("")){
            throw new RegrasException("Informe a descrição do lançamento.");
        }

        if(lancamento.getMes() ==  null || lancamento.getMes() < 1 || lancamento.getMes() > 12){
            throw new RegrasException("O mês inserido é inválido.");
        }

        if(lancamento.getAno() == null || lancamento.getAno().toString().length() != 4){
            throw new RegrasException("O ano inserido é inválido");
        }

        if(lancamento.getUsuario() == null || lancamento.getUsuario().getId() == null){
            throw new RegrasException("Informe um usuário");
        }

        if(lancamento.getValor() == null || lancamento.getValor().compareTo(BigDecimal.ZERO) < 1){
            throw new RegrasException("O valor informado é inválido.");
        }

        if(lancamento.getTipoLancamento() == null){
            throw new RegrasException("Informe um tipo de Lançamento.");
        }
    }

    public Optional<Lancamento> encontrarPorId(Long id){
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    public BigDecimal consultarSaldoPorUsuario(Long id){
        BigDecimal receita = repository.consultarSaldoPorUsuarioETipoLancamento(id, TipoLancamento.RECEITA.name());
        BigDecimal despesa = repository.consultarSaldoPorUsuarioETipoLancamento(id, TipoLancamento.DESPESA.name());

        if(receita == null){
            receita = BigDecimal.ZERO;
        }

        if(despesa == null){
            despesa = BigDecimal.ZERO;
        }
        BigDecimal saldo = receita.subtract(despesa);
        return saldo;
    }
}
