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
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody LancamentoDTO dto){
            return service.encontrarPorId(id).map(entidade -> {
                try{
                    Lancamento lancamento = converterDTOparaEntidade(dto);
                    lancamento.setId(entidade.getId());
                    service.atualizar(lancamento);
                    return ResponseEntity.ok(lancamento);
                } catch (RegrasException e){
                    return ResponseEntity.badRequest().body(e.getMessage());
                }
            }).orElseGet(() ->
                    new ResponseEntity("Lançamento não encontrado.", HttpStatus.BAD_REQUEST));

    }

    @DeleteMapping("{id}")
    public ResponseEntity deletar(@PathVariable("id") Long id){
        return service.encontrarPorId(id).map(entidade -> {
            service.deletar(entidade);
            return new ResponseEntity("Lançamento deletado com sucesso.", HttpStatus.NO_CONTENT);
        }).orElseGet(() ->
                new ResponseEntity("Lançamento não encontrado.", HttpStatus.BAD_REQUEST));
    }

    public Lancamento converterDTOparaEntidade(LancamentoDTO dto){
        Lancamento lancamento = new Lancamento();
        lancamento.setId(dto.getId());
        lancamento.setDescricao(dto.getDescricao());
        lancamento.setMes(dto.getMes());
        lancamento.setAno(dto.getAno());
        lancamento.setValor(dto.getValor());

        Usuario usuario = usuarioService.encontrarPorId(dto.getUsuario()).orElseThrow(() -> new RegrasException("Usuário não encontrado para o ID informado."));

        lancamento.setUsuario(usuario);
        if(dto.getTipoLancamento() != null) {
            lancamento.setTipoLancamento(TipoLancamento.valueOf(dto.getTipoLancamento()));
        }

        if (dto.getStatus() != null) {
            lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));
        }

        return lancamento;
    }
    @GetMapping
    public ResponseEntity filtrar(@RequestParam(value = "descricao", required = false) String descricao,
                                  @RequestParam(value = "mes", required = false) Integer mes,
                                  @RequestParam(value = "ano", required = false) Integer ano,
                                  @RequestParam(value = "usuario", required = true) Long idUsuario){

        Optional<Usuario> usuario = usuarioService.encontrarPorId(idUsuario);
        Lancamento lancamentoFiltrado = new Lancamento();
        lancamentoFiltrado.setDescricao(descricao);
        lancamentoFiltrado.setMes(mes);
        lancamentoFiltrado.setAno(ano);

        if(!usuario.isPresent()){
            return ResponseEntity.badRequest().body("Não foi possível encontrar lançamentos: Usuário inválido.");
        } else {
            lancamentoFiltrado.setUsuario(usuario.get());
        }

        List<Lancamento> lancamentos = service.filtrar(lancamentoFiltrado);
        return ResponseEntity.ok(lancamentos);






    }
}
