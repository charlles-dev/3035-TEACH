package desafio;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class EscolaDAO {

    private final EntityManagerFactory emf;
    private final EntityManager em;

    public EscolaDAO() {
        this.emf = Persistence.createEntityManagerFactory("escolaPU");
        this.em = emf.createEntityManager();
    }

    public void fechar() {
        em.close();
        emf.close();
    }

    // --- ALUNO ---

    public void inserirAluno(Aluno aluno) {
        em.getTransaction().begin();
        em.persist(aluno);
        em.getTransaction().commit();
    }

    public List<Aluno> listarAlunos() {
        return em.createQuery("SELECT a FROM Aluno a", Aluno.class).getResultList();
    }

    public Aluno buscarAlunoPorEmail(String email) {
        TypedQuery<Aluno> query = em.createQuery("SELECT a FROM Aluno a WHERE a.email = :email", Aluno.class);
        query.setParameter("email", email);
        return query.getResultStream().findFirst().orElse(null);
    }

    // --- CURSO ---

    public void inserirCurso(Curso curso) {
        em.getTransaction().begin();
        em.persist(curso);
        em.getTransaction().commit();
    }

    public List<Curso> listarCursos() {
        return em.createQuery("SELECT c FROM Curso c", Curso.class).getResultList();
    }

    public List<Curso> buscarCursoPorNome(String nome) {
        TypedQuery<Curso> query = em.createQuery("SELECT c FROM Curso c WHERE LOWER(c.nome) LIKE LOWER(:nome)", Curso.class);
        query.setParameter("nome", "%" + nome + "%");
        return query.getResultList();
    }

    public Curso buscarCursoPorId(Long id) {
        return em.find(Curso.class, id);
    }
    
    public Curso atualizarCurso(Curso curso) {
        em.getTransaction().begin();
        Curso cursoAtualizado = em.merge(curso);
        em.getTransaction().commit();
        return cursoAtualizado;
    }

    // --- MATRICULA ---

    public void inserirMatricula(Matricula matricula) {
        em.getTransaction().begin();
        em.persist(matricula);
        em.getTransaction().commit();
    }
    
    public Matricula atualizarMatricula(Matricula matricula) {
        em.getTransaction().begin();
        Matricula matNova = em.merge(matricula);
        em.getTransaction().commit();
        return matNova;
    }
    
    public Matricula buscarMatriculaPorId(Long id) {
        return em.find(Matricula.class, id);
    }

    public List<Matricula> listarMatriculas() {
        return em.createQuery("SELECT m FROM Matricula m JOIN FETCH m.aluno JOIN FETCH m.curso WHERE m.status = 'ATIVA'", Matricula.class).getResultList();
    }

    // --- RELATORIO ---

    public List<RelatorioEngajamentoDTO> gerarRelatorioEngajamento() {
        List<Curso> cursos = em.createQuery("SELECT DISTINCT c FROM Curso c LEFT JOIN FETCH c.matriculas m LEFT JOIN FETCH m.aluno", Curso.class).getResultList();
        
        List<RelatorioEngajamentoDTO> relatorios = new ArrayList<>();
        LocalDate hoje = LocalDate.now();
        LocalDate trintaDiasAtras = hoje.minusDays(30);

        for (Curso curso : cursos) {
            String nome = curso.getNome();
            long totalAlunos = 0;
            double somaIdade = 0.0;
            int contadorIdades = 0;
            long matriculasUltimos30Dias = 0;

            if (curso.getMatriculas() != null) {
                totalAlunos = curso.getMatriculas().size();
                
                for (Matricula m : curso.getMatriculas()) {
                    if (m.getStatus() != StatusMatricula.ATIVA) continue;
                    
                    // Soma de idades
                    if (m.getAluno() != null && m.getAluno().getDataNascimento() != null) {
                        int idade = Period.between(m.getAluno().getDataNascimento(), hoje).getYears();
                        somaIdade += idade;
                        contadorIdades++;
                    }
                    
                    // Matrículas os últimos 30 dias
                    if (m.getDataMatricula() != null && !m.getDataMatricula().isBefore(trintaDiasAtras)) {
                        matriculasUltimos30Dias++;
                    }
                }
            }

            Double mediaIdade = contadorIdades > 0 ? somaIdade / contadorIdades : 0.0;
            relatorios.add(new RelatorioEngajamentoDTO(nome, totalAlunos, mediaIdade, matriculasUltimos30Dias));
        }

        return relatorios;
    }
}
