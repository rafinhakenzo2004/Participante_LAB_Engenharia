package com.trabalho.participante.controller;

import com.trabalho.participante.service.ParticipanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/participantes")
public class ParticipanteController {

    @Autowired
    private ParticipanteService participanteService;

    //ROTA DA TELA 1: Identificação Rápida por CPF (QR Code)
    @PostMapping("/escanear/evento/{eventoId}")
    public String escanearQrCode(
            @PathVariable Long eventoId, 
            @RequestParam("cpf") String cpf, 
            Model model) { 
        
        try {
            String resultado = participanteService.registrarPresencaPorQrCode(eventoId, cpf);
            model.addAttribute("mensagem", resultado);
            
        } catch (Exception e) {
            model.addAttribute("mensagem", "Erro: " + e.getMessage());
        }

        return "escanear";
    }

    //ROTA DA TELA 2: Cadastro Puro do Aluno

    @PostMapping("/cadastrar")
    public String cadastrarNovoParticipante(
            @RequestParam("nome") String nome,
            @RequestParam("cpf") String cpf,
            @RequestParam("email") String email,
            @RequestParam(value = "linkedin", required = false) String linkedin,
            Model model) {

        try {
            String resultado = participanteService.cadastrarNovoParticipante(nome, cpf, email, linkedin);
            model.addAttribute("mensagem", resultado);
            
        } catch (Exception e) {
            model.addAttribute("mensagem", "Erro ao cadastrar: " + e.getMessage());
        }

        return "escanear"; 
    }
}