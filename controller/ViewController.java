package com.trabalho.participante.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller 
public class ViewController {

    @GetMapping("/escanear")
    public String abrirTelaDeTeste() {
        return "escanear"; // Retorna o nome do arquivo escanear.html
    }
}