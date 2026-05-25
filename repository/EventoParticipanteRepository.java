package com.trabalho.participante.repository;

import com.trabalho.participante.model.EventoParticipante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EventoParticipanteRepository extends JpaRepository<EventoParticipante, Long> {
    
    // Se precisar listar todos os participantes de um Evento específico
    List<EventoParticipante> findByEventoId(Long eventoId);
    
    boolean existsByParticipanteIdAndEventoId(Long participanteId, Long eventoId);
}