package desafio;

public record AlunoRecordDTO(Long id, String nome, String email) {
    public static AlunoRecordDTO fromEntity(Aluno aluno) {
        return new AlunoRecordDTO(aluno.getId(), aluno.getNome(), aluno.getEmail());
    }
}
