package com.trabalho.participante.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "Evento_Participante",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"Participanteid", "Eventoid"})
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventoParticipante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "Participanteid", nullable = false)
    private Participante participante;

    @ManyToOne
    @JoinColumn(name = "Eventoid", nullable = false)
    private Evento evento;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Certificadoid")
    private Certificado certificado;
}