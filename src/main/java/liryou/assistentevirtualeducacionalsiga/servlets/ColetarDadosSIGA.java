package liryou.assistentevirtualeducacionalsiga.servlets;
 
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse; 
// Permite gerenciar sessões
import jakarta.servlet.http.HttpSession;

// Classes do Selenium para acessar e interagir com o SIGA
// Permite localizar elementos web
import org.openqa.selenium.By;
// Permite interagir com elementos web
import org.openqa.selenium.WebElement;
// Permite simular um navegador web headless
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
// Classe responsável por gerenciar a criação e encerramento do navegador
import liryou.assistentevirtualeducacionalsiga.classes.DriverNavegador;

// Permite a manipulação de listas
import java.util.ArrayList;
// Permite a criação de listas de diferentes tipos
import java.util.List;

// Permite a manipulação de Arrays e Objetos JSON
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/** @author Liryou */
@WebServlet(name = "ColetarDadosSIGA", urlPatterns = {"/ColetarDadosSIGA"})
public class ColetarDadosSIGA extends HttpServlet {
    private static String mensagemResultadoColetaDadosSIGA = "Sucesso coleta de dados";  
    
    // Criando o JSON que armazena as informações individuais do estudante
    static JSONObject estudante = new JSONObject();
    static JSONArray avisos = new JSONArray();    
    static JSONArray planoDeEnsino = new JSONArray();
    static JSONArray horariosPlanoDeEnsino = new JSONArray();
    static JSONArray notasParciaisPlanoDeEnsino = new JSONArray();
    static JSONArray historicoCompleto = new JSONArray();

    // Informações individuais
    static String nomeEstudante;
    static String usuarioSIGA;
    static String registroAcademico;
    static int semestreAtual;
    static float percentualDeProgressao;
    static float percentualDeProgressaoIntercambio;
    static float percentualDeRendimento;
    static int quantidadeSemestresCursados;
    static String emailInstitucional;
    static String nomeCurso;
    static String situacaoCurso;
    static String periodoCurso;
    // Informações em listas
    // Plano de Ensino
    static List<String> listaNomesDisciplinasPlanoDeEnsino = new ArrayList<>();
    static List<String> listaSiglasDisciplinasPlanoDeEnsino = new ArrayList<>();
    static List<String> listaNomesProfessoresDisciplinasPlanoDeEnsino = new ArrayList<>();
    static List<String> listaEmentasDisciplinasPlanoDeEnsino = new ArrayList<>();
    static List<String> listaObjetivosDisciplinasPlanoDeEnsino = new ArrayList<>();
    static List<Integer> listaQuantidadesAulasSemanaisDisciplinasPlanoDeEnsino = new ArrayList<>();
    static List<Integer> listaQuantidadesAulasTeoricasDisciplinasPlanoDeEnsino = new ArrayList<>();
    static List<Integer> listaQuantidadesAulasPraticasDisciplinasPlanoDeEnsino = new ArrayList<>();
    static List<Integer> listaQuantidadesAulasTotaisDisciplinasPlanoDeEnsino = new ArrayList<>();
    static List<String> listaQuantidadesFaltasPossiveisPlanoDeEnsino = new ArrayList<>();
    static List<Integer> listaQuantidadePresencasParciaisPlanoDeEnsino = new ArrayList<>();
    static List<Integer> listaQuantidadeFaltasParciaisPlanoDeEnsino = new ArrayList<>();
    // Histórico completo
    static List<String> listaSiglasDisciplinasHistoricoCompleto = new ArrayList<>();
    static List<String> listaNomesDisciplinasHistoricoCompleto = new ArrayList<>();
    static List<String> listaPeriodosDisciplinasHistoricoCompleto = new ArrayList<>();
    static List<String> listaSituacoesDisciplinasHistoricoCompleto = new ArrayList<>();
    static List<String> listaMediasFinaisDisciplinasHistoricoCompleto = new ArrayList<>();
    static List<String> listaFrequenciasDisciplinasHistoricoCompleto = new ArrayList<>();
    static List<Integer> listaQuantidadesFaltasDisciplinasHistoricoCompleto = new ArrayList<>();
    static List<String> listaObservacoesDisciplinasHistoricoCompleto = new ArrayList<>();
    static List<String> listaHorarios = new ArrayList<>();
   
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Instanciando a classe Driver que gerencia o HtmlUnitDriver
        DriverNavegador driverManager = new DriverNavegador();
        HtmlUnitDriver driverNavegador = driverManager.getDriver();
        
        try {
            if (request.getParameter("usuarioSIGA") != null && request.getParameter("senhaSIGA") != null) { 
                usuarioSIGA = request.getParameter("usuarioSIGA");
                String senhaSIGA = request.getParameter("senhaSIGA");
                
                // Realiza o login no sistema SIGA utilizando os dados fornecidos
                realizarLoginSIGA(driverNavegador, usuarioSIGA, senhaSIGA);
                
                if (mensagemResultadoColetaDadosSIGA.equals("Sucesso coleta de dados")) {
                    // Saindo do SIGA
                    WebElement elementoInputSairDoSIGA = driverNavegador.findElement(By.cssSelector("#MPW0041IMAGESAIR"));
                    elementoInputSairDoSIGA.click();
                    
                    atribuirValoresJSON();
                    criarSessionUsuarioLogado(request);
                }
            } else {
                mensagemResultadoColetaDadosSIGA = "Falha ao receber parâmetros (usuário, senha)";
            }
        } catch (Exception e) {
            mensagemResultadoColetaDadosSIGA = e.getMessage();
        } finally {
            driverManager.quitDriver();
        }
        
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        // Retorna o resultado da operação para o cliente
        response.getWriter().print(mensagemResultadoColetaDadosSIGA);
    }
    
    private static void realizarLoginSIGA(HtmlUnitDriver driverNavegador, String usuarioSIGA, String senhaSIGA) throws InterruptedException {        
        // Acessando a página de login do SIGA
        driverNavegador.get("https://siga.cps.sp.gov.br/aluno/login.aspx");
        Thread.sleep(3000);
        
        if (driverNavegador.getTitle().equals("cannot open database \"ASPState\" requested by the login. The login failed.<br>Login failed for user 'aspnet'.")) {
            mensagemResultadoColetaDadosSIGA = "O SIGA está temporariamente fora do ar. Por favor, aguarde e tente novamente mais tarde.";
        } else {            
            // Localizando os elementos do formulário de login na página
            WebElement elementoLoginFormSigaInputUsuario = driverNavegador.findElement(By.cssSelector("#vSIS_USUARIOID"));
            WebElement elementoLoginFormSigaInputSenha = driverNavegador.findElement(By.cssSelector("#vSIS_USUARIOSENHA"));
            WebElement elementoLoginFormSigaSubmitButton = driverNavegador.findElement(By.name("BTCONFIRMA"));

            // Preenchendo o formulário com os dados fornecidos e submetendo-o
            elementoLoginFormSigaInputUsuario.sendKeys(usuarioSIGA);
            elementoLoginFormSigaInputSenha.sendKeys(senhaSIGA);
            elementoLoginFormSigaSubmitButton.click();

            esperarCarregamentoPagina(driverNavegador, "home");
            // Capturando o título da página atual
            
            // Se o título da página for igual a "home" significa que o login foi um sucesso
            if (driverNavegador.getTitle().equals("home")) {
                coletarDadosPaginaHome(driverNavegador);
                coletarDadosPaginaHistoricoCompleto(driverNavegador);
                coletarDadosPaginaHorario(driverNavegador);
                coletarDadosPaginaNotasParciais(driverNavegador);
                coletarDadosPaginaFaltasParciais(driverNavegador);
            } else {
                mensagemResultadoColetaDadosSIGA = "Falha ao realizar login - " + driverNavegador.getTitle();
            }
        }
    }
    
    private static void coletarDadosPaginaHome(HtmlUnitDriver driverNavegador) throws InterruptedException {
        coletarValoresIndividuais(driverNavegador);
        coletarValoresPlanoDeEnsino(driverNavegador);
        coletarValoresAvisos(driverNavegador);
    }
 
    private static void coletarDadosPaginaHistoricoCompleto(HtmlUnitDriver driverNavegador) throws InterruptedException {
        // Acessando a página de Histórico completo
        driverNavegador.get("https://siga.cps.sp.gov.br/aluno/historicocompleto.aspx");
       
        esperarCarregamentoPagina(driverNavegador, "Historico completo");
        
        if (driverNavegador.getTitle().equals("Historico completo")) {
            coletarValoresHistoricoCompleto(driverNavegador);
        } else {
            mensagemResultadoColetaDadosSIGA = "Tempo limite excedido - Historico completo";
        }
    }
 
    private static void coletarDadosPaginaHorario(HtmlUnitDriver driverNavegador) throws InterruptedException {
        // Acessando a página de Horário
        driverNavegador.get("https://siga.cps.sp.gov.br/aluno/horario.aspx");
       
        esperarCarregamentoPagina(driverNavegador, "Horário");
        
        if (driverNavegador.getTitle().equals("Horário")) {
            coletarValoresHorarios(driverNavegador);
        } else {
            mensagemResultadoColetaDadosSIGA = "Tempo limite excedido - Horário";
        }
    }
 
    private static void coletarDadosPaginaNotasParciais(HtmlUnitDriver driverNavegador) throws InterruptedException {
        // Acessando a página de Notas parciais
        driverNavegador.get("https://siga.cps.sp.gov.br/aluno/notasparciais.aspx");
       
        esperarCarregamentoPagina(driverNavegador, "notasparciais do estudante");
        
        if (driverNavegador.getTitle().equals("notasparciais do estudante")) {
            coletarValoresNotasParciais(driverNavegador);
        } else {
            mensagemResultadoColetaDadosSIGA = "Tempo limite excedido - Notas parciais";
        }
    }
 
    private static void coletarDadosPaginaFaltasParciais(HtmlUnitDriver driverNavegador) throws InterruptedException {
        // Acessando a página de Faltas parciais
        driverNavegador.get("https://siga.cps.sp.gov.br/aluno/faltasparciais.aspx");
 
        esperarCarregamentoPagina(driverNavegador, "Faltas parciais do estudante");
        
        if (driverNavegador.getTitle().equals("Faltas parciais do estudante")) {
            coletarValoresPresencasEFaltasParciaisPlanoDeEnsino(driverNavegador);
        } else {
            mensagemResultadoColetaDadosSIGA = "Tempo limite excedido - Faltas parciais";
        }
    }
 
    private static void coletarValoresIndividuais(HtmlUnitDriver driverNavegador) {
        // Encontrando os elementos que possuem as informações do aluno
        WebElement elementoNomeEstudante = driverNavegador.findElement(By.cssSelector("#span_MPW0041vPRO_PESSOALNOME"));
        WebElement elementoRegistroAcademico = driverNavegador.findElement(By.cssSelector("#span_MPW0041vACD_ALUNOCURSOREGISTROACADEMICOCURSO"));
        WebElement elementoSemestreAtual = driverNavegador.findElement(By.cssSelector("#span_MPW0041vACD_ALUNOCURSOCICLOATUAL"));
        WebElement elementoPercentualDeProgressao = driverNavegador.findElement(By.cssSelector("#span_MPW0041vACD_ALUNOCURSOINDICEPP"));
        WebElement elementoPercentualDeProgressaoIntercambio = driverNavegador.findElement(By.cssSelector("#span_MPW0041vACD_ALUNOCURSOINDICEPPARINTER"));
        WebElement elementoPercentualDeRendimento = driverNavegador.findElement(By.cssSelector("#span_MPW0041vACD_ALUNOCURSOINDICEPR"));
        WebElement elementoQuantidadeSemestresCursados = driverNavegador.findElement(By.cssSelector("#span_MPW0041vSEMESTRESCURSADOS"));
        WebElement elementoEmailInstitucional = driverNavegador.findElement(By.cssSelector("#span_MPW0041vINSTITUCIONALFATEC"));
        WebElement elementoNomeCurso = driverNavegador.findElement(By.cssSelector("#span_vACD_CURSONOME_MPAGE"));
        WebElement elementoSituacaoCurso = driverNavegador.findElement(By.cssSelector("#span_vSITUACAO_MPAGE"));
        WebElement elementoPeriodoCurso = driverNavegador.findElement(By.cssSelector("#span_vACD_PERIODODESCRICAO_MPAGE"));
        
        // Capturando os valores dos elementos e os armazenando em variáveis
        nomeEstudante = verificarElementoVazio(elementoNomeEstudante);
        if (!nomeEstudante.equals("Valor não encontrado")) {
            // Corrigindo o nome do aluno, que sempre vem com "-" no final
            nomeEstudante = nomeEstudante.substring(0, nomeEstudante.length() - 1);
            nomeEstudante = formatarNome(nomeEstudante);
        }
        registroAcademico = verificarElementoVazio(elementoRegistroAcademico);
        semestreAtual = Integer.parseInt(verificarElementoVazio(elementoSemestreAtual));
        percentualDeProgressao = Float.parseFloat(verificarElementoVazio(elementoPercentualDeProgressao));
        // O valor costuma vir com ",", ao invés de "."
        percentualDeProgressaoIntercambio = Float.parseFloat(verificarElementoVazio(elementoPercentualDeProgressaoIntercambio).replace(",", "."));
        percentualDeRendimento = Float.parseFloat(verificarElementoVazio(elementoPercentualDeRendimento));
        quantidadeSemestresCursados = Integer.parseInt(verificarElementoVazio(elementoQuantidadeSemestresCursados));
        emailInstitucional = verificarElementoVazio(elementoEmailInstitucional);
        nomeCurso = verificarElementoVazio(elementoNomeCurso);
        situacaoCurso = verificarElementoVazio(elementoSituacaoCurso);
        periodoCurso = verificarElementoVazio(elementoPeriodoCurso);     
    }

    private static void coletarValoresPlanoDeEnsino(HtmlUnitDriver driverNavegador) throws InterruptedException {
        // Coletando todos os elementos <a> que contêm as matérias do plano de ensino
        List<WebElement> elementosLinksMateriasPlanoDeEnsino = driverNavegador.findElements(By.cssSelector("#ygtvc25 a"));

        for (int i = 0; i < elementosLinksMateriasPlanoDeEnsino.size(); i++) {
            // Obtém o link da matéria atual
            WebElement elementoLinkMateriaPlanoDeEnsino = elementosLinksMateriasPlanoDeEnsino.get(i);
            String hrefLinkMateriaPlanoDeEnsino = elementoLinkMateriaPlanoDeEnsino.getAttribute("href");

            // Acessa o link da matéria atual
            driverNavegador.get(hrefLinkMateriaPlanoDeEnsino);

            // Capturando o nome da matéria atual
            WebElement elementoSpanNomeDisciplinaPlanoDeEnsino = driverNavegador.findElement(By.cssSelector("#span_W0005vACD_DISCIPLINANOME"));
            String nomeDisciplinaPlanoDeEnsino = verificarElementoVazio(elementoSpanNomeDisciplinaPlanoDeEnsino);
            listaNomesDisciplinasPlanoDeEnsino.add(nomeDisciplinaPlanoDeEnsino);
            
            // Capturando a sigla da matéria atual
            WebElement elementoSpanSiglaDisciplinaPlanoDeEnsino = driverNavegador.findElement(By.cssSelector("#span_W0005vSHOW_ACD_DISCIPLINASIGLA"));
            String siglaDisciplinaPlanoDeEnsino = verificarElementoVazio(elementoSpanSiglaDisciplinaPlanoDeEnsino);
            listaSiglasDisciplinasPlanoDeEnsino.add(siglaDisciplinaPlanoDeEnsino);
            
            // Capturando o nome do professor da matéria atual
            WebElement elementoSpanNomeProfessorDisciplinaPlanoDeEnsino = driverNavegador.findElement(By.cssSelector("#span_W0005vPRO_PESSOALNOME"));
            String nomeProfessorDisciplinaPlanoDeEnsino = verificarElementoVazio(elementoSpanNomeProfessorDisciplinaPlanoDeEnsino);
            listaNomesProfessoresDisciplinasPlanoDeEnsino.add(nomeProfessorDisciplinaPlanoDeEnsino);
            
            // Capturando a ementa da matéria atual
            WebElement elementoSpanEmentaDisciplinaPlanoDeEnsino = driverNavegador.findElement(By.cssSelector("#span_W0008W0013vACD_DISCIPLINAEMENTA"));
            String ementaDisciplinaPlanoDeEnsino = verificarElementoVazio(elementoSpanEmentaDisciplinaPlanoDeEnsino);
            listaEmentasDisciplinasPlanoDeEnsino.add(ementaDisciplinaPlanoDeEnsino);
            
            // Capturando o objetivo da matéria atual
            WebElement elementoSpanObjetivoDisciplinaPlanoDeEnsino = driverNavegador.findElement(By.cssSelector("#span_W0008W0013vACD_DISCIPLINAOBJETIVO"));
            String objetivoDisciplinaPlanoDeEnsino = verificarElementoVazio(elementoSpanObjetivoDisciplinaPlanoDeEnsino);
            listaObjetivosDisciplinasPlanoDeEnsino.add(objetivoDisciplinaPlanoDeEnsino);
            
            // Capturando a quantidade de aulas semanais da matéria atual
            WebElement elementoSpanQuantidadeAulasSemanaisDisciplinaPlanoDeEnsino = driverNavegador.findElement(By.cssSelector("#span_W0008W0013vACD_DISCIPLINAAULASSEMANAIS"));
            int quantidadeAulasSemanaisDisciplinaPlanoDeEnsino = Integer.parseInt(verificarElementoVazio(elementoSpanQuantidadeAulasSemanaisDisciplinaPlanoDeEnsino));
            listaQuantidadesAulasSemanaisDisciplinasPlanoDeEnsino.add(quantidadeAulasSemanaisDisciplinaPlanoDeEnsino);
            
            // Capturando a quantidade de aulas teóricas da matéria atual
            WebElement elementoSpanQuantidadeAulasTeoricasDisciplinaPlanoDeEnsino = driverNavegador.findElement(By.cssSelector("#span_W0008W0013vACD_DISCIPLINAAULASTEORICAS"));
            int quantidadeAulasTeoricasDisciplinaPlanoDeEnsino = Integer.parseInt(verificarElementoVazio(elementoSpanQuantidadeAulasTeoricasDisciplinaPlanoDeEnsino));
            listaQuantidadesAulasTeoricasDisciplinasPlanoDeEnsino.add(quantidadeAulasTeoricasDisciplinaPlanoDeEnsino);
            
            // Capturando a quantidade de aulas práticas da matéria atual
            WebElement elementoSpanQuantidadeAulasPraticasDisciplinaPlanoDeEnsino = driverNavegador.findElement(By.cssSelector("#span_W0008W0013vACD_DISCIPLINAAULASPRATICAS"));
            int quantidadeAulasPraticasDisciplinaPlanoDeEnsino = Integer.parseInt(verificarElementoVazio(elementoSpanQuantidadeAulasPraticasDisciplinaPlanoDeEnsino));
            listaQuantidadesAulasPraticasDisciplinasPlanoDeEnsino.add(quantidadeAulasPraticasDisciplinaPlanoDeEnsino);
            
            // Capturando a quantidade total de aulas da matéria atual
            WebElement elementoSpanQuantidadeAulasTotaisDisciplinaPlanoDeEnsino = driverNavegador.findElement(By.cssSelector("#span_W0008W0013vACD_DISCIPLINAAULASTOTAISPERIODO"));
            int quantidadeAulasTotaisDisciplinaPlanoDeEnsino = Integer.parseInt(verificarElementoVazio(elementoSpanQuantidadeAulasTotaisDisciplinaPlanoDeEnsino));
            listaQuantidadesAulasTotaisDisciplinasPlanoDeEnsino.add(quantidadeAulasTotaisDisciplinaPlanoDeEnsino);
            
            // O aluno pode ter até 1/4 do total de aulas como faltas (80 = 20; 40 = 10)
            if (quantidadeAulasTotaisDisciplinaPlanoDeEnsino == 80) {
                listaQuantidadesFaltasPossiveisPlanoDeEnsino.add("20");
            } else if (quantidadeAulasTotaisDisciplinaPlanoDeEnsino == 40) {
                listaQuantidadesFaltasPossiveisPlanoDeEnsino.add("10");
            }
            // Casos de matérias como estágio e tcc
            else {
                listaQuantidadesFaltasPossiveisPlanoDeEnsino.add("Esta matéria não possui aulas, por ser a disciplina de estágio, ou TG.");
            }
            
            // Volta para a página original
            driverNavegador.navigate().back();
            // Atualiza a lista para que não fique obsoleta após voltar à página
            elementosLinksMateriasPlanoDeEnsino = driverNavegador.findElements(By.cssSelector("#ygtvc25 a"));
        }     
    }
 
    private static void coletarValoresAvisos(HtmlUnitDriver driverNavegador) {
        // Coletando todos os elementos <p> que contêm os avisos
        List<WebElement> elementosParagrafosAvisos = driverNavegador.findElements(By.cssSelector("#span_vTEXTO p"));

        for (WebElement elementoParagrafoAviso : elementosParagrafosAvisos) {
            String aviso = verificarElementoVazio(elementoParagrafoAviso);

            if (!aviso.equals("Valor não encontrado") && !aviso.contains("_") && !aviso.equals(" ")) {
                // Adicionando o texto de cada <p> (aviso) à lista
                avisos.put(new JSONObject().put("aviso", aviso));
            }
        }
    }

    private static void coletarValoresHistoricoCompleto(HtmlUnitDriver driverNavegador) {
        // Coletando todos os elementos <span> que contêm as siglas das matérias presentes no histórico completo
        List<WebElement> elementosSpansSiglasDisciplinasHistoricoCompleto = driverNavegador.findElements(By.cssSelector("#Grid1ContainerTbl tr:not(:nth-child(1)) td:nth-child(1) span"));
        // Coletando todos os elementos <span> que contêm os nomes das matérias presentes no histórico completo
        List<WebElement> elementosSpansNomesDisciplinasHistoricoCompleto = driverNavegador.findElements(By.cssSelector("#Grid1ContainerTbl tr:not(:nth-child(1)) td:nth-child(2) span"));
        List<WebElement> elementosSpansPeriodosDisciplinasHistoricoCompleto = driverNavegador.findElements(By.cssSelector("#Grid1ContainerTbl tr:not(:nth-child(1)) td:nth-child(3) span"));
        List<WebElement> elementosImgsSituacoesDisciplinasHistoricoCompleto = driverNavegador.findElements(By.cssSelector("#Grid1ContainerTbl tr:not(:nth-child(1)) td:nth-child(4) img"));
        List<WebElement> elementosSpansMediasFinaisDisciplinasHistoricoCompleto = driverNavegador.findElements(By.cssSelector("#Grid1ContainerTbl tr:not(:nth-child(1)) td:nth-child(5) span"));
        List<WebElement> elementosSpansFrequenciasDisciplinasHistoricoCompleto = driverNavegador.findElements(By.cssSelector("#Grid1ContainerTbl tr:not(:nth-child(1)) td:nth-child(6) span"));
        List<WebElement> elementosSpansQuantidadesFaltasDisciplinasHistoricoCompleto = driverNavegador.findElements(By.cssSelector("#Grid1ContainerTbl tr:not(:nth-child(1)) td:nth-child(7) span"));
        List<WebElement> elementosSpansObservacoesDisciplinasHistoricoCompleto = driverNavegador.findElements(By.cssSelector("#Grid1ContainerTbl tr:not(:nth-child(1)) td:nth-child(8) span"));

        for (int i = 0; i < elementosSpansSiglasDisciplinasHistoricoCompleto.size(); i++) {
            // Capturando a sigla da matéria atual presente no histórico completo
            WebElement elementoSpanSiglaDisciplinaHistoricoCompleto = elementosSpansSiglasDisciplinasHistoricoCompleto.get(i);
            String siglaDisciplinaHistoricoCompleto = verificarElementoVazio(elementoSpanSiglaDisciplinaHistoricoCompleto);
            listaSiglasDisciplinasHistoricoCompleto.add(siglaDisciplinaHistoricoCompleto);
            
            // Capturando o nome da matéria atual presente no histórico completo
            WebElement elementoSpanNomeDisciplinaHistoricoCompleto = elementosSpansNomesDisciplinasHistoricoCompleto.get(i);
            String nomeDisciplinaHistoricoCompleto = verificarElementoVazio(elementoSpanNomeDisciplinaHistoricoCompleto);
            listaNomesDisciplinasHistoricoCompleto.add(nomeDisciplinaHistoricoCompleto);
            
            // Capturando o período da matéria atual presente no histórico completo
            WebElement elementoSpanPeriodoDisciplinaHistoricoCompleto = elementosSpansPeriodosDisciplinasHistoricoCompleto.get(i);
            String periodoDisciplinaHistoricoCompleto = verificarElementoVazio(elementoSpanPeriodoDisciplinaHistoricoCompleto);
            listaPeriodosDisciplinasHistoricoCompleto.add(periodoDisciplinaHistoricoCompleto);
            
            // Capturando a situação da matéria atual presente no histórico completo, que esta no formato de imagem
            WebElement elementoImgSituacaoDisciplinaHistoricoCompleto = elementosImgsSituacoesDisciplinasHistoricoCompleto.get(i);
            String srcSituacaoDisciplinaHistoricoCompleto = elementoImgSituacaoDisciplinaHistoricoCompleto.getAttribute("src");
            // Verificando a imagem para determinar a situação da disciplina
            // Adicionando a situação de cada matéria (aprovado ou não) à lista
            if (srcSituacaoDisciplinaHistoricoCompleto.contains("checkTrue.png")) {
                listaSituacoesDisciplinasHistoricoCompleto.add("Aprovado");
            } else {
                listaSituacoesDisciplinasHistoricoCompleto.add("Não aprovado");
            }

            // Capturando a média final da matéria atual presente no histórico completo
            WebElement elementoSpanMediaFinalDisciplinaHistoricoCompleto = elementosSpansMediasFinaisDisciplinasHistoricoCompleto.get(i);
            String mediaFinalDisciplinaHistoricoCompleto = verificarElementoVazio(elementoSpanMediaFinalDisciplinaHistoricoCompleto);
            
            if (!mediaFinalDisciplinaHistoricoCompleto.equals("Valor não encontrado")) {
                if (mediaFinalDisciplinaHistoricoCompleto.equals("--")) {
                    // Adicionando uma mensagem ao invés do texto do <span>
                    listaMediasFinaisDisciplinasHistoricoCompleto.add("A média final desta matéria não está disponível porque esta disciplina foi cursada pelo estudante em uma unidade de ensino diferente da atual");
                } else {
                    // Adicionando o texto do <span>
                    listaMediasFinaisDisciplinasHistoricoCompleto.add(mediaFinalDisciplinaHistoricoCompleto);
                }
            }

            // Capturando a frequência da matéria atual presente no histórico completo
            WebElement elementoSpanFrequenciaDisciplinaHistoricoCompleto = elementosSpansFrequenciasDisciplinasHistoricoCompleto.get(i);
            String frequenciaDisciplinaHistoricoCompleto = verificarElementoVazio(elementoSpanFrequenciaDisciplinaHistoricoCompleto);

            if (!frequenciaDisciplinaHistoricoCompleto.equals("Valor não encontrado")) {
                if (frequenciaDisciplinaHistoricoCompleto.equals("--")) {
                    // Adicionando uma mensagem ao invés do texto do <span>
                    listaFrequenciasDisciplinasHistoricoCompleto.add("A frequência desta matéria não está disponível porque esta disciplina ainda está sendo cursada pelo estudante");
                } else {
                    // Adicionando o texto do <span>
                    listaFrequenciasDisciplinasHistoricoCompleto.add(frequenciaDisciplinaHistoricoCompleto);
                }
            }

            // Capturando a quantidade de faltas da matéria atual presente no histórico completo
            WebElement elementoSpanQuantidadeFaltasDisciplinaHistoricoCompleto = elementosSpansQuantidadesFaltasDisciplinasHistoricoCompleto.get(i);
            int quantidadeFaltasDisciplinaHistoricoCompleto = Integer.parseInt(verificarElementoVazio(elementoSpanQuantidadeFaltasDisciplinaHistoricoCompleto));
            listaQuantidadesFaltasDisciplinasHistoricoCompleto.add(quantidadeFaltasDisciplinaHistoricoCompleto);

            // Capturando a observação da matéria atual presente no histórico completo
            WebElement elementoSpanObservacaoDisciplinaHistoricoCompleto = elementosSpansObservacoesDisciplinasHistoricoCompleto.get(i);
            String observacaoDisciplinaHistoricoCompleto = verificarElementoVazio(elementoSpanObservacaoDisciplinaHistoricoCompleto);
            listaObservacoesDisciplinasHistoricoCompleto.add(observacaoDisciplinaHistoricoCompleto);
        }
    }
    
    private static void coletarValoresHorarios(HtmlUnitDriver driverNavegador) {
        // Array com os seletores CSS para os horários dos dias da semana
        String[] seletores = {
            "#Grid2ContainerTbl tr:not(:nth-child(1)) span", // Segunda-feira
            "#Grid3ContainerTbl tr:not(:nth-child(1)) span", // Terça-feira
            "#Grid4ContainerTbl tr:not(:nth-child(1)) span", // Quarta-feira
            "#Grid5ContainerTbl tr:not(:nth-child(1)) span", // Quinta-feira
            "#Grid6ContainerTbl tr:not(:nth-child(1)) span", // Sexta-feira
            "#Grid7ContainerTbl tr:not(:nth-child(1)) span"  // Sábado
        };
        
        for (int i = 0; i < seletores.length; i++) {
            // Capturando todos os <span> (Horário, Disciplina e Turma) que estão dentro das linhas de cada dia da semana
            // Ex: 15:00-15:50  POO  A
            // Ex: 15:50-16:40  POO  A
            List<WebElement> elementosSpansHorariosDisciplinasETurmasNoDia = driverNavegador.findElements(By.cssSelector(seletores[i]));

            // StringBuilder para organizar os horários do dia atual
            StringBuilder valoresHorariosAulaBuilder = new StringBuilder();
            int contador = 1;

            // Verificando se a lista de <span> está vazia
            if (!elementosSpansHorariosDisciplinasETurmasNoDia.isEmpty()) {
                for (WebElement elementoSpanHorarioDisciplinaETurmaNoDia : elementosSpansHorariosDisciplinasETurmasNoDia) {
                    // Adicionando o texto de cada <span> (Horário, Disciplina ou Turma) à string
                    valoresHorariosAulaBuilder.append(verificarElementoVazio(elementoSpanHorarioDisciplinaETurmaNoDia));

                    // Se o contador for menor que 3, adiciona uma vírgula; no terceiro elemento, adiciona um separador (" | ")
                    // O motivo para isso é que a cada 3 elementos um novo horário é iniciado.. Ex:
                    // 15:00-15:50,POO,A | 15:50-16:40,POO,A | ...
                    if (contador < 3) {
                        valoresHorariosAulaBuilder.append(",");
                    } else {
                        valoresHorariosAulaBuilder.append(" | ");
                        contador = 0;
                    }

                    contador++;
                }
            }
            // Adicionando os horários do dia atual a lista final
            listaHorarios.add(valoresHorariosAulaBuilder.toString());
        }
    }
    
    private static void coletarValoresNotasParciais(HtmlUnitDriver driverNavegador) {
        // Capturando todos os <tr>, que contêm o cabeçalho ("Avaliação Data de lançamento Nota") e seus respectivos valores
        List<WebElement> elementosLinhasNotasParciais = driverNavegador.findElements(By.cssSelector("tr.FreeStyleGridOdd table.Grid tr"));

        // Variáveis para estado atual
        String[] cabecalhos = null;
        List<String> valoresNotas = null;

        for (WebElement elementoLinhaNotaParcial : elementosLinhasNotasParciais) {
            // A linha atual pode ser igual a:
            // Avaliação Data de Lançamento Nota (cabeçalho) ou
            // P1 13/11/24 8,8 (valores avaliação)
            String valorLinhaAtual = elementoLinhaNotaParcial.getText();
            // Substituindo a "," por ".", para evitar erros futuros
            valorLinhaAtual = valorLinhaAtual.replace(",", ".");

            // Verificando se a linha atual é um cabeçalho
            if (valorLinhaAtual.startsWith("Avaliação")) {
                // Se já houver uma lista de notas parciais de uma matéria em progresso e a linha atual for um cabeçalho significa que a lista da matéria esta completa, e outra lista será iniciada.. Ex:
                // Avaliação Data de Lançamento Nota
                // P1 13/11/24 8.8
                // P2 13/11/24 8.8
                // Avaliação Data de Lançamento Nota (outro cabeçalho, ou seja: a lista esta completa)
                if (valoresNotas != null) {
                    notasParciaisPlanoDeEnsino.put(new JSONObject().put("nota_parcial", valoresNotas));
                }

                cabecalhos = valorLinhaAtual.split(" ");
                // Criando uma nova lista para a próxima matéria
                valoresNotas = new ArrayList<>();
            }
            // Se a linha atual não for um cabeçalho
            else {
                // Verificando se cabeçalhos e lista de valores foram inicializados
                if (cabecalhos != null && valoresNotas != null) {
                    // Divide a linha em partes usando espaço como separador
                    String[] valores = valorLinhaAtual.split(" ");

                    // Formata os valores separados por vírgulas e adiciona à lista
                    valoresNotas.add(String.join(", ", valores));
                }
            }
        }

        // Adicionando a última lista processada ao JSONArray
        if (valoresNotas != null) {
            notasParciaisPlanoDeEnsino.put(new JSONObject().put("nota_parcial", valoresNotas));
        }
    }

    private static void coletarValoresPresencasEFaltasParciaisPlanoDeEnsino(HtmlUnitDriver driverNavegador) {
        // Coletando todos os elementos <span> que contêm as presenças parciais do plano de ensino
        List<WebElement> elementosSpansQuantidadesPresencasParciaisDisciplinasPlanoDeEnsino = driverNavegador.findElements(By.cssSelector("#Grid1ContainerTbl tr:not(:nth-child(1)) td:nth-child(3) span"));
        // Coletando todos os elementos <span> que contêm as faltas parciais do plano de ensino
        List<WebElement> elementosSpansQuantidadesFaltasParciaisDisciplinasPlanoDeEnsino = driverNavegador.findElements(By.cssSelector("#Grid1ContainerTbl tr:not(:nth-child(1)) td:nth-child(4) span"));

        for (int i = 0; i < elementosSpansQuantidadesPresencasParciaisDisciplinasPlanoDeEnsino.size(); i++) {
            // Capturando a quantidade de presenças parciais da matéria atual presente no plano de ensino
            WebElement elementoSpanQuantidadePresencasParciaisDisciplinaPlanoDeEnsino = elementosSpansQuantidadesPresencasParciaisDisciplinasPlanoDeEnsino.get(i);
            int quantidadePresencasParciaisDisciplinaPlanoDeEnsino = Integer.parseInt(verificarElementoVazio(elementoSpanQuantidadePresencasParciaisDisciplinaPlanoDeEnsino));
            listaQuantidadePresencasParciaisPlanoDeEnsino.add(quantidadePresencasParciaisDisciplinaPlanoDeEnsino);

            // Capturando a quantidade de faltas parciais da matéria atual presente no plano de ensino
            WebElement elementoSpanQuantidadeFaltasParciaisDisciplinaPlanoDeEnsino = elementosSpansQuantidadesFaltasParciaisDisciplinasPlanoDeEnsino.get(i);
            int quantidadeFaltasParciaisDisciplinaPlanoDeEnsino = Integer.parseInt(verificarElementoVazio(elementoSpanQuantidadeFaltasParciaisDisciplinaPlanoDeEnsino));
            listaQuantidadeFaltasParciaisPlanoDeEnsino.add(quantidadeFaltasParciaisDisciplinaPlanoDeEnsino);
        }
    }

    private static void atribuirValoresJSON() {
        // Informações individuais
        estudante.put("nome_estudante", nomeEstudante);
        estudante.put("usuario_SIGA", usuarioSIGA);
        estudante.put("registro_acadêmico", registroAcademico);
        estudante.put("semestre_atual", semestreAtual);
        estudante.put("percentual_progressão", percentualDeProgressao + "%");
        estudante.put("percentual_progressão_intercâmbio", percentualDeProgressaoIntercambio + "%");
        estudante.put("percentual_rendimento", percentualDeRendimento + "%");
        estudante.put("quantidade_semestres_cursados", quantidadeSemestresCursados);
        estudante.put("email_institucional", emailInstitucional);
        estudante.put("nome_curso", nomeCurso);
        estudante.put("situação_curso", situacaoCurso);
        estudante.put("período_curso", periodoCurso);

        // Informações em listas
        // Plano de Ensino
        for (int i = 0; i < listaNomesDisciplinasPlanoDeEnsino.size(); i++) {
            // Capturando as notas parciais da disciplina atual
            JSONArray valoresNotasParciaisDisciplina = notasParciaisPlanoDeEnsino.getJSONObject(i).getJSONArray("nota_parcial");
            int quantidadeAulasRestantesDisciplina = listaQuantidadesAulasTotaisDisciplinasPlanoDeEnsino.get(i) - (listaQuantidadePresencasParciaisPlanoDeEnsino.get(i) + listaQuantidadeFaltasParciaisPlanoDeEnsino.get(i));
            
            planoDeEnsino.put(new JSONObject()
                .put("nome_disciplina", listaNomesDisciplinasPlanoDeEnsino.get(i))
                .put("sigla_disciplina", listaSiglasDisciplinasPlanoDeEnsino.get(i))
                .put("nome_professor", formatarNome(listaNomesProfessoresDisciplinasPlanoDeEnsino.get(i)))
                .put("ementa_disciplina", listaEmentasDisciplinasPlanoDeEnsino.get(i))
                .put("objetivo_disciplina", listaObjetivosDisciplinasPlanoDeEnsino.get(i))
                .put("quantidade_aulas_semanais_disciplina", listaQuantidadesAulasSemanaisDisciplinasPlanoDeEnsino.get(i))
                .put("quantidade_aulas_teóricas_disciplina", listaQuantidadesAulasTeoricasDisciplinasPlanoDeEnsino.get(i))
                .put("quantidade_aulas_práticas_disciplina", listaQuantidadesAulasPraticasDisciplinasPlanoDeEnsino.get(i))
                .put("quantidade_total_aulas_disciplina", listaQuantidadesAulasTotaisDisciplinasPlanoDeEnsino.get(i))
                .put("quantidade_presenças_parciais_disciplina", listaQuantidadePresencasParciaisPlanoDeEnsino.get(i))
                .put("quantidade_faltas_atual_disciplina", listaQuantidadeFaltasParciaisPlanoDeEnsino.get(i))
                .put("limite_faltas_permitidas_disciplina", listaQuantidadesFaltasPossiveisPlanoDeEnsino.get(i))
                .put("quantidade_aulas_restantes_disciplina", quantidadeAulasRestantesDisciplina) //total-(presenças+faltas)
                .put("notas_parciais_disciplina", valoresNotasParciaisDisciplina)
            );
        }
        // Historico Completo
        for (int i = 0; i < listaSiglasDisciplinasHistoricoCompleto.size(); i++) {
            historicoCompleto.put(new JSONObject()
                .put("nome_disciplina", listaNomesDisciplinasHistoricoCompleto.get(i))
                .put("sigla_disciplina", listaSiglasDisciplinasHistoricoCompleto.get(i))
                .put("período_disciplina", listaPeriodosDisciplinasHistoricoCompleto.get(i))
                .put("situação_disciplina", listaSituacoesDisciplinasHistoricoCompleto.get(i))
                .put("média_final_disciplina", listaMediasFinaisDisciplinasHistoricoCompleto.get(i))
                .put("frequência_disciplina", listaFrequenciasDisciplinasHistoricoCompleto.get(i))
                .put("quantidade_faltas_disciplina", listaQuantidadesFaltasDisciplinasHistoricoCompleto.get(i))
                .put("observação_disciplina", listaObservacoesDisciplinasHistoricoCompleto.get(i))
            );
        }

        // Horários Plano de Ensino
        String[] diasSemana = {"Segunda-feira", "Terça-feira", "Quarta-feira", "Quinta-feira", "Sexta-feira", "Sábado"};       
        // Processando os valores da lista de horários
        for (int i = 0; i < diasSemana.length; i++) {
            String diaAtual = diasSemana[i];
            String[] linhasDiaAtual = listaHorarios.get(i).split(" \\| ");

            // Map para agrupar horários por turma e sigla, evitando duplicações
            Map<String, JSONObject> turmaSiglaMap = new HashMap<>();

            for (String linhaDiaAtual : linhasDiaAtual) {
                if (!linhaDiaAtual.isEmpty()) {
                    String[] valoresLinhaDiaAtual = linhaDiaAtual.split(",");
                    
                    String horarioLinhaDiaAtual = valoresLinhaDiaAtual[0];
                    String siglaLinhaDiaAtual = valoresLinhaDiaAtual[1];
                    String turmaLinhaDiaAtual = valoresLinhaDiaAtual[2];

                    // Chave única baseada em "turma|sigla" para agrupar dados
                    String chave = turmaLinhaDiaAtual + "|" + siglaLinhaDiaAtual;

                    // Verifica se já existe um objeto para essa turma e sigla
                    if (!turmaSiglaMap.containsKey(chave)) {
                        JSONObject informacoesHorarioDisciplinaAtual = new JSONObject()
                            .put("turma_disciplina", turmaLinhaDiaAtual)
                            .put("sigla_disciplina", siglaLinhaDiaAtual)
                            .put("dia_semana", diaAtual)
                            .put("horários_disciplina", new JSONArray());

                        turmaSiglaMap.put(chave, informacoesHorarioDisciplinaAtual);
                    }

                    // Adiciona o horário ao array de horários
                    turmaSiglaMap.get(chave).getJSONArray("horários_disciplina").put(horarioLinhaDiaAtual);
                }
            }

            // Adiciona os objetos do map ao JSON final
            horariosPlanoDeEnsino.putAll(turmaSiglaMap.values());
        }
    }
    
    private static void criarSessionUsuarioLogado(HttpServletRequest request) {
        HttpSession session = request.getSession();
        // Armazenando o status de logado e informações sobre o usuário em uma sessão
        session.setAttribute("isSIGALogado", "true");
        session.setAttribute("estudante", estudante);
        session.setAttribute("avisos", avisos);
        session.setAttribute("planoDeEnsino", planoDeEnsino);
        session.setAttribute("historicoCompleto", historicoCompleto);
        session.setAttribute("horariosPlanoDeEnsino", horariosPlanoDeEnsino);
    }
    
    private static String verificarElementoVazio(WebElement elemento) {
        String textoElemento = elemento.getText();
        
        if (!textoElemento.isEmpty()) {
            return textoElemento;   
        } else {
            return "Valor não encontrado";
        }
    }
    
    public static void esperarCarregamentoPagina(HtmlUnitDriver driverNavegador, String tituloDesejado) throws InterruptedException {
        String tituloPaginaAtual = driverNavegador.getTitle();
        // Aguardando 3 segundos para a página carregar
        Thread.sleep(3000);
        
        // Se o título da página atual for diferente do título esperado 
        if (!tituloPaginaAtual.equals(tituloDesejado)) {
            Thread.sleep(4000);
        }
    }
    
    private static String formatarNome(String nome) {
        String[] partesNome = nome.toLowerCase().split(" ");
        StringBuilder nomeFormatado = new StringBuilder();

        for (String parteNome : partesNome) {
            // Transformando primeira letra de cada parte do nome em maiúscula e adiciona ao StringBuilder
            nomeFormatado.append(Character.toUpperCase(parteNome.charAt(0)))
                .append(parteNome.substring(1))
                .append(" ");
        }

        // Remove o espaço extra no final da StringBuilder e converte para String
        nome = nomeFormatado.toString().trim();
        
        return nome;
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("index.jsp");
    }
}
