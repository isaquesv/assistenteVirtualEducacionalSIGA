package liryou.assistentevirtualeducacionalsiga.servlets;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**  @author Liryou */
@WebServlet(name = "validandoLoginUsuario", urlPatterns = {"/validandoLoginUsuario"})
public class validandoLoginUsuario extends HttpServlet {
    //  Método que será chamado quando uma requisição POST for feita para este servlet
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            //  Capturando os valores do formulário enviados pelo usuário, caso existam
            if (request.getParameter("emailUsuario") != null || request.getParameter("senhaUsuario") != null) {
                String emailUsuario = request.getParameter("emailUsuario");
                String senhaUsuario = request.getParameter("senhaUsuario");
                boolean isLoginUsuarioValido = false;

                //  DB = VERIFICAR SE O EMAIL DIGITADO JÁ ESTA CADASTRADO NO BANCO. SE JÁ ESTIVER..
                if ("exemplo@usuario.com".equals(emailUsuario) && "123".equals(senhaUsuario)) {
                    isLoginUsuarioValido = true;
                    
                    //  Armazenando o status de logado em uma sessão
                    HttpSession session = request.getSession();
                    session.setAttribute("isUsuarioLogado", "true");
                }

                //  Retornando o valor como texto ("true" ou "false")
                response.getWriter().print(isLoginUsuarioValido);
            } else {
                //  Redirecionando o usuário para 'index.jsp'
                response.sendRedirect("index.jsp");
            }
        } catch (Exception ex) {
            response.sendRedirect("index.jsp");
        }
    }

    // Para caso o usuário tente acessar esta página sem ter informado o Email e a Senha, ou seja, tentar burlar o sistema através da URL do site
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("index.jsp");
    }
}