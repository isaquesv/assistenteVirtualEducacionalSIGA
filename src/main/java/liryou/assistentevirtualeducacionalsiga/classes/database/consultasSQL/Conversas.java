package liryou.assistentevirtualeducacionalsiga.classes.database.consultasSQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public interface Conversas {

    public static void SelectConversa(Connection connection, int cdConversa) {
        try {
            // Prepara o comando SQL de seleção
            PreparedStatement pstmt = connection.prepareStatement(
                "SELECT * FROM CONVERSAS WHERE cd_conversa = ?"
            );
            // Define o valor do parâmetro
            pstmt.setInt(1, cdConversa);
            
            // Executa a consulta
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("Conversa encontrada para o aluno: " + rs.getInt("id_aluno"));
            } else {
                System.out.println("Conversa não encontrada.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao consultar a tabela CONVERSAS: " + e.getMessage());
        }
    }

    public static void InsertConversa(Connection connection, int cdConversa, int idAluno) {
        try {
            // Prepara o comando SQL de inserção
            PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO CONVERSAS (cd_conversa, id_aluno) VALUES (?, ?)"
            );
            // Define os valores dos parâmetros
            pstmt.setInt(1, cdConversa);
            pstmt.setInt(2, idAluno);
            
            // Executa a inserção
            pstmt.executeUpdate();
            System.out.println("Conversa inserida com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao inserir na tabela CONVERSAS: " + e.getMessage());
        }
    }
}
