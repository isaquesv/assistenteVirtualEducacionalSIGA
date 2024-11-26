package liryou.assistentevirtualeducacionalsiga.classes.database.consultasSQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface Unidades_Ensino_Cursos {

    public static int SelectUnidadesEnsinoCursosContagemRegistros(Connection connectionBD, int codigoUnidadeEnsino) {
        int quantidadeTotalRegistros = 0;
        
        try {
            // Prepara o comando SQL de seleção
            PreparedStatement pstmt = connectionBD.prepareStatement(
                "SELECT COUNT(*) AS quantidadeTotalRegistros " +
                "FROM unidades_ensino_cursos " +
                "WHERE id_unidade_ensino = ?"
            );
            // Define os valores dos parâmetros
            pstmt.setInt(1, codigoUnidadeEnsino);
            
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
    
    public static boolean InsertUnidadeEnsinoCurso(Connection connectionBD, int codigoUnidadeEnsino, int codigoCurso) {
        boolean unidadeEnsinoCursoInseridaComSucesso = false;
        
        try {
            // Prepara o comando SQL de inserção
            PreparedStatement pstmt = connectionBD.prepareStatement(
                "INSERT INTO unidades_ensino_cursos (id_unidade_ensino, id_curso) " +
                "VALUES (?, ?)"
            );
            // Define os valores dos parâmetros
            pstmt.setInt(1, codigoUnidadeEnsino);
            pstmt.setInt(2, codigoCurso);
            
            // Executa a inserção
            int insercaoBemSucedida = pstmt.executeUpdate();
            if (insercaoBemSucedida == 1) {
                unidadeEnsinoCursoInseridaComSucesso = true;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        return unidadeEnsinoCursoInseridaComSucesso;
    }
}