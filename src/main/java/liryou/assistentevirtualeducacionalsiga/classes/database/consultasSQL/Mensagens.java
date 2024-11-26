package liryou.assistentevirtualeducacionalsiga.classes.database.consultasSQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

public interface Mensagens {

    public static int SelectMensagensContagemRegistros(Connection connectionBD, int codigoConversa, int codigoAluno) {
        int quantidadeTotalRegistros = 0;
        
        try {
            // Prepara o comando SQL de seleção
            PreparedStatement pstmt = connectionBD.prepareStatement(
                "SELECT COUNT(*) AS quantidadeTotalRegistros " +
                "FROM mensagens " +
                "WHERE id_conversa = ? AND id_aluno = ? AND is_mensagem_ativa = ?"
            );
            // Define o valor do parâmetro
            pstmt.setInt(1, codigoConversa);
            pstmt.setInt(2, codigoAluno);
            pstmt.setInt(3, 1);
            
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
    
    public static JSONArray SelectMensagens(Connection connectionBD, int codigoConversa, int codigoAluno) {
        // Array JSON para armazenar as informações das mensagens
        JSONArray listaInformacoesMensagem = new JSONArray();

        try {
            // Prepara o comando SQL de seleção
            PreparedStatement pstmt = connectionBD.prepareStatement(
                "SELECT * " +
                "FROM mensagens " +
                "WHERE id_conversa = ? AND id_aluno = ? AND is_mensagem_ativa = ?"
            );
            // Define o valor do parâmetro
            pstmt.setInt(1, codigoConversa);
            pstmt.setInt(2, codigoAluno);
            pstmt.setInt(3, 1);
            
            // Executa a consulta
            ResultSet resultadoConsulta = pstmt.executeQuery();
            // Percorre todos os resultados e armazena as informações das mensagens ativas do aluno na lista
            while (resultadoConsulta.next()) {
                String textoMensagem = resultadoConsulta.getString("ds_mensagem");
                int isMensagemEnviadaPorIa = resultadoConsulta.getInt("is_enviada_por_ia");
                    
                // Adiciona as informações da mensagem ao array JSON
                listaInformacoesMensagem.put(new JSONObject()
                    .put("texto_mensagem", textoMensagem)
                    .put("is_mensagem_enviada_por_ia", isMensagemEnviadaPorIa)
                );
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        return listaInformacoesMensagem;
    }

    public static boolean InsertMensagem(Connection connectionBD, int codigoConversa, int codigoAluno, String descricaoMensagem, int isEnviadaPorIA) {
        boolean mensagemInseridaComSucesso = false;
        
        try {
            // Prepara o comando SQL de inserção
            PreparedStatement pstmt = connectionBD.prepareStatement(
                "INSERT INTO mensagens (id_conversa, id_aluno, ds_mensagem, is_enviada_por_ia, is_mensagem_ativa, dt_criacao_mensagem) " +
                "VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)"
            );
            // Define os valores dos parâmetros
            pstmt.setInt(1, codigoConversa);
            pstmt.setInt(2, codigoAluno);
            pstmt.setString(3, descricaoMensagem);
            pstmt.setInt(4, isEnviadaPorIA);
            pstmt.setInt(5, 1);
            
            // Executa a inserção
            int insercaoBemSucedida = pstmt.executeUpdate();
            if (insercaoBemSucedida == 1) {
                mensagemInseridaComSucesso = true;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());   
        }
        
        return mensagemInseridaComSucesso;
    }
    
    public static boolean UpdateDesativarMensagens(Connection connectionBD, int codigoConversa, int codigoAluno){
        boolean mensagensExcluidas = false;
        
        try {
            // Prepara o comando SQL de atualização
            PreparedStatement pstmt = connectionBD.prepareStatement(
                "UPDATE mensagens " +
                "SET is_mensagem_ativa = 0 " +
                "WHERE id_conversa = ? AND id_aluno = ?"
            );
            pstmt.setInt(1, codigoConversa);
            pstmt.setInt(2, codigoAluno);

            // Executa a inserção
            int atualizacaoBemSucedida = pstmt.executeUpdate();
            if (atualizacaoBemSucedida > 0) {
                mensagensExcluidas = true;
            }
        } catch (SQLException e) {
            // Erro de conexão ou execução
            System.err.println(e.getMessage());
        }
        
        return mensagensExcluidas;
    }
}