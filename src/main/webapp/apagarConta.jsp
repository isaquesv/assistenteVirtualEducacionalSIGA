<%-- 
    Document   : apagarConta
    Created on : 14 de nov. de 2024, 12:35:37
    Author     : Liryou
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-br">
    <head>
        <title>Apagar conta</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <%@include file="WEB-INF/jspf/html-head-libs.jspf" %>
        <link rel="stylesheet" href="style/apagarConta.css">
    </head>
    <body>
        <%@include file="WEB-INF/jspf/navbar.jspf" %>

        <main class="content"> 
            <div class="apagarConta ms-auto me-auto pt-0">
                <h1>Apagar conta?</h1>
                <p>
                    Apagar a conta no EduSIGA significará a exclusão de todos os seus dados, incluindo o login salvo e as conversas com o EduSiga.
                    Deseja apagar?
                </p>
                <h3>Este processo é irreversível.</h3>
                <button type="button" class="deleteButton" data-bs-toggle="modal" data-bs-target="#modalExcluirConta">Apagar</button>
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

        <div class="modal fade" id="modalExcluirConta" tabindex="-1" aria-labelledby="modalExcluirContaLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h1 class="modal-title" id="modalExcluirContaLabel">Excluir conta</h1>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        Confirme novamente para apagar.
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="botaoNaoApagarConta" data-bs-dismiss="modal">Não</button>
                        <button type="button" class="botaoSimApagarConta" id="excluirConta">Apagar</button>
                    </div>
                </div>
            </div>
        </div>

        <%@include file="WEB-INF/jspf/footer.jspf" %>
        <%@include file="WEB-INF/jspf/html-body-libs.jspf" %>
        <script src="scripts/apagarConta.js"></script>
    </body>
</html>
