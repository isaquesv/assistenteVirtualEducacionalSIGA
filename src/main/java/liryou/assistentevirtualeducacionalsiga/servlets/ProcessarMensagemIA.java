package liryou.assistentevirtualeducacionalsiga.servlets;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
// Permite gerenciar sessões
import jakarta.servlet.http.HttpSession;

// Permite a conexão com o banco de dados
import java.sql.Connection;
// Classe responsável por gerenciar a criação e encerramento do banco de dados
import liryou.assistentevirtualeducacionalsiga.classes.DriverBD;

// Classe responsável por manipular o Gemini
import liryou.assistentevirtualeducacionalsiga.classes.IAGemini;

@WebServlet(name = "ProcessarMensagemIA", urlPatterns = {"/ProcessarMensagemIA"})
public class ProcessarMensagemIA extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Connection connectionBD = null;    

        int codigoAluno = (Integer) session.getAttribute("codigoAluno");
        int codigoConversa = (Integer) session.getAttribute("codigoConversa");
        String respostaIA = null;
        
        try {
            if (request.getParameter("promptAluno") != null) {
                // Obtém a conexão e chama os métodos para criar tabelas e verificar tabelas vazias
                connectionBD = DriverBD.getConnection();
                String promptAluno = request.getParameter("promptAluno");
                
                // Chama o método de gerar resposta da classe ChatIA
                respostaIA = IAGemini.gerarResposta(request, connectionBD, codigoConversa, codigoAluno, promptAluno);
            } else {
                respostaIA = "Falha ao receber parâmetro (pergunta)";
            }
        } catch (Exception e) {
            respostaIA = e.getMessage();
        } finally {
            // Fechar a conexão com o banco de dados, para que ela consiga ser chamada novamente
            DriverBD.closeConnection(connectionBD);
        }
                
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(respostaIA);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("index.jsp");
    }
}