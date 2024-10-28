package DataBase.tabelas;

import DataBase.consultasSQL.Unidade_Ensino;
import DataBase.consultasSQL.Alunos;
import DataBase.consultasSQL.Cursos;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Tabelas implements  Alunos, Unidade_Ensino, Cursos{

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
            // Cria tabela Cursos
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS cursos (" + "cd_curso INTEGER PRIMARY KEY, " + "nm_curso VARCHAR(100) NOT NULL, " +
                                    "sg_curso VARCHAR(10), " + "ds_curso VARCHAR(2700))");
            // Cria tabela Unidade_Ensino_Curso
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS UNIDADES_ENSINO_CURSOS (" + "id_unidade_ensino INTEGER NOT NULL, " + "id_curso INTEGER NOT NULL, " +
                                   "PRIMARY KEY (id_unidade_ensino, id_curso)" + ")");     
            // Cria tabela Conversas
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS CONVERSAS (" + "cd_conversa INTEGER PRIMARY KEY, " + "id_aluno INTEGER NOT NULL" + ")");
            // Cria tabela Mensagens
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS MENSAGENS (" + "cd_mensagem INTEGER PRIMARY KEY, " + "id_conversa INTEGER NOT NULL, " +
                            "id_aluno INTEGER NOT NULL, " + "ds_mensagem TEXT, " + "is_enviada_por_ia TINYINT(1) NOT NULL, " + "is_mensagem_ativa TINYINT(1) NOT NULL, " +
                            "dt_criacao_mensagem DATE NOT NULL" + ")");
                    
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
    
    
