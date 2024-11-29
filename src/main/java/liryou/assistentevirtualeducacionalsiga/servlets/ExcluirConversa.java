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

/** @author Liryou */
@WebServlet(name = "ExcluirConversa", urlPatterns = {"/ExcluirConversa"})
public class ExcluirConversa extends HttpServlet {
    //  Método que será chamado quando uma requisição POST for feita para este servlet
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Connection connectionBD = null;    

        // Captura o valor da sessão "codigoAluno" e "codigoConversa"
        int codigoAluno = (Integer) session.getAttribute("codigoAluno");
        int codigoConversa = (Integer) session.getAttribute("codigoConversa");
        boolean isMensagensExcluidas = false;
        
        try {
            // Obtém a conexão e chama os métodos para criar tabelas e verificar tabelas vazias
            connectionBD = DriverBD.getConnection();
            int quantidadeMensagens = Mensagens.SelectMensagensContagemRegistros(connectionBD, codigoConversa, codigoAluno);
            
            if (quantidadeMensagens == 0) {
                isMensagensExcluidas = true;
            } else {
                isMensagensExcluidas = Mensagens.UpdateDesativarMensagens(connectionBD, codigoConversa, codigoAluno);
            }       
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            // Fechar a conexão com o banco de dados, para que ela consiga ser chamada novamente
            DriverBD.closeConnection(connectionBD);
        }
        
        response.getWriter().print(isMensagensExcluidas);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("index.jsp");
    }
}
