package JPA;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inscricoes")
public class Inscricao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dataInscricao;

    @Column(nullable = false)
    private Boolean confirmada = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participante_id", nullable = false)
    private Participante participante;

    // Constructors
    public Inscricao() {
    }

    public Inscricao(LocalDateTime dataInscricao, Boolean confirmada) {
        this.dataInscricao = dataInscricao;
        this.confirmada = confirmada;
    }

    public Inscricao(Evento evento, Participante participante) {
        this.evento = evento;
        this.participante = participante;
        this.dataInscricao = LocalDateTime.now();
        this.confirmada = false;
    }

    public Inscricao(Evento evento, Participante participante, Boolean confirmada) {
        this.evento = evento;
        this.participante = participante;
        this.dataInscricao = LocalDateTime.now();
        this.confirmada = confirmada;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataInscricao() {
        return dataInscricao;
    }

    public void setDataInscricao(LocalDateTime dataInscricao) {
        this.dataInscricao = dataInscricao;
    }

    public Boolean getConfirmada() {
        return confirmada;
    }

    public void setConfirmada(Boolean confirmada) {
        this.confirmada = confirmada;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public Participante getParticipante() {
        return participante;
    }

    public void setParticipante(Participante participante) {
        this.participante = participante;
    }

    @Override
    public String toString() {
        return "Inscricao{" +
                "id=" + id +
                ", dataInscricao=" + dataInscricao +
                ", confirmada=" + confirmada +
                ", evento=" + (evento != null ? evento.getNome() : "null") +
                ", participante=" + (participante != null ? participante.getNome() : "null") +
                '}';
    }
}
