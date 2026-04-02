package JPA;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.fusesource.jansi.AnsiConsole;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static EntityManagerFactory emf;
    private static EntityManager em;
    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Cores ANSI para um verdadeiro UI/UX de Terminal
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BOLD = "\u001B[1m";

    public static void main(String[] args) {
        // Inicializa o JAnsi: Ele resolve todos os problemas de acentuação (ç, ã) e emojis no Windows
        AnsiConsole.systemInstall();
        
        logger.info("Iniciando o sistema...");
        try {
            printBanner("INICIANDO CONEXÃO COM BANCO DE DADOS");
            System.out.println(ANSI_CYAN + "⏳ Aguarde, estabilizando conexão segura com PostgreSQL..." + ANSI_RESET);
            emf = Persistence.createEntityManagerFactory("eventPU");
            em = emf.createEntityManager();

            boolean rodando = true;
            while (rodando) {
                limparTela();
                System.out.println(ANSI_PURPLE + ANSI_BOLD + "╔══════════════════════════════════════════════════╗");
                System.out.println("║            ✨ SISTEMA DE EVENTOS  ✨            ║");
                System.out.println("╚══════════════════════════════════════════════════╝" + ANSI_RESET);
                System.out.println(ANSI_CYAN + "\nNavegue pelo menu abaixo escolhendo uma opção:" + ANSI_RESET);
                System.out.println();
                System.out.println("  " + ANSI_BOLD + ANSI_BLUE + "[1]" + ANSI_RESET + " 📅 Novo Cadastro de " + ANSI_YELLOW + "Evento" + ANSI_RESET);
                System.out.println("  " + ANSI_BOLD + ANSI_BLUE + "[2]" + ANSI_RESET + " 👤 Novo Cadastro de " + ANSI_GREEN + "Participante" + ANSI_RESET);
                System.out.println("  " + ANSI_BOLD + ANSI_BLUE + "[3]" + ANSI_RESET + " 🎫 Emitir " + ANSI_PURPLE + "Inscrição" + ANSI_RESET);
                System.out.println("  " + ANSI_BOLD + ANSI_BLUE + "[4]" + ANSI_RESET + " 📋 Painel de " + ANSI_YELLOW + "Eventos" + ANSI_RESET);
                System.out.println("  " + ANSI_BOLD + ANSI_BLUE + "[5]" + ANSI_RESET + " 👥 Diretório de " + ANSI_GREEN + "Participantes" + ANSI_RESET);
                System.out.println("  " + ANSI_BOLD + ANSI_RED  + "[0]" + ANSI_RESET + " ❌ " + ANSI_RED + "Sair do Sistema" + ANSI_RESET);
                System.out.println();
                System.out.print(ANSI_BOLD + ANSI_CYAN + "👉 Qual a sua escolha? " + ANSI_RESET);

                String opcao = scanner.nextLine();

                limparTela();
                switch (opcao) {
                    case "1": cadastrarEvento(); break;
                    case "2": cadastrarParticipante(); break;
                    case "3": realizarInscricao(); break;
                    case "4": listarEventos(); esperarEnter(); break;
                    case "5": listarParticipantes(); esperarEnter(); break;
                    case "0": 
                        rodando = false; 
                        System.out.println(ANSI_GREEN + ANSI_BOLD + "\n👋 Obrigado por usar o Sistema de Eventos PRO! Encerrando...\n" + ANSI_RESET);
                        break;
                    default: 
                        System.out.println(ANSI_RED + "⚠️  Opção inválida! Por favor, escolha um número válido do menu." + ANSI_RESET);
                        esperarEnter();
                }
            }
        } catch (Exception e) {
            logger.error("Erro fatal no sistema: ", e);
            System.out.println(ANSI_RED + ANSI_BOLD + "\n❌ ERRO CRÍTICO NA APLICAÇÃO: " + e.getMessage() + ANSI_RESET);
        } finally {
            if (em != null && em.isOpen()) em.close();
            if (emf != null && emf.isOpen()) emf.close();
            logger.info("Sistema encerrado.");
            // Garante a devolução correta do controle do terminal original para o Windows
            AnsiConsole.systemUninstall();
        }
    }

    private static void limparTela() {
        System.out.print("\033[H\033[2J");  
        System.out.flush();
    }

    private static void printBanner(String titulo) {
        System.out.println(ANSI_BLUE + "--------------------------------------------------------");
        System.out.println(ANSI_BOLD + "  " + titulo);
        System.out.println(ANSI_RESET + ANSI_BLUE + "--------------------------------------------------------" + ANSI_RESET);
    }

    private static void esperarEnter() {
        System.out.print(ANSI_CYAN + "\n[ Pressione ENTER para retornar ao Menu ]" + ANSI_RESET);
        scanner.nextLine();
    }

    private static void cadastrarEvento() {
        printBanner("NOVO CADASTRO: EVENTO");
        System.out.print(ANSI_WHITE + "✏️  Nome do evento.........: " + ANSI_BOLD + ANSI_YELLOW);
        String nome = scanner.nextLine();

        System.out.print(ANSI_RESET + ANSI_WHITE + "📅 Data (DD/MM/YYYY).....: " + ANSI_BOLD + ANSI_YELLOW);
        String dataStr = scanner.nextLine();
        LocalDate data;
        try {
            data = LocalDate.parse(dataStr, dateFormatter);
        } catch (DateTimeParseException e) {
            System.out.println(ANSI_RED + "\n❌ Formato de data bloqueado! Certifique-se de usar DD/MM/YYYY (Ex: 25/12/2026)." + ANSI_RESET);
            esperarEnter();
            return;
        }

        System.out.print(ANSI_RESET + ANSI_WHITE + "📍 Localização...........: " + ANSI_BOLD + ANSI_YELLOW);
        String local = scanner.nextLine();
        System.out.print(ANSI_RESET);

        Evento evento = new Evento(nome, data, local);
        salvarEntidade(evento);
    }

    private static void cadastrarParticipante() {
        printBanner("NOVO CADASTRO: PARTICIPANTE");
        System.out.print(ANSI_WHITE + "👤 Nome completo.........: " + ANSI_BOLD + ANSI_GREEN);
        String nome = scanner.nextLine();

        System.out.print(ANSI_RESET + ANSI_WHITE + "📧 Email profissional....: " + ANSI_BOLD + ANSI_GREEN);
        String email = scanner.nextLine();
        System.out.print(ANSI_RESET);

        Participante participante = new Participante(nome, email);
        salvarEntidade(participante);
    }

    private static void realizarInscricao() {
        printBanner("EMISSÃO DE NOVA INSCRIÇÃO");
        listarEventos();
        System.out.print(ANSI_BOLD + ANSI_CYAN + "\n👉 Informe o ID Numérico do " + ANSI_YELLOW + "EVENTO: " + ANSI_RESET);
        String eventoStr = scanner.nextLine();
        
        System.out.println();
        listarParticipantes();
        System.out.print(ANSI_BOLD + ANSI_CYAN + "\n👉 Informe o ID Numérico do " + ANSI_GREEN + "PARTICIPANTE: " + ANSI_RESET);
        String participanteStr = scanner.nextLine();
        System.out.print(ANSI_RESET);

        try {
            Long eventoId = Long.parseLong(eventoStr);
            Long participanteId = Long.parseLong(participanteStr);

            Evento evento = em.find(Evento.class, eventoId);
            if (evento == null) {
                System.out.println(ANSI_RED + "\n❌ Evento não encontrado no banco de dados." + ANSI_RESET);
                esperarEnter();
                return;
            }

            Participante participante = em.find(Participante.class, participanteId);
            if (participante == null) {
                System.out.println(ANSI_RED + "\n❌ Participante não cadastrado." + ANSI_RESET);
                esperarEnter();
                return;
            }

            Inscricao inscricao = new Inscricao(evento, participante);
            salvarEntidade(inscricao);
            
        } catch (NumberFormatException e) {
            System.out.println(ANSI_RED + "\n❌ Ação Bloqueada: Os IDs informados devem ser numéricos!" + ANSI_RESET);
            esperarEnter();
        }
    }

    private static void listarEventos() {
        printBanner("PAINEL DE EVENTOS DISPONÍVEIS");
        TypedQuery<Evento> query = em.createQuery("SELECT e FROM Evento e ORDER BY e.id", Evento.class);
        List<Evento> eventos = query.getResultList();
        if (eventos.isEmpty()) {
            System.out.println(ANSI_YELLOW + "   (Nenhum evento atualmente localizado nos registros)" + ANSI_RESET);
        } else {
            System.out.printf(ANSI_BOLD + ANSI_YELLOW + " %-5s | %-25s | %-12s | %s%n" + ANSI_RESET, "ID", "NOME DO EVENTO", "DATA", "LOCAL");
            System.out.println(ANSI_YELLOW + "----------------------------------------------------------------------" + ANSI_RESET);
            for (Evento e : eventos) {
                System.out.printf(ANSI_WHITE + " %-5d | %-25s | %-12s | %s%n" + ANSI_RESET, e.getId(), e.getNome(), e.getData().format(dateFormatter), e.getLocal());
            }
        }
    }

    private static void listarParticipantes() {
        printBanner("DIRETÓRIO PROFISSIONAL DE PARTICIPANTES");
        TypedQuery<Participante> query = em.createQuery("SELECT p FROM Participante p ORDER BY p.id", Participante.class);
        List<Participante> participantes = query.getResultList();
        if (participantes.isEmpty()) {
            System.out.println(ANSI_YELLOW + "   (Diretório vazio no momento)" + ANSI_RESET);
        } else {
            System.out.printf(ANSI_BOLD + ANSI_GREEN + " %-5s | %-25s | %s%n" + ANSI_RESET, "ID", "NOME REGISTRADO", "ENDEREÇO DE E-MAIL");
            System.out.println(ANSI_GREEN + "----------------------------------------------------------------------" + ANSI_RESET);
            for (Participante p : participantes) {
                System.out.printf(ANSI_WHITE + " %-5d | %-25s | %s%n" + ANSI_RESET, p.getId(), p.getNome(), p.getEmail());
            }
        }
    }

    private static void salvarEntidade(Object obj) {
        try {
            em.getTransaction().begin();
            em.persist(obj);
            em.getTransaction().commit();
            System.out.println(ANSI_GREEN + ANSI_BOLD + "\n✅ Sucesso Mestre! O registro foi imortalizado no banco de dados." + ANSI_RESET);
            logger.info("Entidade salva com sucesso: {}", obj.getClass().getSimpleName());
        } catch (ConstraintViolationException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            logger.warn("Erro de validação evitado na camada de negócio.");
            System.out.println(ANSI_RED + ANSI_BOLD + "\n⚠️  BLOCO DE SEGURANÇA: REJEIÇÃO POR ERRO DE DADOS" + ANSI_RESET);
            for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
                System.out.println(ANSI_RED + "   ► " + violation.getMessage() + ANSI_RESET);
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            
            // Tratamento de Integridade para UNIQUE constraints
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("constraint") || 
               (e.getCause() != null && e.getCause().getMessage().toLowerCase().contains("duplicate"))) {
                System.out.println(ANSI_RED + ANSI_BOLD + "\n⚠️  CONFLITO: Já existe um cadastro possuindo esse dado único (Exemplo: e-mail idêntico)." + ANSI_RESET);
            } else {
                System.out.println(ANSI_RED + "\n❌ Exceção Crítica: O banco de dados recusou a transação." + ANSI_RESET);
                logger.error("Erro inesperado durante salvamento:", e);
            }
        }
        esperarEnter();
    }
}