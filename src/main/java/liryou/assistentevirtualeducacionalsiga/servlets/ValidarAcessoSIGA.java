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

// Permite a conexão com o banco de dados
import java.sql.Connection;
// Classe responsável por gerenciar a criação e encerramento do banco de dados
import liryou.assistentevirtualeducacionalsiga.classes.DriverBD;
// Classes que contém as consultas SQL disponíveis para as tabelas Alunos e Conversas
import liryou.assistentevirtualeducacionalsiga.classes.database.consultasSQL.Alunos;
import liryou.assistentevirtualeducacionalsiga.classes.database.consultasSQL.Conversas;
// Método que permite esperar o carregamento das páginas
import static liryou.assistentevirtualeducacionalsiga.servlets.ColetarDadosSIGA.esperarCarregamentoPagina;

/** @author Liryou */
@WebServlet(name = "ValidarAcessoSIGA", urlPatterns = {"/ValidarAcessoSIGA"})
public class ValidarAcessoSIGA extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String mensagemResultadoValidacaoAcessoSIGA = null;

        Connection connectionBD = null;
        // Instanciando a classe Driver que gerencia o navegador HtmlUnitDriver
        DriverNavegador driverManager = new DriverNavegador();
        HtmlUnitDriver driverNavegador = driverManager.getDriver();

        try {
            if (request.getParameter("usuarioSIGA") != null && request.getParameter("senhaSIGA") != null) {
                // Obtém a conexão e chama os métodos para criar tabelas e verificar tabelas vazias
                connectionBD = DriverBD.getConnection();

                String usuarioSIGA = request.getParameter("usuarioSIGA");
                String senhaSIGA = request.getParameter("senhaSIGA");

                // Acessa a página de login do SIGA
                driverNavegador.get("https://siga.cps.sp.gov.br/aluno/login.aspx");
                
                if (driverNavegador.getTitle().equals("cannot open database \"ASPState\" requested by the login. The login failed.<br>Login failed for user 'aspnet'.")) {
                    mensagemResultadoValidacaoAcessoSIGA = "O SIGA está temporariamente fora do ar. Por favor, aguarde e tente novamente mais tarde.";
                } else {
                    // Localizando os elementos do formulário de login na página
                    WebElement loginFormSigaInputUsuario = driverNavegador.findElement(By.cssSelector("#vSIS_USUARIOID"));
                    WebElement loginFormSigaInputSenha = driverNavegador.findElement(By.cssSelector("#vSIS_USUARIOSENHA"));
                    WebElement loginFormSigaSubmitButton = driverNavegador.findElement(By.name("BTCONFIRMA"));

                    // Preenchendo o formulário com os dados fornecidos e submetendo-o
                    loginFormSigaInputUsuario.sendKeys(usuarioSIGA);
                    loginFormSigaInputSenha.sendKeys(senhaSIGA);
                    loginFormSigaSubmitButton.click();

                    esperarCarregamentoPagina(driverNavegador, "home");
                    
                    if (driverNavegador.getTitle().equals("home")) {
                        WebElement elementoNomeUnidade = driverNavegador.findElement(By.cssSelector("#span_vUNI_UNIDADENOME_MPAGE"));
                        String nomeUnidade = elementoNomeUnidade.getText();

                        if (nomeUnidade.equals("Faculdade de Tecnologia de Praia Grande")) {

                            int codigoAluno = Alunos.SelectAluno(connectionBD, usuarioSIGA);
                            // Cadastra o aluno caso não exista um registro com o usuario informado
                            if (codigoAluno == 0) {
                                codigoAluno = Alunos.InsertAluno(connectionBD, usuarioSIGA);
                            }

                            int codigoConversa = Conversas.SelectConversa(connectionBD, codigoAluno);
                            // Cadastra uma conversa caso não exista um registro para o aluno
                            if (codigoConversa == 0) {
                                codigoConversa = Conversas.InsertConversa(connectionBD, codigoAluno);
                            }

                            if (codigoAluno > 0 && codigoConversa > 0) {
                                int statusContaAlunoAtivoOuNao = Alunos.SelectAlunoStatusAtivo(connectionBD, usuarioSIGA);
                                // Reativa a conta do aluno caso a conta estiver desativada
                                if (statusContaAlunoAtivoOuNao == 0) {
                                    Alunos.UpdateStatusAluno(connectionBD, usuarioSIGA);
                                }

                                HttpSession session = request.getSession();
                                // Armazenando o codigo do aluno e do seu respectivo chat com a IA em uma sessão
                                session.setAttribute("codigoAluno", codigoAluno);
                                session.setAttribute("codigoConversa", codigoConversa);

                                mensagemResultadoValidacaoAcessoSIGA = "Sucesso no login";

                                // Saindo do SIGA
                                WebElement elementoInputSairDoSIGA = driverNavegador.findElement(By.cssSelector("#MPW0041IMAGESAIR"));
                                elementoInputSairDoSIGA.click();
                            } else {
                                mensagemResultadoValidacaoAcessoSIGA = "Falha ao registrar aluno";
                            }

                        } else {
                            mensagemResultadoValidacaoAcessoSIGA = "O login SIGA informado pertence a uma instituição diferente da Fatec Praia Grande. Por favor, verifique e insira um login SIGA válido.";
                        }
                    } else {
                        mensagemResultadoValidacaoAcessoSIGA = "Falha no login - Não confere Usuário e Senha";
                    }
                }
            } else {
                mensagemResultadoValidacaoAcessoSIGA = "Falha ao receber parâmetros (usuário, senha)";
            }
        } catch (Exception e) {
            mensagemResultadoValidacaoAcessoSIGA = e.getMessage();
        } finally {
            // Fecha o navegador
            driverManager.quitDriver();
            // Fecha a conexão com o banco de dados
            DriverBD.closeConnection(connectionBD);
        }

        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(mensagemResultadoValidacaoAcessoSIGA);
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("index.jsp");
    }
}
