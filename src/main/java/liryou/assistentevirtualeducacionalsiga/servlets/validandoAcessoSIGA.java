package liryou.assistentevirtualeducacionalsiga.servlets;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
//  Importando bibliotecas do Selenium que permitem automação de navegação na web
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**  @author Liryou */
@WebServlet(name = "validandoAcessoSIGA", urlPatterns = {"/validandoAcessoSIGA"})
public class validandoAcessoSIGA extends HttpServlet {
    //  Método que será chamado quando uma requisição POST for feita para este servlet
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            //  Capturando os valores do formulário enviados pelo usuário, caso existam
            if (request.getParameter("usuarioSIGA") != null || request.getParameter("senhaSIGA") != null) {
                String usuarioSIGA = request.getParameter("usuarioSIGA");
                String senhaSIGA = request.getParameter("senhaSIGA");
                boolean isAcessoSIGAValido = false;

                //  Criando uma instância com o HtmlUnitDriver (um navegador "headless"), para possibilitar interações web sem exibir uma janela
                WebDriver driver = new HtmlUnitDriver(true);  //  (true) ativa o suporte a JavaScript

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
                        isAcessoSIGAValido = true;
                    }

                    //  Retorna o valor como texto ("true" ou "false")
                    response.getWriter().print(isAcessoSIGAValido);
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
        //  Redirecionando o usuário para 'index.jsp'
        response.sendRedirect("index.jsp");
    }
}