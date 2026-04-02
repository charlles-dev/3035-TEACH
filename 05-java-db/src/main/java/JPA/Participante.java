package JPA;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "participantes")
public class Participante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome não pode estar em branco")
    @Column(nullable = false, length = 100)
    private String nome;

    @NotBlank(message = "O email não pode estar em branco")
    @Email(message = "Email inválido")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @OneToMany(mappedBy = "participante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Inscricao> inscricoes = new ArrayList<>();

    // Constructors
    public Participante() {
    }

    public Participante(String nome, String email) {
        this.nome = nome;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        inscricao.setParticipante(this);
    }

    public void removeInscricao(Inscricao inscricao) {
        inscricoes.remove(inscricao);
        inscricao.setParticipante(null);
    }

    @Override
    public String toString() {
        return "Participante{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
