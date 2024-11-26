package liryou.assistentevirtualeducacionalsiga.classes.database.consultasSQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

public interface Matrizes_Curriculares {
    
    public static int SelectMatrizesCurricularesContagemRegistros(Connection connectionBD) {
        int quantidadeTotalRegistros = 0;
        
        try {
            // Prepara o comando SQL de seleção
            PreparedStatement pstmt = connectionBD.prepareStatement(
                "SELECT COUNT(*) AS quantidadeTotalRegistros " +
                "FROM matrizes_curriculares"
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
    
    public static JSONArray SelectMatrizesCurriculares(Connection connectionBD, int codigoCurso) {
        // Array JSON para armazenar as informações da matriz curricular de determinado curso
        JSONArray listaInformacoesMatrizCurricular = new JSONArray();

        try {
            // Prepara o comando SQL de seleção
            PreparedStatement pstmt = connectionBD.prepareStatement(
                "SELECT * " +
                "FROM matrizes_curriculares " +
                "WHERE id_curso = ?"
            );
            // Define o valor do parâmetro
            pstmt.setInt(1, codigoCurso);
            
            // Executa a consulta
            ResultSet resultadoConsulta = pstmt.executeQuery();
            // Percorre todos os resultados e armazena as informações da matriz curricular na lista
            while (resultadoConsulta.next()) {
                String descricaoMatrizCurricular = resultadoConsulta.getString("ds_matriz_curricular");
                    
                // Adiciona as informações da matriz curricular ao array JSON
                listaInformacoesMatrizCurricular.put(new JSONObject()
                    .put("descrição_curso", descricaoMatrizCurricular)   
                );
            } 
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        return listaInformacoesMatrizCurricular;
    }

    public static boolean InsertMatrizCurricular(Connection connectionBD, String descricaoMatrizCurricular, int codigoCurso) {
        boolean matrizCurricularInseridaComSucesso = false;
        
        try {
            // Prepara o comando SQL de inserção
            PreparedStatement pstmt = connectionBD.prepareStatement(
                "INSERT INTO matrizes_curriculares (ds_matriz_curricular, id_curso) " +
                "VALUES (?, ?)"
            );
            // Define os valores dos parâmetros
            pstmt.setString(1, descricaoMatrizCurricular);
            pstmt.setInt(2, codigoCurso);
            
            // Executa a inserção
            int insercaoBemSucedida = pstmt.executeUpdate();
            if (insercaoBemSucedida == 1) {
                matrizCurricularInseridaComSucesso = true;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        return matrizCurricularInseridaComSucesso;
    }
}