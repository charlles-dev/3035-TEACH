package JPA;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "eventos")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do evento não pode ser vazio")
    @Column(nullable = false, length = 100)
    private String nome;

    @NotNull(message = "A data do evento é obrigatória")
    @FutureOrPresent(message = "A data do evento deve ser hoje ou no futuro")
    @Column(nullable = false)
    private LocalDate data;

    @NotBlank(message = "O local do evento não pode ser vazio")
    @Column(nullable = false, length = 200)
    private String local;

    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Inscricao> inscricoes = new ArrayList<>();

    // Constructors
    public Evento() {
    }

    public Evento(String nome, LocalDate data, String local) {
        this.nome = nome;
        this.data = data;
        this.local = local;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public List<Inscricao> getInscricoes() {
        return inscricoes;
    }

    public void setInscricoes(List<Inscricao> inscricoes) {
        this.inscricoes = inscricoes;
    }

    // Helper methods
    public void addInscricao(Inscricao inscricao) {
        inscricoes.add(inscricao);
        inscricao.setEvento(this);
    }

    public void removeInscricao(Inscricao inscricao) {
        inscricoes.remove(inscricao);
        inscricao.setEvento(null);
    }

    @Override
    public String toString() {
        return "Evento{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", data=" + data +
                ", local='" + local + '\'' +
                '}';
    }
}
