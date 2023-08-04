package com.iankoski.financas.controllers;

import com.iankoski.financas.dto.LancamentoDTO;
import com.iankoski.financas.entities.Lancamento;
import com.iankoski.financas.entities.Usuario;
import com.iankoski.financas.enums.StatusLancamento;
import com.iankoski.financas.enums.TipoLancamento;
import com.iankoski.financas.exceptions.RegrasException;
import com.iankoski.financas.services.LancamentoService;
import com.iankoski.financas.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lancamento")
public class LancamentoController {

    @Autowired
    private LancamentoService service;
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity salvar (@RequestBody LancamentoDTO dto){
       try {
           Lancamento lancamento = converterDTOparaEntidade(dto);
           lancamento = service.salvar(lancamento);
           return new ResponseEntity(lancamento, HttpStatus.CREATED);
       } catch (RegrasException e){
           return ResponseEntity.badRequest().body(e.getMessage());
       }

    }

    public Lancamento converterDTOparaEntidade(LancamentoDTO dto){
        Lancamento lancamento = new Lancamento();
        lancamento.setId(dto.getId());
        lancamento.setDescricao(dto.getDescricao());
        lancamento.setMes(dto.getMes());
        lancamento.setAno(dto.getAno());
        lancamento.setValor(dto.getValor());

        Usuario usuario = usuarioService.encontrarPorId(dto.getId()).orElseThrow(() -> new RegrasException("Usuário não encontrado para o ID informado."));

        lancamento.setUsuario(usuario);
        lancamento.setTipoLancamento(TipoLancamento.valueOf(dto.getTipoLancamento()));
        lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));

        return lancamento;
    }
}
