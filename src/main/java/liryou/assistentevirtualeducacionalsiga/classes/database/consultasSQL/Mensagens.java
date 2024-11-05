package liryou.assistentevirtualeducacionalsiga.classes.database.consultasSQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public interface Mensagens {

    public static void SelectMensagem(Connection connection, int cdMensagem) {
        try {
            // Prepara o comando SQL de seleção
            PreparedStatement pstmt = connection.prepareStatement(
                "SELECT * FROM MENSAGENS WHERE cd_mensagem = ?"
            );
            // Define o valor do parâmetro
            pstmt.setInt(1, cdMensagem);
            
            // Executa a consulta
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("Mensagem: " + rs.getString("ds_mensagem"));
            } else {
                System.out.println("Mensagem não encontrada.");
            }
        } catch (SQLException e) {
            // Trata possíveis erros de execução
            System.err.println("Erro ao consultar a tabela MENSAGENS: " + e.getMessage());
        }
    }

    public static void InsertMensagem(Connection connection, int cdMensagem, int idConversa, int idAluno, String dsMensagem,
                                      boolean isEnviadaPorIA, boolean isMensagemAtiva, java.sql.Date dtCriacaoMensagem) {
        try {
            // Prepara o comando SQL de inserção
            PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO MENSAGENS (cd_mensagem, id_conversa, id_aluno, ds_mensagem, is_enviada_por_ia, " +
                "is_mensagem_ativa, dt_criacao_mensagem) VALUES (?, ?, ?, ?, ?, ?, ?)"
            );
            // Define os valores dos parâmetros
            pstmt.setInt(1, cdMensagem);
            pstmt.setInt(2, idConversa);
            pstmt.setInt(3, idAluno);
            pstmt.setString(4, dsMensagem);
            pstmt.setBoolean(5, isEnviadaPorIA);
            pstmt.setBoolean(6, isMensagemAtiva);
            pstmt.setDate(7, dtCriacaoMensagem);
            
            // Executa a inserção
            pstmt.executeUpdate();
            System.out.println("Mensagem inserida com sucesso.");
        } catch (SQLException e) {
            // Trata possíveis erros de execução
            System.err.println("Erro ao inserir na tabela MENSAGENS: " + e.getMessage());
        }
    }
}
