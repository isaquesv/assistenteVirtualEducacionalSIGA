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
// Classes que contém as consultas SQL disponíveis para a tabela Mensagens
import liryou.assistentevirtualeducacionalsiga.classes.database.consultasSQL.Mensagens;
// Permite a manipulação de Arrays JSON
import org.json.JSONArray;

/** @author Liryou */
@WebServlet(name = "CapturarMensagensAnterioresConversa", urlPatterns = {"/CapturarMensagensAnterioresConversa"})
public class CapturarMensagensAnterioresConversa extends HttpServlet {
    //  Método que será chamado quando uma requisição POST for feita para este servlet
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Connection connectionBD = null;    

        // Captura o valor da sessão "codigoAluno" e "codigoConversa"
        int codigoConversa = (Integer) session.getAttribute("codigoConversa");
        int codigoAluno = (Integer) session.getAttribute("codigoAluno");
        JSONArray listaMensagensCapturadas = new JSONArray();
        
        try {
            // Obtém a conexão e chama os métodos para criar tabelas e verificar tabelas vazias
            connectionBD = DriverBD.getConnection();
            listaMensagensCapturadas = Mensagens.SelectMensagens(connectionBD, codigoConversa, codigoAluno);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            // Fechar a conexão com o banco de dados, para que ela consiga ser chamada novamente
            DriverBD.closeConnection(connectionBD);
        }
        
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        // Retorna o resultado da operação para o cliente
        response.getWriter().print(listaMensagensCapturadas);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("index.jsp");
    }
}