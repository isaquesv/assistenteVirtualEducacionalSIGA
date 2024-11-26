package liryou.assistentevirtualeducacionalsiga.servlets;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
// Permite gerenciar sessões
import jakarta.servlet.http.HttpSession;

/** @author Liryou */
@WebServlet(name = "FinalizarSessao", urlPatterns = {"/FinalizarSessao"})
public class FinalizarSessao extends HttpServlet {
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

        response.getWriter().print(isSessaoFinalizada);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("index.jsp");
    }
}
