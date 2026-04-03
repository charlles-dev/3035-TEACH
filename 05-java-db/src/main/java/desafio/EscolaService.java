package desafio;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EscolaService {

    private final EscolaDAO dao;
    private final Validator validator;
    private final ValidatorFactory factory;

    public EscolaService() {
        this.dao = new EscolaDAO();
        this.factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    public void fechar() {
        dao.fechar();
        factory.close();
    }

    private <T> void validar(T entidade) {
        Set<ConstraintViolation<T>> violations = validator.validate(entidade);
        if (!violations.isEmpty()) {
            String mensagens = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            throw new IllegalArgumentException("Erro de Validação: " + mensagens);
        }
    }

    // --- ALUNO ---

    public AlunoRecordDTO cadastrarAluno(String nome, String email, LocalDate dataNascimento) {
        if (dao.buscarAlunoPorEmail(email) != null) {
            throw new IllegalArgumentException("Erro: Email já cadastrado no sistema.");
        }

        Aluno aluno = new Aluno(nome, email, dataNascimento);
        validar(aluno);
        
        dao.inserirAluno(aluno);
        return AlunoRecordDTO.fromEntity(aluno);
    }

    public List<AlunoRecordDTO> listarAlunos() {
        return dao.listarAlunos().stream()
                .map(AlunoRecordDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public AlunoRecordDTO buscarAlunoPorEmail(String email) {
        Aluno a = dao.buscarAlunoPorEmail(email);
        return a != null ? AlunoRecordDTO.fromEntity(a) : null;
    }

    // --- CURSO ---

    public CursoRecordDTO cadastrarCurso(String nome, String descricao, int cargaHoraria) {
        Curso curso = new Curso(nome, descricao, cargaHoraria);
        validar(curso);
        
        dao.inserirCurso(curso);
        return CursoRecordDTO.fromEntity(curso);
    }

    public List<CursoRecordDTO> listarCursos() {
        return dao.listarCursos().stream()
                .map(CursoRecordDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<CursoRecordDTO> buscarCursoPorNome(String nome) {
        return dao.buscarCursoPorNome(nome).stream()
                .map(CursoRecordDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public CursoRecordDTO atualizarCurso(Long id, int novaCargaHoraria, String novaDescricao) {
        Curso curso = dao.buscarCursoPorId(id);
        if (curso == null) {
            throw new IllegalArgumentException("Erro: Curso não encontrado.");
        }
        
        curso.setCargaHoraria(novaCargaHoraria);
        if (novaDescricao != null && !novaDescricao.isBlank()) {
            curso.setDescricao(novaDescricao);
        }
        validar(curso);

        Curso atualizado = dao.atualizarCurso(curso);
        return CursoRecordDTO.fromEntity(atualizado);
    }

    // --- MATRICULA ---

    public MatriculaRecordDTO matricularAluno(String emailAluno, String nomeCursoExato) {
        Aluno aluno = dao.buscarAlunoPorEmail(emailAluno);
        if (aluno == null) {
            throw new IllegalArgumentException("Erro: Aluno não encontrado com este email.");
        }

        List<Curso> cursos = dao.buscarCursoPorNome(nomeCursoExato);
        if (cursos.isEmpty()) {
            throw new IllegalArgumentException("Erro: Curso não encontrado.");
        }
        Curso curso = cursos.get(0); // Pega o primeiro que bate (simples match)

        // Verificando se já está matriculado ativamente
        List<Matricula> ativas = dao.listarMatriculas();
        boolean jaMatriculado = ativas.stream().anyMatch(m -> 
            m.getAluno().getId().equals(aluno.getId()) && 
            m.getCurso().getId().equals(curso.getId()) &&
            m.getStatus() == StatusMatricula.ATIVA
        );
        if (jaMatriculado) {
            throw new IllegalArgumentException("Erro: O aluno já está matriculado(a) ativamente neste curso.");
        }

        Matricula mat = new Matricula(aluno, curso, LocalDate.now());
        validar(mat);
        dao.inserirMatricula(mat);
        
        return MatriculaRecordDTO.fromEntity(mat);
    }

    public List<MatriculaRecordDTO> listarMatriculasAtivas() {
        return dao.listarMatriculas().stream()
                .map(MatriculaRecordDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public void cancelarMatricula(Long matriculaId) {
        Matricula mat = dao.buscarMatriculaPorId(matriculaId);
        if (mat == null) {
            throw new IllegalArgumentException("Erro: Matrícula não encontrada.");
        }
        if (mat.getStatus() == StatusMatricula.CANCELADA) {
            throw new IllegalArgumentException("Erro: A Matrícula já encontra-se cancelada.");
        }
        
        mat.setStatus(StatusMatricula.CANCELADA);
        dao.atualizarMatricula(mat);
    }

    // --- RELATORIOS ---
    
    public List<RelatorioEngajamentoDTO> gerarRelatorio() {
        return dao.gerarRelatorioEngajamento();
    }
}
