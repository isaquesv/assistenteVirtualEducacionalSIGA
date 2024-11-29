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
// Classes que contém as consultas SQL disponíveis para a tabela Alunos
import liryou.assistentevirtualeducacionalsiga.classes.database.consultasSQL.Alunos;
//
import org.json.JSONObject;

/** @author Liryou */
@WebServlet(name = "ExcluirConta", urlPatterns = {"/ExcluirConta"})
public class ExcluirConta extends HttpServlet {
    //  Método que será chamado quando uma requisição POST for feita para este servlet
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isContaDesativada = false;
        Connection connectionBD = null;    
        
        HttpSession session = request.getSession();
        JSONObject estudante = new JSONObject();
        estudante = (JSONObject) session.getAttribute("estudante");     
        String usuarioSIGA = estudante.getString("usuario_SIGA");
   
        try {
            // Obtém a conexão e chama os métodos para criar tabelas e verificar tabelas vazias
            connectionBD = DriverBD.getConnection();
            isContaDesativada = Alunos.UpdateStatusAluno(connectionBD, usuarioSIGA);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            // Fechar a conexão com o banco de dados, para que ela consiga ser chamada novamente
            DriverBD.closeConnection(connectionBD);
        }
        
        response.getWriter().print(isContaDesativada);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("index.jsp");
    }
}