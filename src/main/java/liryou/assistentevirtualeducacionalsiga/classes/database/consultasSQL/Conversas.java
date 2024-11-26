package liryou.assistentevirtualeducacionalsiga.classes.database.consultasSQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface Conversas {

    public static int SelectConversa(Connection connectionBD, int codigoAluno) {
        int codigoConversa = 0;

        try {
            // Prepara o comando SQL de seleção
            PreparedStatement pstmt = connectionBD.prepareStatement(
                "SELECT * " +
                "FROM conversas " +
                "WHERE id_aluno = ?"
            );
            pstmt.setInt(1, codigoAluno);

            // Executa a consulta
            ResultSet resultadoConsulta = pstmt.executeQuery();
            // Verifica se existe pelo menos um registro na consulta
            if (resultadoConsulta.next()) {
                codigoConversa = resultadoConsulta.getInt("cd_conversa");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return codigoConversa;
    }

    public static int InsertConversa(Connection connectionBD, int codigoAluno) {
        int codigoConversaInserida = 0;

        try {
            // Prepara o comando SQL de inserção com retorno de chave gerada
            PreparedStatement pstmt = connectionBD.prepareStatement(
                "INSERT INTO conversas (id_aluno) " +
                "VALUES (?)",
                PreparedStatement.RETURN_GENERATED_KEYS
            );
            pstmt.setInt(1, codigoAluno);

            // Executa a inserção
            int insercaoBemSucedida = pstmt.executeUpdate();
            if (insercaoBemSucedida == 1) {
                // Obtém a chave(cd_conversa) gerada
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    codigoConversaInserida = generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return codigoConversaInserida;
    }
}