package DataBase.consultasSQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public interface Cursos {
     
    public static void SelectCurso(Connection connection, Statement statement, int codigo) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                "SELECT * FROM cursos WHERE cd_curso = ?"
            );
            pstmt.setInt(1, codigo);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("Curso Encontrado: " + rs.getString("nm_curso"));
            } else {
                System.out.println("Curso não encontrado.");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void InsertCurso(Connection connection, Statement statement, String nome, String sigla, String descricao) {
        try {
            statement.executeUpdate("INSERT INTO cursos (cd_curso, nm_curso, sg_curso, ds_curso) " +
                "VALUES(null, '" + nome + "', '" + sigla + "', '" + descricao + "')");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}

