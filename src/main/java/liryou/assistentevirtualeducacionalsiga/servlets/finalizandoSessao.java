package liryou.assistentevirtualeducacionalsiga.servlets;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/** @author Liryou */
@WebServlet(name = "finalizandoSessao", urlPatterns = {"/finalizandoSessao"})
public class finalizandoSessao extends HttpServlet {
    //  Método que será chamado quando uma requisição POST for feita para este servlet
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isSessaoFinalizada = false;
        
        //  Invalidando a sessão
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
            isSessaoFinalizada = true;
        }

        //  Retornando o valor como texto ("true" ou "false")
        response.getWriter().print(isSessaoFinalizada);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Redirecionando o usuário para 'index.jsp'
        response.sendRedirect("index.jsp");
    }

}
