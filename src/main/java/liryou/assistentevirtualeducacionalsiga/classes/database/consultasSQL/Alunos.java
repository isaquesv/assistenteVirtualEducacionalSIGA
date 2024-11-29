package liryou.assistentevirtualeducacionalsiga.classes.database.consultasSQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface Alunos {
    
    public static int SelectAluno(Connection connectionBD, String usuarioSIGA) {
        int codigoAluno = 0;
        
        try {
            // Prepara o comando SQL de seleção
            PreparedStatement pstmt = connectionBD.prepareStatement(
                "SELECT * " +
                "FROM alunos " +
                "WHERE usuario_aluno = ?"
            );
            pstmt.setString(1, usuarioSIGA);
            
            // Executa a consulta
            ResultSet resultadoConsulta = pstmt.executeQuery();
            // Verifica se existe pelo menos um registro na consulta
            if (resultadoConsulta.next()) {
                codigoAluno = resultadoConsulta.getInt("cd_aluno");
            } 
        } catch (SQLException e) {
            // Erro de conexão ou execução
            System.err.println(e.getMessage());
        }
        
        return codigoAluno;
    }
    
    public static int SelectAlunoStatusAtivo(Connection connectionBD, String usuarioSIGA) {
        int statusContaAluno = 0;
        
        try {
            // Prepara o comando SQL de seleção
            PreparedStatement pstmt = connectionBD.prepareStatement(
                "SELECT * " +
                "FROM alunos " +
                "WHERE usuario_aluno = ?"
            );
            pstmt.setString(1, usuarioSIGA);
            
            // Executa a consulta
            ResultSet resultadoConsulta = pstmt.executeQuery();
            // Verifica se existe pelo menos um registro na consulta
            if (resultadoConsulta.next()) {
                statusContaAluno = resultadoConsulta.getInt("is_aluno_ativo");
            } 
        } catch (SQLException e) {
            // Erro de conexão ou execução
            System.err.println(e.getMessage());
        }
        
        return statusContaAluno;
    }
    
    public static int InsertAluno(Connection connectionBD, String usuarioSIGA){
        int codigoAlunoInserido = 0;
        
        try {
            // Prepara o comando SQL de inserção
            PreparedStatement pstmt = connectionBD.prepareStatement(
                "INSERT INTO alunos(usuario_aluno, is_aluno_ativo) " +
                "VALUES(?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS
            );
            pstmt.setString(1, usuarioSIGA);
            pstmt.setInt(2, 1);
            
            // Executa a inserção
            int insercaoBemSucedida = pstmt.executeUpdate();
            if (insercaoBemSucedida == 1) {
                // Obtém a chave(cd_aluno) gerada
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    codigoAlunoInserido = generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            // Erro de conexão ou execução
            System.err.println(e.getMessage());
        }
        
        return codigoAlunoInserido;
    }
    
    public static boolean UpdateStatusAluno(Connection connectionBD, String usuarioSIGA){
        boolean statusAlunoAtualizado = false;
        
        try {
            // Prepara o comando SQL de atualização
            PreparedStatement pstmt = connectionBD.prepareStatement(
                "UPDATE alunos " +
                "SET is_aluno_ativo = 1 - is_aluno_ativo " +
                "WHERE usuario_aluno = ?"
            );
            pstmt.setString(1, usuarioSIGA);

            // Executa a inserção
            int atualizacaoBemSucedida = pstmt.executeUpdate();
            if (atualizacaoBemSucedida == 1) {
                statusAlunoAtualizado = true;
            }
        } catch (SQLException e) {
            // Erro de conexão ou execução
            System.err.println(e.getMessage());
        }
        
        return statusAlunoAtualizado;
    }
}
