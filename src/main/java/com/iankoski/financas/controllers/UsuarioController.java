package com.iankoski.financas.controllers;

import com.iankoski.financas.dto.UsuarioDTO;
import com.iankoski.financas.entities.Usuario;
import com.iankoski.financas.exceptions.AutenticacaoException;
import com.iankoski.financas.exceptions.RegrasException;
import com.iankoski.financas.services.LancamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.iankoski.financas.services.UsuarioService;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping(value = "/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @Autowired
    private LancamentoService lancamentoService;

    @PostMapping
    public ResponseEntity salvar(@RequestBody UsuarioDTO usuarioDTO){
        Usuario usuario = Usuario.builder()
                .nome(usuarioDTO.getNome())
                .email(usuarioDTO.getEmail())
                .senha(usuarioDTO.getSenha()).build();

        try {
            Usuario usuarioSalvo = service.salvarUsuario(usuario);
            return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
        } catch (RegrasException e){
                return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("/autenticacao")
    public ResponseEntity autenticar(@RequestBody UsuarioDTO usuarioDTO){
        try {
            Usuario autenticacao = service.autenticar(usuarioDTO.getEmail(), usuarioDTO.getSenha());
            return ResponseEntity.ok(autenticacao);
        } catch (AutenticacaoException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/saldo/{id}")
    public ResponseEntity consultarSaldo(Long id){
        Optional<Usuario> usuario = service.encontrarPorId(id);

        if(!usuario.isPresent()){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        BigDecimal saldo = lancamentoService.consultarSaldoPorUsuario(id);
        return ResponseEntity.ok(saldo);
    }
}
