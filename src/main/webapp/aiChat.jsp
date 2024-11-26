<%--
    Document   : aiChat
    Created on : 25 de set. de 2024, 00:12:54
    Author     : Liryou
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-br">
    <head>
        <title>Chat IA</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <%@include file="WEB-INF/jspf/html-head-libs.jspf" %>
        <link rel="stylesheet" href="style/aiChat.css">
    </head>
    <body>
        <%@include file="WEB-INF/jspf/navbar.jspf" %>

        <main class="content">    
            <div class="container d-flex h-100">
                <div id="chatBackground" class="mt-2 mb-2 me-auto ms-auto p-0">
                    <header>
                        <button id="excluirConversa" data-bs-toggle="modal" data-bs-target="#modalExcluirConversa">Excluir conversa</button>
                    </header>
                    <main id="chat" class="p-3">

                    </main>
                    <footer>
                        <div class="d-flex">
                            <input type="text" class="form-control" id="inputPergunta" placeholder="Digite uma mensagem...">
                            <button id="enviarPergunta"><i class="bi bi-send-fill"></i></i></button>
                        </div>
                    </footer>
                </div>
            </div>
        </main>

        <div class="modal fade" id="modalFinalizarSessao" tabindex="-1" aria-labelledby="modalFinalizarSessaoLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h1 class="modal-title fs-5" id="modalFinalizarSessaoLabel">Finalizar sessão</h1>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        Você deseja finalizar sua sessão?
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="botaoNaoFinalizarSessao" data-bs-dismiss="modal">Não</button>
                        <button type="button" class="botaoSimFinalizarSessao" id="finalizarSessao">Finalizar</button>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="modalExcluirConversa" tabindex="-1" aria-labelledby="modalExcluirConversaLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h1 class="modal-title" id="modalExcluirConversaLabel">Excluir conversa</h1>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        Você deseja deletar <b>TODAS</b> as suas mensagens registradas nesta conversa?
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="botaoNaoExcluirConversa" data-bs-dismiss="modal">Não</button>
                        <button type="button" class="botaoSimExcluirConversa" id="deletarConversa">Deletar</button>
                    </div>
                </div>
            </div>
        </div>

        <%@include file="WEB-INF/jspf/footer.jspf" %>
        <%@include file="WEB-INF/jspf/html-body-libs.jspf" %>
        <script src="scripts/aiChat.js"></script>
    </body>
</html>
