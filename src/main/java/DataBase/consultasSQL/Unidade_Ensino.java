package DataBase.consultasSQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public interface Unidade_Ensino {
   
    public static void SelectUnidadeEnsino(Connection connection, Statement statement, int codigo) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                "SELECT * FROM unidades_ensino WHERE cd_unidade_ensino = ?"
            );
            pstmt.setInt(1, codigo);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("Unidade de Ensino Encontrada: " + rs.getString("nm_unidade_ensino"));
            } else {
                System.out.println("Unidade de Ensino não encontrada.");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void InsertUnidadeEnsino(Connection connection, Statement statement, String nome, String endereco, String descricao) {
        try {
            statement.executeUpdate("INSERT INTO unidades_ensino (cd_unidade_ensino, nm_unidade_ensino, ds_endereco_unidade_ensino, ds_unidade_ensino) " +
                "VALUES(null, '" + nome + "', '" + endereco + "', '" + descricao + "')");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}

