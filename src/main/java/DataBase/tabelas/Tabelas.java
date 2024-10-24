package DataBase.tabelas;

import DataBase.entidades.Unidade_Ensino;
import DataBase.entidades.Alunos;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Tabelas implements Alunos, Unidade_Ensino{

    public static void main(String[] args) {
        Connection connection = null;

        try {
            // Cria a conexão com o banco de dados SQLite
            connection = DriverManager.getConnection("jdbc:sqlite:edusiga.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // Espera até 30 segundos para conectar

            // Cria tabela Alunos
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS alunos (cd_aluno INTEGER PRIMARY KEY, cpf_aluno VARCHAR(11) NOT NULL, is_aluno_ativo TINYINT(1) NOT NULL)");
            // Cria tabela Unidades_Ensino
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS unidades_ensino (" + "cd_unidade_ensino INTEGER PRIMARY KEY, " + "nm_unidade_ensino VARCHAR(100) NOT NULL, " +
                                    "ds_endereco_unidade_ensino VARCHAR(255), " + "ds_unidade_ensino VARCHAR(3500))");
            
        } catch (SQLException e) {
            // Erro de conexão ou execução
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
    
    
