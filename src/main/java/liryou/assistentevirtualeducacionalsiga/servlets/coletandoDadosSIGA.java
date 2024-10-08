package liryou.assistentevirtualeducacionalsiga.servlets;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
// Importando bibliotecas do Selenium que permitem automação de navegação na web
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
//  Importa classes que permitem a criação e manipulação de Arrays
import java.util.ArrayList;
import java.util.List;
//  Importante classes que permitem criar sessões
import jakarta.servlet.http.HttpSession;

/**  @author Liryou */
@WebServlet(name = "coletandoDadosSIGA", urlPatterns = {"/coletandoDadosSIGA"})
public class coletandoDadosSIGA extends HttpServlet {
    //  Método que será chamado quando uma requisição POST for feita para este servlet
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            //  Capturando os valores do formulário enviados pelo usuário, caso existam
            if (request.getParameter("usuarioSIGA") != null || request.getParameter("senhaSIGA") != null) {
                String usuarioSIGA = request.getParameter("usuarioSIGA");
                String senhaSIGA = request.getParameter("senhaSIGA");
                boolean isColetandoDadosSIGASucesso = false;

                //  Criando uma instância com o HtmlUnitDriver (um navegador "headless"), para possibilitar interações web sem exibir uma janela
                WebDriver driver = new HtmlUnitDriver(true); //  (true) ativa o suporte a JavaScript

                try {
                    //  Acessando a página de login do SIGA
                    driver.get("https://siga.cps.sp.gov.br/aluno/login.aspx");

                    //  Encontrando os campos de CPF e senha na página de login
                    WebElement loginFormSigaInputCpf = driver.findElement(By.id("vSIS_USUARIOID"));
                    WebElement loginFormSigaInputSenha = driver.findElement(By.id("vSIS_USUARIOSENHA"));
                    WebElement loginFormSigaSubmitButton = driver.findElement(By.name("BTCONFIRMA"));

                    //  Preenchendo os campos de CPF e senha com os valores capturados do usuário
                    loginFormSigaInputCpf.sendKeys(usuarioSIGA);
                    loginFormSigaInputSenha.sendKeys(senhaSIGA);
                    //  Clicando no botão de login para tentar acessar o sistema
                    loginFormSigaSubmitButton.click();

                    //  Esperando 3 segundos
                    Thread.sleep(3000);

                    //  Se o título da página atual não for "home"
                    if (!driver.getTitle().equals("home")) {
                        //  Esperando +4 segundos
                        Thread.sleep(4000);
                    }

                    //  Se o login for um sucesso e o título da página atual for "home"
                    if (driver.getTitle().equals("home")) {
                        //  Encontrando os elementos que possuem as informações do aluno
                        WebElement nomePessoalAluno = driver.findElement(By.cssSelector("#span_MPW0041vPRO_PESSOALNOME"));
                        WebElement registroAcademicoAluno = driver.findElement(By.cssSelector("#span_MPW0041vACD_ALUNOCURSOREGISTROACADEMICOCURSO"));
                        WebElement cicloAtualAluno = driver.findElement(By.cssSelector("#span_MPW0041vACD_ALUNOCURSOCICLOATUAL"));
                        WebElement emailInstitucionalAluno = driver.findElement(By.cssSelector("#span_MPW0041vINSTITUCIONALFATEC"));
                        WebElement nomeUnidade = driver.findElement(By.cssSelector("#span_vUNI_UNIDADENOME_MPAGE"));
                        WebElement nomeCurso = driver.findElement(By.cssSelector("#span_vACD_CURSONOME_MPAGE"));
                        WebElement situacaoCurso = driver.findElement(By.cssSelector("#span_vSITUACAO_MPAGE"));
                        WebElement periodoCurso = driver.findElement(By.cssSelector("#span_vACD_PERIODODESCRICAO_MPAGE"));

                        String nomePessoal = nomePessoalAluno.getText();
                        nomePessoal = nomePessoal.substring(0, nomePessoal.length() - 1);

                        //  Capturando todos os elementos <span> que contêm o plano de ensino do aluno na página 
                        List<WebElement> spansPlanoDeEnsino = driver.findElements(By.cssSelector("#ygtvc24 span.NodeTextDecoration"));
                        //  Criando uma lista para armazenar os nomes das matérias
                        List<String> valoresPlanoDeEnsino = new ArrayList<>();
                        for (WebElement span : spansPlanoDeEnsino) {
                            //  Adicionando o texto de cada <span> (matéria) à lista
                            valoresPlanoDeEnsino.add(span.getText());
                        }

                        //  Criando uma string com as matérias do plano de ensino, separadas por vírgulas
                        String planoDeEnsino = String.join(",", valoresPlanoDeEnsino);

                        //  Criandondo a string contextoInicial, responsável por conter uma descrição do aluno
                        String contextoInicial = "Você é um assistente inteligente e educado, que está conversando com um estudante da Fatec (Faculdade de Tecnologia). Responda sempre em português do Brasil e forneça apenas as informações estritamente relacionadas à pergunta do usuário. Não revele informações pessoais como nome completo, RA, email, ou dados do curso a menos que o usuário pergunte diretamente sobre esses detalhes. O nome completo deste aluno é " + nomePessoal + ". Seu Registro Acadêmico (RA) é " + registroAcademicoAluno.getText() + ". Seu Email Institucional é " + emailInstitucionalAluno.getText() + ". O nome da unidade da FATEC onde o aluno está matriculado é " + nomeUnidade.getText() + ". O nome do curso que este aluno está cursando é " + nomeCurso.getText() + ". O ciclo/semestre acadêmico atual do aluno é " + cicloAtualAluno.getText() + ". A situação do aluno neste curso é " + situacaoCurso.getText() + ". O período do curso é " + periodoCurso.getText() + ". A lista de disciplinas no plano de ensino deste aluno, formatada como uma String de disciplinas separadas por vírgulas, é " + planoDeEnsino + ". Lembre-se de fornecer apenas as informações estritamente relacionadas à pergunta, sem adicionar detalhes não solicitados.";

                        HttpSession session = request.getSession();
                        //  Armazenando o contextoInicial e o status de logado em uma sessão
                        session.setAttribute("contextoInicial", contextoInicial);
                        session.setAttribute("isSIGALogado", "true");

                        isColetandoDadosSIGASucesso = true;
                    }

                    //  Retornando o valor como texto ("true" ou "false")
                    response.getWriter().print(isColetandoDadosSIGASucesso);
                } finally {
                    // Fechando o driver
                    driver.quit();
                }
            } else {
                // Redirecionando o usuário para 'index.jsp'
                response.sendRedirect("index.jsp");
            }
        } catch (Exception ex) {
            response.sendRedirect("index.jsp");
        }

    }

    //  Caso o usuário tente acessar esta página sem ter informado o CPF e a Senha, ou seja, tentar burlar o sistema através da URL do site
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("index.jsp");
    }
}
