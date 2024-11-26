package liryou.assistentevirtualeducacionalsiga.classes.database.consultasSQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONObject;

public interface Unidades_Ensino {
   
    public static int SelectUnidadesEnsinoContagemRegistros(Connection connectionBD) {
        int quantidadeTotalRegistros = 0;
        
        try {
            // Prepara o comando SQL de seleção
            PreparedStatement pstmt = connectionBD.prepareStatement(
                "SELECT COUNT(*) AS quantidadeTotalRegistros " +
                "FROM unidades_ensino"
            );
            
            // Executa a consulta
            ResultSet resultadoConsulta = pstmt.executeQuery();
            // Se existir ao menos 1 registro
            if (resultadoConsulta.next() == true) {
                quantidadeTotalRegistros = resultadoConsulta.getInt("quantidadeTotalRegistros");
            }     
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        return quantidadeTotalRegistros;
    }
    
    public static int SelectUnidadeEnsinoCodigo(Connection connectionBD, String nomeUnidadeEnsino) {
        int codigoUnidadeEnsino = 0;
        
        try {
            // Prepara o comando SQL de seleção
            PreparedStatement pstmt = connectionBD.prepareStatement(
                "SELECT cd_unidade_ensino " +
                "FROM unidades_ensino " +
                "WHERE nm_unidade_ensino = ?"
            );
            // Define o valor do parâmetro
            pstmt.setString(1, nomeUnidadeEnsino);
            
            // Executa a consulta
            ResultSet resultadoConsulta = pstmt.executeQuery();
            // Se existir ao menos 1 registro
            if (resultadoConsulta.next() == true) {
                codigoUnidadeEnsino = resultadoConsulta.getInt("cd_unidade_ensino");
            }     
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        return codigoUnidadeEnsino;
    }
    
    public static JSONObject SelectUnidadeEnsino(Connection connectionBD, String nomeUnidadeEnsino) {
        // Object JSON para armazenar as informações da unidade de ensino
        JSONObject listaInformacoesUnidadeEnsino = new JSONObject();

        try {
            // Prepara o comando SQL de seleção
            PreparedStatement pstmt = connectionBD.prepareStatement(
                "SELECT * " +
                "FROM unidades_ensino " +
                "WHERE nm_unidade_ensino = ?"
            );
            // Define o valor do parâmetro
            pstmt.setString(1, nomeUnidadeEnsino);
            
            // Executa a consulta
            ResultSet resultadoConsulta = pstmt.executeQuery();
            if (resultadoConsulta.next() == true) {
                String codigoUnidadeEnsino = Integer.toString(resultadoConsulta.getInt("cd_unidade_ensino"));
                String enderecoUnidadeEnsino = resultadoConsulta.getString("ds_endereco_unidade_ensino");
                String descricaoUnidadeEnsino = resultadoConsulta.getString("ds_unidade_ensino");
                    
                // Adiciona as informações da unidade de ensino ao JSON Object
                listaInformacoesUnidadeEnsino.put("codigo_unidade_ensino", codigoUnidadeEnsino);
                listaInformacoesUnidadeEnsino.put("nome_unidade_ensino", nomeUnidadeEnsino);
                listaInformacoesUnidadeEnsino.put("endereco_unidade_ensino", enderecoUnidadeEnsino);
                listaInformacoesUnidadeEnsino.put("descricao_unidade_ensino", descricaoUnidadeEnsino);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        return listaInformacoesUnidadeEnsino;
    }

    public static int InsertUnidadeEnsino(Connection connectionBD, String nomeUnidadeEnsino, String enderecoUnidadeEnsino, String descricaoUnidadeEnsino) {
        int codigoUnidadeEnsino = 0;
        
        try {
            // Prepara o comando SQL de inserção
            PreparedStatement pstmt = connectionBD.prepareStatement(
                "INSERT INTO unidades_ensino (nm_unidade_ensino, ds_endereco_unidade_ensino, ds_unidade_ensino) " +
                "VALUES (?, ?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS
            );
            // Define os valores dos parâmetros
            pstmt.setString(1, nomeUnidadeEnsino);
            pstmt.setString(2, enderecoUnidadeEnsino);
            pstmt.setString(3, descricaoUnidadeEnsino);
            
            // Executa a inserção
            int insercaoBemSucedida = pstmt.executeUpdate();
            if (insercaoBemSucedida == 1) {
                // Obtém a chave(cd_unidade_ensino) gerada
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    codigoUnidadeEnsino = generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        return codigoUnidadeEnsino;
    }
}