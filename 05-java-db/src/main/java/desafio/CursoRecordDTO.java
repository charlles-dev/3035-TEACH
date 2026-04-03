package desafio;

public record CursoRecordDTO(Long id, String nome, int cargaHoraria) {
    public static CursoRecordDTO fromEntity(Curso curso) {
        return new CursoRecordDTO(curso.getId(), curso.getNome(), curso.getCargaHoraria());
    }
}
