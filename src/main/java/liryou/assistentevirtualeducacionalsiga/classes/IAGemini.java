package liryou.assistentevirtualeducacionalsiga.classes;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;

// Import para capturar e formatar a data atual
import java.util.Locale;

// Classe que permite a conexão com o banco de dados
import java.sql.Connection;
// Classes que contém as consultas SQL utilizadas
import liryou.assistentevirtualeducacionalsiga.classes.database.consultasSQL.Mensagens;
import liryou.assistentevirtualeducacionalsiga.classes.database.consultasSQL.Unidades_Ensino;
import liryou.assistentevirtualeducacionalsiga.classes.database.consultasSQL.Cursos;
import liryou.assistentevirtualeducacionalsiga.classes.database.consultasSQL.Matrizes_Curriculares;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.TextStyle;

public class IAGemini {
    public static final HttpClient httpClient = SelfCertificatedServer.createHttpClient();
    private static final String API_KEY = "INSIRA_SUA_CHAVE_AQUI";

    private static final JSONObject DATA_ATUAL = new JSONObject();
    private static final JSONArray SYSTEM_INSTRUCTIONS = new JSONArray();
    private static final JSONArray HISTORICO_MENSAGENS_ANTERIORES = new JSONArray();
    private static final JSONArray FATEC_BD_DATA = new JSONArray();
    private static final JSONArray STUDENT_SIGA_DATA = new JSONArray();
   
    public static void capturarHistoricoMensagens(Connection connectionBD, int codigoConversa, int codigoAluno) {
        JSONArray mensagensAnteriores = Mensagens.SelectMensagens(connectionBD, codigoConversa, codigoAluno);
        
        for (int i = 0; i < mensagensAnteriores.length(); i++) {
            JSONObject mensagemAnterior = mensagensAnteriores.getJSONObject(i);
            
            String mensagem = mensagemAnterior.getString("texto_mensagem");
            int isMensagemEnviadaPorIa = mensagemAnterior.getInt("is_mensagem_enviada_por_ia");

            if (isMensagemEnviadaPorIa == 1) {
                HISTORICO_MENSAGENS_ANTERIORES.put(new JSONObject()
                    .put("texto_mensagem_anterior", mensagem)      
                    .put("is_mensagem_enviada_por_ia", true)
                );
            } else {
                HISTORICO_MENSAGENS_ANTERIORES.put(new JSONObject()
                    .put("texto_mensagem_anterior", mensagem)      
                    .put("is_mensagem_enviada_por_ia", false)
                );
            }
        }
    }   

    public static String gerarResposta(HttpServletRequest requestServlet, Connection connectionBD, int codigoConversa, int codigoAluno, String promptAluno) throws Exception {
        String respostaGerada;
        capturarDataAtual();
        organizarInstrucoesSistema();
        capturarEOrganizarInformacoesFATECPG();
        capturarEOrganizarInformacoesSIGA(requestServlet);
        
        // Personalizando o prompt com uma introdução que inclui o nome.
        JSONObject promptCompleto = new JSONObject();
        promptCompleto.put("data_atual", DATA_ATUAL);
        promptCompleto.put("instruções_sistema", SYSTEM_INSTRUCTIONS);
        promptCompleto.put("historico_mensagens_anteriores", HISTORICO_MENSAGENS_ANTERIORES);
        promptCompleto.put("informações_unidade_ensino", FATEC_BD_DATA);
        promptCompleto.put("informações_estudante", STUDENT_SIGA_DATA);
        promptCompleto.put("prompt_estudante", "Com base nas instruções e informações fornecidas, responda o seguinte prompt de forma objetiva, dizendo apenas o necessário: " + promptAluno);
        
        // Estruturando o corpo da requisição em JSON
        JSONObject data = new JSONObject();
        // "partsArray" contém o texto do prompt
        JSONArray partsArray = new JSONArray()
            .put(new JSONObject().put("text", promptCompleto.toString()));
        // "contents" inclui as partes do prompt
        data.put("contents", new JSONArray()
            .put(new JSONObject().put("parts", partsArray))
        );

        // Configuração do cliente HTTP e construção da requisição
        HttpRequest request = HttpRequest.newBuilder()
            // O modelo utilizado é o "gemini-1.5-flash-001", mas pode ser trocado pelo "gemini-1.5-flash-8b" ou "gemini-1.5-flash"
            .uri(new URI("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + API_KEY))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(data.toString()))
            .build();

        // Envio da requisição e captura da resposta
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Verificando o código de resposta HTTP
        if (response.statusCode() != 200) {
            throw new RuntimeException("Erro na requisição: " + response.statusCode() + " - " + response.body());
        } else {
            // Parse da resposta JSON
            JSONObject jsonResponse = new JSONObject(response.body());

            // Verificando se a resposta contém os "candidates"
            if (jsonResponse.has("candidates")) {
                JSONArray candidatesArray = jsonResponse.getJSONArray("candidates");

                // Verificando se há algum candidato na resposta
                if (candidatesArray.length() > 0) {
                    JSONObject candidate = candidatesArray.getJSONObject(0);

                    // Verificando se o candidato contém o campo "content"
                    if (candidate.has("content")) {
                        JSONObject content = candidate.getJSONObject("content");

                        // Verificando se o conteúdo contém as "parts" com a resposta
                        if (content.has("parts") && content.getJSONArray("parts").length() > 0) {
                            // Armazenando a resposta gerada em "respostaGerada"
                            respostaGerada = content.getJSONArray("parts").getJSONObject(0).getString("text");
                        } else {
                            throw new RuntimeException("Nenhum texto encontrado em 'content.parts'.");
                        }
                    } else {
                        throw new RuntimeException("Campo 'content' não encontrado no candidato.");
                    }
                } else {
                    throw new RuntimeException("Nenhum candidato encontrado.");
                }
            } else {
                throw new RuntimeException("Campo 'candidates' não encontrado na resposta.");
            }
        }
        
        // Registrando o prompt do aluno no banco de dados
        boolean perguntaRegistradaNoHistoricoComSucesso = Mensagens.InsertMensagem(connectionBD, codigoConversa, codigoAluno, promptAluno, 0);
        if (!perguntaRegistradaNoHistoricoComSucesso) {
            return "Falha ao registrar pergunta no banco de dados";
        }
        
        // Registrando a resposta da IAGemini no banco de dados
        boolean respostaRegistradaNoHistoricoComSucesso = Mensagens.InsertMensagem(connectionBD, codigoConversa, codigoAluno, respostaGerada, 1);
        if (respostaRegistradaNoHistoricoComSucesso) {
            return respostaGerada;
        } else {
            return "Falha ao registrar resposta no banco de dados";
        }
    }
    
    private static void organizarInstrucoesSistema() {
        // Contextualizando o que é o EduSIGA
        SYSTEM_INSTRUCTIONS.put(new JSONObject()
            .put("instrução", "Você é um assistente virtual educacional, chamado EduSIGA, integrado ao SIGA (Sistema Integrado de Gestão Acadêmica), responsável por interagir com estudantes da Fatec de Praia Grande.")
        );
        SYSTEM_INSTRUCTIONS.put(new JSONObject()
            .put("instrução", "Você foi desenvolvido pela equipe Liryou, composta por Isaque Silva Venancio, Luiza Carla Simões dos Santos e Ruan Fernandes Mendonça. Sua criação ocorreu no ano de 2024, com o objetivo de ser um trabalho acadêmico para a disciplina de Programação Orientada a Objetos, lecionada por Ricardo Pupo Larguesa.")
        );
        // Como responder as requisições
        SYSTEM_INSTRUCTIONS.put(new JSONObject()
            .put("instrução", "Lembre-se de sempre responder de forma compreensiva e objetiva. Além disso, sempre gere respostas baseadas unicamente nas informações do estudante que você tem acesso.")
        );
        // Como responder determinadas requisições
        // (qual o plano de ensino)
        SYSTEM_INSTRUCTIONS.put(new JSONObject()
            .put("instrução", "Se o aluno perguntar qual o seu plano de ensino, o sistema exibir apenas o nome e sigla das matérias. Exiba as demais informações do plano de ensino apenas se o estudante solicitar.")
        );
        // (matérias restantes até o fim do curso)
        SYSTEM_INSTRUCTIONS.put(new JSONObject()
            .put("instrução", "Se o aluno perguntar quantas e/ou quais matérias faltam para concluir seu curso, o sistema deve acessar a matriz curricular do curso e verificar quais matérias já foram concluídas, quais estão em andamento e quais ainda não foram iniciadas pelo aluno. Em seguida, o sistema deve exibir ao aluno apenas as matérias que ainda não foram iniciadas, garantindo que essas pertencem à matriz curricular do curso.")
        );
        // (quantidade de faltas possíveis em cada matéria)
        SYSTEM_INSTRUCTIONS.put(new JSONObject()
            .put("instrução", "Se o aluno perguntar quantas faltas ele pode ter em cada matéria, o sistema deve acessar o plano de ensino e exibir ao aluno o limite de faltas e também a quantidade de faltas atuais de cada matéria.")
        );
        // Capturando matérias de dias futuros
        SYSTEM_INSTRUCTIONS.put(new JSONObject()
            .put("instrução",
                "Caso o usuário solicite ou questione algo como: 'Que aulas eu terei daqui a X dias', 'Quais são as aulas que vou ter daqui a X dias', 'Que disciplinas vou cursar daqui a X dias', 'Quais matérias terei daqui X dias', 'Daqui a X dias, quais são as aulas?', 'O que vou estudar daqui X dias?', ou expressões similares relacionadas a aulas futuras, o sistema deve: " +
                "1. Identificar a quantidade de dias informada pelo usuário (X). " +
                "2. Somar esta quantidade de dias (X) à data atual. " +
                "3. Determinar o dia da semana correspondente à data calculada. " +
                "4. Com base no dia da semana identificado, consultar o plano de ensino do usuário para recuperar as matérias programadas para aquele dia. " +
                "5. Exibir ao usuário as matérias identificadas, evitando qualquer suposição ou generalização."
            )
        );
        // Capturando matérias de dias passados
        SYSTEM_INSTRUCTIONS.put(new JSONObject()
            .put("instrução",
                "Caso o usuário solicite ou questione algo como: 'Que aulas eu tive X dias atrás', 'Quais aulas eu tive X dias atrás', 'Que disciplinas eu tive X dias atrás', 'Quais matérias estudei há X dias', 'Há X dias, quais aulas eu tive?', ou expressões similares relacionadas a aulas passadas, o sistema deve: " +
                "1. Identificar a quantidade de dias informada pelo usuário (X). " +
                "2. Subtrair esta quantidade de dias (X) da data atual. " +
                "3. Determinar o dia da semana correspondente à data calculada. " +
                "4. Com base no dia da semana identificado, consultar o plano de ensino do usuário para recuperar as matérias programadas para aquele dia. " +
                "5. Exibir ao usuário as matérias identificadas, evitando qualquer suposição ou generalização."
            )
        );
        // Compreendendo semestres
        SYSTEM_INSTRUCTIONS.put(new JSONObject()
            .put("instrução",
                "O ano letivo é dividido em 2 semestres: 'AAAA1' (janeiro a junho) e 'AAAA2' (julho a dezembro). " +
                "1. Identifique o semestre anterior, atual ou posterior com base na data atual: subtraia ou incremente o semestre ('S') e ajuste o ano ('AAAA') conforme necessário. " +
                "2. Use o plano de ensino para informações do semestre atual. " +
                "3. Use o histórico acadêmico para semestres passados. " +
                "4. Use a matriz curricular para semestres futuros."
            )
        );
        // Formatação de respostas
        SYSTEM_INSTRUCTIONS.put(new JSONObject()
            .put("instrução", "Formate todas as suas respostas em HTML para que possam ser exibidas diretamente em uma página da web. Siga as seguintes regras de formatação: Use tags básicas para o conteúdo, como listas não ordenadas (<ul>) e inclua cada item dentro de <li>; Utilize <b> apenas quando for realmente necessário para destacar partes importantes; Use parágrafos com <p> para qualquer outro texto; Garanta que o conteúdo esteja bem estruturado e pronto para ser exibido em um website. Não utilize tags como <h1>, <h2>, <h3>, <h4> ou qualquer outro recurso de formatação que não seja essencial.")
        );
    }
    
    private static void capturarEOrganizarInformacoesFATECPG() {
        Connection connectionBD = null;
        JSONObject unidadeEnsinoFatecPG = new JSONObject();
        JSONArray cursosFatecPG = new JSONArray();
        JSONArray matrizesCurricularesFatecPG = new JSONArray();
            
        try {
            connectionBD = DriverBD.getConnection();
            
            unidadeEnsinoFatecPG = Unidades_Ensino.SelectUnidadeEnsino(connectionBD, "Faculdade de Tecnologia de Praia Grande");
            cursosFatecPG = Cursos.SelectCursos(connectionBD, 1);
            matrizesCurricularesFatecPG.put(new JSONObject()
                .put("matriz_curricular_ads", Matrizes_Curriculares.SelectMatrizesCurriculares(connectionBD, 1))
                .put("matriz_curricular_comex", Matrizes_Curriculares.SelectMatrizesCurriculares(connectionBD, 2))
                .put("matriz_curricular_dsm", Matrizes_Curriculares.SelectMatrizesCurriculares(connectionBD, 3))
                .put("matriz_curricular_ge", Matrizes_Curriculares.SelectMatrizesCurriculares(connectionBD, 4))
                .put("matriz_curricular_pq", Matrizes_Curriculares.SelectMatrizesCurriculares(connectionBD, 5))
            );
            
            String nomeUnidadeEnsino = unidadeEnsinoFatecPG.getString("nome_unidade_ensino");
            String enderecoUnidadeEnsino = unidadeEnsinoFatecPG.getString("endereco_unidade_ensino");
            String descricaoUnidadeEnsino = unidadeEnsinoFatecPG.getString("descricao_unidade_ensino");
            
            FATEC_BD_DATA.put(new JSONObject()
                .put("nome_unidade_ensino", nomeUnidadeEnsino)
                .put("endereco_unidade_ensino", enderecoUnidadeEnsino)
                .put("descricao_unidade_ensino", descricaoUnidadeEnsino)
            );
            
            for (int i = 0; i < cursosFatecPG.length(); i++) {
                FATEC_BD_DATA.put(new JSONObject()
                    .put("informações_curso", cursosFatecPG.get(i))
                    .put("matriz_curricular_curso", matrizesCurricularesFatecPG.get(i))
                );
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            // Fechar a conexão com o banco de dados
            DriverBD.closeConnection(connectionBD);
        }
    }
    
    private static void capturarEOrganizarInformacoesSIGA(HttpServletRequest requestServlet) {
        HttpSession session = requestServlet.getSession();
        
        JSONObject estudante = (JSONObject) session.getAttribute("estudante");
        JSONArray avisos = (JSONArray) session.getAttribute("avisos");
        JSONArray planoDeEnsino = (JSONArray) session.getAttribute("planoDeEnsino");
        JSONArray historicoCompleto = (JSONArray) session.getAttribute("historicoCompleto");
        JSONArray horariosPlanoDeEnsino = (JSONArray) session.getAttribute("horariosPlanoDeEnsino");

        STUDENT_SIGA_DATA.put(new JSONObject()
            .put("informações_estudante", estudante)
            .put("informações_avisos", avisos)
            .put("informações_plano_de_ensino", planoDeEnsino)
            .put("informações_horarios_plano_de_ensino", horariosPlanoDeEnsino)
            .put("informações_historico_completo", historicoCompleto)
        );
    }
    
    private static void capturarDataAtual() {
        // Data atual
        LocalDate dataAtual = LocalDate.now();
        Locale locale = new Locale("pt", "BR");

        // Obtendo o dia da semana (Segunda-feira, Terça-feira...)
        String diaDaSemana = dataAtual.getDayOfWeek().getDisplayName(TextStyle.FULL, locale);
        
        DATA_ATUAL.put("data", dataAtual.toString()); // "AAAA-MM-DD"
        DATA_ATUAL.put("dia_da_semana", diaDaSemana);
        DATA_ATUAL.put("fuso_horario", "America/Sao_Paulo");
    }
}