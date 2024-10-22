package com.mycompany.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBase {

    public static void main(String[] args) {
        Connection connection = null;

        try {
            // Cria a conexão com o banco de dados SQLite
            connection = DriverManager.getConnection("jdbc:sqlite:edusiga.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // Espera até 30 segundos para conectar

            // Cria e manipula a tabela 'alunos'
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS alunos (cd_aluno INTEGER PRIMARY KEY, cpf_aluno VARCHAR(11) NOT NULL, is_aluno_ativo TINYINT(1) NOT NULL)");
            
            // Teste capturar aluno específico
            SelectAluno(connection, statement, "57822308866");
            InsertAluno(connection, statement, "51392175895");
            //UpdateStatusAluno(connection, statement, "57822308866");
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
