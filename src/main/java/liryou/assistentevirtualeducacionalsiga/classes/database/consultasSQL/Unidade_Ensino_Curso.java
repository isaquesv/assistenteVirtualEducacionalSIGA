package liryou.assistentevirtualeducacionalsiga.classes.database.consultasSQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public interface Unidade_Ensino_Curso {

    public static void InsertUnidadeEnsinoCurso(Connection connection, int idUnidadeEnsino, int idCurso) {
        try {
            // Prepara o comando SQL de inserção
            PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO UNIDADES_ENSINO_CURSOS (id_unidade_ensino, id_curso) VALUES (?, ?)"
            );
            // Define os valores dos parâmetros
            pstmt.setInt(1, idUnidadeEnsino);
            pstmt.setInt(2, idCurso);
            
            // Executa a inserção
            pstmt.executeUpdate();
            System.out.println("Unidade de ensino e curso inseridos com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao inserir na tabela UNIDADES_ENSINO_CURSOS: " + e.getMessage());
        }
    }
}


