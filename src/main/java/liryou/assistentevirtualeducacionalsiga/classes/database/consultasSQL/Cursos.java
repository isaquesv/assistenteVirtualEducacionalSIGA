package liryou.assistentevirtualeducacionalsiga.classes.database.consultasSQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

public interface Cursos {
    public static int SelectCursosContagemRegistros(Connection connectionBD) {
        int quantidadeTotalRegistros = 0;
        
        try {
            // Prepara o comando SQL de seleção
            PreparedStatement pstmt = connectionBD.prepareStatement(
                "SELECT COUNT(*) AS quantidadeTotalRegistros " +
                "FROM cursos"
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
    
    public static JSONArray SelectCursos(Connection connectionBD, int codigoUnidadeEnsino) {
        // Array JSON para armazenar as informações dos cursos
        JSONArray listaInformacoesCursos = new JSONArray();

        try {
            // Prepara o comando SQL de seleção
            PreparedStatement pstmt = connectionBD.prepareStatement(
                "SELECT * " +
                "FROM cursos " +
                "INNER JOIN unidades_ensino_cursos ON unidades_ensino_cursos.id_curso = cursos.cd_curso " +
                "WHERE unidades_ensino_cursos.id_unidade_ensino = ?"
            );
            pstmt.setInt(1, codigoUnidadeEnsino);
                
            // Executa a consulta
            ResultSet resultadoConsulta = pstmt.executeQuery();
            // Percorre todos os resultados e armazena as informações dos cursos na lista
            while (resultadoConsulta.next()) {
                String codigoCurso = Integer.toString(resultadoConsulta.getInt("cd_curso"));
                String nomeCurso = resultadoConsulta.getString("nm_curso");
                String siglaCurso = resultadoConsulta.getString("sg_curso");
                String descricaoCurso = resultadoConsulta.getString("ds_curso");
                
                // Adiciona as informações do curso ao array JSON
                listaInformacoesCursos.put(new JSONObject()
                    .put("codigo_curso", codigoCurso)
                    .put("nome_curso", nomeCurso)
                    .put("sigla_curso", siglaCurso)
                    .put("descricao_curso", descricaoCurso)
                );
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        return listaInformacoesCursos;
    }

    public static int InsertCurso(Connection connectionBD, String nomeCurso, String siglaCurso, String descricaoCurso) {
        int codigoCurso = 0;
        
        try {
            // Prepara o comando SQL de inserção
            PreparedStatement pstmt = connectionBD.prepareStatement(
                "INSERT INTO cursos (nm_curso, sg_curso, ds_curso) " +
                "VALUES (?, ?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS
            );
            pstmt.setString(1, nomeCurso);
            pstmt.setString(2, siglaCurso);
            pstmt.setString(3, descricaoCurso);

            // Executa a inserção
            int insercaoBemSucedida = pstmt.executeUpdate();
            if (insercaoBemSucedida == 1) {
                // Obtém a chave(cd_curso) gerada
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    codigoCurso = generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        return codigoCurso;
    }
}