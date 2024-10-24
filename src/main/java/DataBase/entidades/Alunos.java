package DataBase.entidades;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public interface Alunos {
    
    public static void SelectAluno(Connection connection, Statement statement, String cpf) {
        try {
            // Consulta e exibe os dados da tabela 'alunos'
            PreparedStatement pstmt = connection.prepareStatement(
                "SELECT * FROM alunos WHERE cpf_aluno = ?"
            );
            pstmt.setString(1, cpf);
            
            // Executa a consulta
            ResultSet rs = pstmt.executeQuery();
            int numRows = 0;
            while (rs.next()) {
                numRows++;
            }
            
            if (numRows > 0) {
                System.out.println("Usuário existe");
            } else {
                System.out.println("Usuário não existe");
                InsertAluno(connection, statement, cpf);
            }
        } catch (SQLException e) {
            // Erro de conexão ou execução
            System.err.println(e.getMessage());
        }
    }
    public static void InsertAluno(Connection connection, Statement statement, String cpf){
        try {
            statement.executeUpdate("INSERT INTO alunos VALUES(null, '" + cpf + "', 1)");
        } catch (SQLException e) {
            // Erro de conexão ou execução
            System.err.println(e.getMessage());
        }
    }
    public static void UpdateStatusAluno(Connection connection, Statement statement, String cpf){
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                "UPDATE alunos SET is_aluno_ativo = 1 - is_aluno_ativo WHERE cpf_aluno = ?"
            );
            pstmt.setString(1, cpf);  // Define o valor do CPF no PreparedStatement

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("O status de atividade do aluno foi atualizado com sucesso.");
            } else {
                System.out.println("Nenhum aluno com o CPF fornecido foi encontrado.");
            }
        } catch (SQLException e) {
            // Erro de conexão ou execução
            System.err.println(e.getMessage());
        }
    }
}
    

