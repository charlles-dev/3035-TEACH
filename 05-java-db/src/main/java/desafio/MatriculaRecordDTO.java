package desafio;

import java.time.LocalDate;

public record MatriculaRecordDTO(Long id, String nomeAluno, String nomeCurso, LocalDate dataMatricula, StatusMatricula status) {
    public static MatriculaRecordDTO fromEntity(Matricula matricula) {
        return new MatriculaRecordDTO(
                matricula.getId(),
                matricula.getAluno().getNome(),
                matricula.getCurso().getNome(),
                matricula.getDataMatricula(),
                matricula.getStatus()
        );
    }
}
