package desafio;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class MainDesafio {

    private static final Logger logger = LoggerFactory.getLogger(MainDesafio.class);
    private static EscolaService servico;
    private static Terminal terminal;
    private static LineReader reader;
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void main(String[] args) {
        try {
            terminal = TerminalBuilder.builder()
                    .system(true)
                    .build();
            reader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .build();

            terminal.writer()
                    .println(boldColor("Inicializando conexão com o Banco de Dados (Escola)...", AttributedStyle.CYAN));
            terminal.flush();

            servico = new EscolaService();

            boolean executando = true;
            while (executando) {
                mostrarMenu();
                String opcao = readLine("\nQual a sua escolha? ");

                limparTela();
                switch (opcao.trim()) {
                    case "1":
                        cadastrarAluno();
                        break;
                    case "2":
                        cadastrarCurso();
                        break;
                    case "3":
                        realizarMatricula();
                        break;
                    case "4":
                        listarAlunos();
                        break;
                    case "5":
                        listarCursos();
                        break;
                    case "6":
                        listarMatriculas();
                        break;
                    case "7":
                        buscarAlunoPorEmail();
                        break;
                    case "8":
                        buscarCursoPorNome();
                        break;
                    case "9":
                        atualizarCurso();
                        break;
                    case "10":
                        cancelarMatricula();
                        break;
                    case "11":
                        exibirRelatorioAvancado();
                        break;
                    case "0":
                        executando = false;
                        terminal.writer().println(
                                boldColor("\n👋 Encerrando o Sistema Escolar. Até logo!\n", AttributedStyle.GREEN));
                        break;
                    default:
                        terminal.writer().println(boldColor("⚠️ Opção inválida!", AttributedStyle.RED));
                        esperarEnter();
                }
            }

        } catch (Exception e) {
            logger.error("Erro fatal: ", e);
            if (terminal != null) {
                terminal.writer().println(boldColor("❌ ERRO: " + e.getMessage(), AttributedStyle.RED));
            }
        } finally {
            if (servico != null)
                servico.fechar();
        }
    }

    private static void mostrarMenu() {
        limparTela();
        terminal.writer()
                .println(boldColor("╔══════════════════════════════════════════════════╗", AttributedStyle.MAGENTA));
        terminal.writer()
                .println(boldColor("║                🎓 SISTEMA ESCOLAR  🎓           ║", AttributedStyle.MAGENTA));
        terminal.writer()
                .println(boldColor("╚══════════════════════════════════════════════════╝", AttributedStyle.MAGENTA));
        terminal.writer().println();
        terminal.writer().println("  [1] 👤 Novo Cadastro de Aluno");
        terminal.writer().println("  [2] 📚 Novo Cadastro de Curso");
        terminal.writer().println("  [3] 📝 Realizar Matrícula");
        terminal.writer().println("  [4] 👥 Listar todos os Alunos");
        terminal.writer().println("  [5] 📖 Listar todos os Cursos");
        terminal.writer().println("  [6] 🗂️  Listar todas as Matrículas (Ativas)");
        terminal.writer().println("  [7] 🔍 Buscar Aluno por E-mail");
        terminal.writer().println("  [8] 🔍 Buscar Curso por Nome");
        terminal.writer().println(boldColor("  [9] ✏️  Atualizar Dados de Curso", AttributedStyle.YELLOW));
        terminal.writer().println(boldColor("  [10] ❌ Cancelar Matrícula", AttributedStyle.YELLOW));
        terminal.writer().println("  [11] 📊 Relatório Avançado de Engajamento");
        terminal.writer().println(boldColor("  [0] ❌ Sair", AttributedStyle.RED));
        terminal.flush();
    }

    // --- AÇÕES ---

    private static void cadastrarAluno() {
        terminal.writer().println(boldColor("--- NOVO ALUNO ---", AttributedStyle.CYAN));
        String nome = readLine("Nome Completo: ");
        String email = readLine("E-mail: ");
        String dataStr = readLine("Data Nat. (DD/MM/YYYY): ");

        try {
            LocalDate data = LocalDate.parse(dataStr, dateFormatter);
            servico.cadastrarAluno(nome, email, data);
            terminal.writer().println(boldColor("✅ Aluno cadastrado com sucesso e Validado!", AttributedStyle.GREEN));
        } catch (DateTimeParseException e) {
            terminal.writer().println(boldColor("❌ Formato de data inválido! Use DD/MM/YYYY.", AttributedStyle.RED));
        } catch (IllegalArgumentException e) {
            terminal.writer().println(boldColor("❌ " + e.getMessage(), AttributedStyle.RED));
        } catch (Exception e) {
            terminal.writer().println(boldColor("❌ Erro ao salvar banco de dados.", AttributedStyle.RED));
        }
        esperarEnter();
    }

    private static void cadastrarCurso() {
        terminal.writer().println(boldColor("--- NOVO CURSO ---", AttributedStyle.CYAN));
        String nome = readLine("Nome do Curso: ");
        String descricao = readLine("Descrição: ");
        String cargaStr = readLine("Carga Horária (horas): ");

        try {
            int cargaHoraria = Integer.parseInt(cargaStr);
            servico.cadastrarCurso(nome, descricao, cargaHoraria);
            terminal.writer().println(boldColor("✅ Curso cadastrado com sucesso e Validado!", AttributedStyle.GREEN));
        } catch (NumberFormatException e) {
            terminal.writer().println(boldColor("❌ A carga horária deve ser um número inteiro.", AttributedStyle.RED));
        } catch (IllegalArgumentException e) {
            terminal.writer().println(boldColor("❌ " + e.getMessage(), AttributedStyle.RED));
        } catch (Exception e) {
            terminal.writer().println(boldColor("❌ Erro fatal.", AttributedStyle.RED));
        }
        esperarEnter();
    }

    private static void realizarMatricula() {
        terminal.writer().println(boldColor("--- REALIZAR MATRÍCULA ---", AttributedStyle.CYAN));
        String email = readLine("E-mail Exato do Aluno: ");
        String curso = readLine("Nome (ou trecho) do Curso: ");

        try {
            MatriculaRecordDTO mat = servico.matricularAluno(email, curso);
            terminal.writer().println(
                    boldColor("✅ Aluno matriculado com sucesso na turma: " + mat.nomeCurso(), AttributedStyle.GREEN));
        } catch (IllegalArgumentException e) {
            terminal.writer().println(boldColor("❌ " + e.getMessage(), AttributedStyle.RED));
        } catch (Exception e) {
            terminal.writer().println(boldColor("❌ Falha inesperada.", AttributedStyle.RED));
        }
        esperarEnter();
    }

    private static void cancelarMatricula() {
        terminal.writer().println(boldColor("--- CANCELAR MATRÍCULA (Soft Delete) ---", AttributedStyle.CYAN));
        String matIdStr = readLine("ID da Matrícula: ");

        try {
            Long matId = Long.parseLong(matIdStr);
            servico.cancelarMatricula(matId);
            terminal.writer().println(
                    boldColor("✅ Matrícula marcada como CANCELADA. Histórico preservado.", AttributedStyle.GREEN));
        } catch (NumberFormatException e) {
            terminal.writer().println(boldColor("❌ ID deve ser numérico.", AttributedStyle.RED));
        } catch (IllegalArgumentException e) {
            terminal.writer().println(boldColor("❌ " + e.getMessage(), AttributedStyle.RED));
        }
        esperarEnter();
    }

    private static void atualizarCurso() {
        terminal.writer().println(boldColor("--- ATUALIZAR CURSO ---", AttributedStyle.CYAN));
        String idStr = readLine("ID do Curso a editar: ");
        try {
            Long id = Long.parseLong(idStr);
            String novaDesc = readLine("Nova Descrição (Deixe em branco para manter atual): ");
            String novaCarga = readLine("Nova Carga Horária: ");
            int cargaHoraria = Integer.parseInt(novaCarga);

            CursoRecordDTO cursoDTO = servico.atualizarCurso(id, cargaHoraria, novaDesc);
            terminal.writer().println(
                    boldColor("✅ Curso ID " + cursoDTO.id() + " atualizado com sucesso!", AttributedStyle.GREEN));
        } catch (NumberFormatException e) {
            terminal.writer().println(boldColor("❌ Valores numéricos inválidos.", AttributedStyle.RED));
        } catch (IllegalArgumentException e) {
            terminal.writer().println(boldColor("❌ " + e.getMessage(), AttributedStyle.RED));
        }
        esperarEnter();
    }

    private static void listarAlunos() {
        terminal.writer().println(boldColor("--- LISTA DE ALUNOS (DTOS) ---", AttributedStyle.CYAN));
        List<AlunoRecordDTO> alunos = servico.listarAlunos();
        if (alunos.isEmpty()) {
            terminal.writer().println("Nenhum aluno no sistema.");
        } else {
            for (AlunoRecordDTO a : alunos) {
                terminal.writer().println(String.format("ID: %-3d | Nome: %-20s | E-mail: %s",
                        a.id(), a.nome(), a.email()));
            }
        }
        esperarEnter();
    }

    private static void listarCursos() {
        terminal.writer().println(boldColor("--- LISTA DE CURSOS (DTOS) ---", AttributedStyle.CYAN));
        List<CursoRecordDTO> cursos = servico.listarCursos();
        if (cursos.isEmpty()) {
            terminal.writer().println("Nenhum curso cadastrado.");
        } else {
            for (CursoRecordDTO c : cursos) {
                terminal.writer().println(String.format("ID: %-3d | Nome: %-20s | CH: %-3d h",
                        c.id(), c.nome(), c.cargaHoraria()));
            }
        }
        esperarEnter();
    }

    private static void listarMatriculas() {
        terminal.writer().println(boldColor("--- LISTA DE MATRÍCULAS (ATIVAS) ---", AttributedStyle.CYAN));
        List<MatriculaRecordDTO> matriculas = servico.listarMatriculasAtivas();
        if (matriculas.isEmpty()) {
            terminal.writer().println("Nenhuma matrícula ativa localizada.");
        } else {
            for (MatriculaRecordDTO m : matriculas) {
                String data = m.dataMatricula() != null ? m.dataMatricula().format(dateFormatter) : "N/A";
                terminal.writer().println(String.format("MATRÍCULA #%d | Data: %s | Aluno: %-20s | Curso: %-20s | %s",
                        m.id(), data, m.nomeAluno(), m.nomeCurso(), m.status()));
            }
        }
        esperarEnter();
    }

    private static void buscarAlunoPorEmail() {
        terminal.writer().println(boldColor("--- BUSCAR ALUNO ---", AttributedStyle.CYAN));
        String email = readLine("E-mail a pesquisar: ");
        AlunoRecordDTO aluno = servico.buscarAlunoPorEmail(email);

        if (aluno == null) {
            terminal.writer().println(boldColor("❌ Nenhum aluno localizado com: " + email, AttributedStyle.YELLOW));
        } else {
            terminal.writer().println(boldColor("✔ Aluno Encontrado:", AttributedStyle.GREEN));
            terminal.writer().println("ID: " + aluno.id() + " | Nome: " + aluno.nome());
        }
        esperarEnter();
    }

    private static void buscarCursoPorNome() {
        terminal.writer().println(boldColor("--- BUSCAR CURSO ---", AttributedStyle.CYAN));
        String nome = readLine("Nome (ou trecho) do curso: ");
        List<CursoRecordDTO> cursos = servico.buscarCursoPorNome(nome);

        if (cursos.isEmpty()) {
            terminal.writer().println(boldColor("❌ Nenhum curso encontrado.", AttributedStyle.YELLOW));
        } else {
            terminal.writer().println(boldColor("✔ Cursos Encontrados:", AttributedStyle.GREEN));
            for (CursoRecordDTO c : cursos) {
                terminal.writer().println(" - ID: " + c.id() + " | " + c.nome() + " (" + c.cargaHoraria() + "h)");
            }
        }
        esperarEnter();
    }

    private static void exibirRelatorioAvancado() {
        terminal.writer().println(boldColor("--- RELATÓRIO DE ENGAJAMENTO (ATIVOS) ---", AttributedStyle.MAGENTA));
        List<RelatorioEngajamentoDTO> relatorios = servico.gerarRelatorio();

        if (relatorios.isEmpty()) {
            terminal.writer().println("Sem dados.");
        } else {
            for (RelatorioEngajamentoDTO rel : relatorios) {
                terminal.writer().println(rel.toString());
            }
        }
        esperarEnter();
    }

    // --- UTILITÁRIOS ---

    private static String readLine(String prompt) {
        String input;
        try {
            terminal.writer().print(boldColor(prompt, AttributedStyle.CYAN));
            terminal.flush();
            input = reader.readLine("");
        } catch (org.jline.reader.UserInterruptException | org.jline.reader.EndOfFileException e) {
            return "0";
        }
        return input;
    }

    private static void esperarEnter() {
        try {
            terminal.writer().print(boldColor("\n[ Pressione ENTER para retornar ao Menu ]", AttributedStyle.CYAN));
            terminal.flush();
            reader.readLine("");
        } catch (Exception e) {
            // ignore
        }
    }

    private static void limparTela() {
        terminal.writer().print("\033[H\033[2J");
        terminal.flush();
    }

    private static String boldColor(String text, int color) {
        return new AttributedStringBuilder()
                .style(AttributedStyle.DEFAULT.bold().foreground(color))
                .append(text)
                .toAnsi();
    }
}
