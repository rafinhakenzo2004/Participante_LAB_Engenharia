package com.trabalho.participante.service;

import com.trabalho.participante.model.*;
import com.trabalho.participante.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ParticipanteService {

    @Autowired
    private ParticipanteRepository participanteRepository;

    @Autowired
    private EventoParticipanteRepository eventoParticipanteRepository;

    @Autowired
    private EventoRepository eventoRepository;

    // Registro de presença e pontuação do aluno via QR Code (Tela 1).
    @Transactional
    public String registrarPresencaPorQrCode(Long eventoId, String cpf) {
        
        // Verifica se o ID capturado pelo QR Code realmente condiz com um evento válido no sistema.
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado."));
    	
        // Busca o participante no banco com base no CPF digitado na tela rápida.
        Participante participante = participanteRepository.findByCpf(cpf)
                .orElseThrow(() -> new RuntimeException("CPF não cadastrado. Por favor, realize o cadastro no evento primeiro."));

        // Vai até a tabela intermediária 'Evento_Participante' e checa se este ID de aluno 
        // já escaneou este ID de evento específico antes. Evita que o aluno ganhe pontos infinitos.
        boolean jaParticipou = eventoParticipanteRepository.existsByParticipanteIdAndEventoId(participante.getId(), evento.getId());
        if (jaParticipou) {
            return "Ops! Você já registrou presença e ganhou os pontos deste evento.";
        }

        // Conecta com a classe intermediaria
        EventoParticipante novaInscricao = new EventoParticipante();
        novaInscricao.setParticipante(participante);
        novaInscricao.setEvento(evento);

        // Como o certificado é garantido ao escanear o QR Code, gerei os dados básicos dele aqui.
        Certificado certificadoAutomatico = new Certificado();
        certificadoAutomatico.setTitulo("Certificado: " + evento.getNome());
        certificadoAutomatico.setDescricao("Certificado gerado automaticamente pela participação.");
        certificadoAutomatico.setTipo("Automático");
        novaInscricao.setCertificado(certificadoAutomatico);

        // Pega quantos pontos o evento vale e soma na coluna do aluno.
        int pontosDoEvento = (evento.getPontos() != null) ? evento.getPontos() : 0;
        participante.setPontos(participante.getPontos() + pontosDoEvento); // Altere para setPontos se necessário

        participanteRepository.save(participante);
        eventoParticipanteRepository.save(novaInscricao);

        return "Presença confirmada! Pontos adicionados: " + pontosDoEvento + ". Total atual: " + participante.getPontos();
    }

    // Primeiro cadastro (Tela 2).
    @Transactional
    public String cadastrarNovoParticipante(String nome, String cpf, String email, String linkedin) {
        
        // Verifica se o CPF digitado já não pertence a outro aluno para evitar duplicações.
        if (participanteRepository.findByCpf(cpf).isPresent()) {
            return "Erro: Este CPF já está cadastrado no sistema!";
        }

        // Mapeia os dados enviados pelo formulário do Frontend para o objeto que o JPA entende.
        Participante novoParticipante = new Participante();
        novoParticipante.setNome(nome);
        novoParticipante.setCpf(cpf);
        novoParticipante.setEmail(email);
        novoParticipante.setLinkedin(linkedin);
        novoParticipante.setPontos(0);

        participanteRepository.save(novoParticipante);
        
        return "Cadastro realizado com sucesso! Volte para a tela anterior e digite seu CPF para garantir sua presença.";
    }
}