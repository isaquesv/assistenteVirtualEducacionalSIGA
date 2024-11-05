package liryou.assistentevirtualeducacionalsiga.classes.database.consultasSQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public interface Alunos {
    
    public static boolean SelectAluno(Connection connection, Statement statement, String cpf) {
        boolean existeAluno = true;
        
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
            
            if (numRows == 0) {
                existeAluno = false;
                InsertAluno(connection, statement, cpf);
            }
        } catch (SQLException e) {
            // Erro de conexão ou execução
            System.err.println(e.getMessage());
        }
        
        return existeAluno;
    }
    
    public static boolean InsertAluno(Connection connection, Statement statement, String cpf){
        boolean alunoInserido = true;
        
        try {
            statement.executeUpdate("INSERT INTO alunos VALUES(null, '" + cpf + "', 1)");
            if (SelectAluno(connection, statement, cpf) == false) {
                alunoInserido = false;
            }
        } catch (SQLException e) {
            // Erro de conexão ou execução
            System.err.println(e.getMessage());
        }
        
        return alunoInserido;
    }
    
    public static boolean UpdateStatusAluno(Connection connection, Statement statement, String cpf){
        boolean statusAlunoAtualizado = true;
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                "UPDATE alunos SET is_aluno_ativo = 1 - is_aluno_ativo WHERE cpf_aluno = ?"
            );
            pstmt.setString(1, cpf);  // Define o valor do CPF no PreparedStatement

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected == 0) {
                statusAlunoAtualizado = false;
            }
        } catch (SQLException e) {
            // Erro de conexão ou execução
            System.err.println(e.getMessage());
        }
        
        return statusAlunoAtualizado;
    }
}
    

