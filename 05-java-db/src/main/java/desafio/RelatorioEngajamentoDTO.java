package desafio;

public class RelatorioEngajamentoDTO {
    private String cursoNome;
    private Long totalAlunos;
    private Double mediaIdade;
    private Long matriculasUltimos30Dias;

    public RelatorioEngajamentoDTO(String cursoNome, Long totalAlunos, Double mediaIdade, Long matriculasUltimos30Dias) {
        this.cursoNome = cursoNome;
        this.totalAlunos = totalAlunos;
        this.mediaIdade = mediaIdade;
        this.matriculasUltimos30Dias = matriculasUltimos30Dias;
    }

    public String getCursoNome() {
        return cursoNome;
    }

    public Long getTotalAlunos() {
        return totalAlunos;
    }

    public Double getMediaIdade() {
        return mediaIdade;
    }

    public Long getMatriculasUltimos30Dias() {
        return matriculasUltimos30Dias;
    }

    @Override
    public String toString() {
        return String.format("Curso: %s | Total de Alunos: %d | Idade Média: %.1f | Matrículas (últm. 30 dias): %d",
                cursoNome, totalAlunos, mediaIdade != null ? mediaIdade : 0.0, matriculasUltimos30Dias);
    }
}
