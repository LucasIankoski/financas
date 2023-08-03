package com.iankoski.financas.controllers;

import com.iankoski.financas.services.LancamentoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lancamento")
public class LancamentoController {

    private LancamentoService service;
}
