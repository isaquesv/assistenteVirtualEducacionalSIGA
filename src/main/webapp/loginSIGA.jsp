<%-- 
    Document   : loginSIGA
    Created on : 25 de set. de 2024, 00:11:59
    Author     : Liryou
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-br">
    <head>
        <title>Login SIGA</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <%@include file="WEB-INF/jspf/html-head-libs.jspf" %>
        <link rel="stylesheet" href="style/loginSIGA.css">
    </head>
    <body>
        <%@include file="WEB-INF/jspf/navbar.jspf" %>

        <main class="content" style="margin-top: 0;">    
            <div class="container">

                <div class="quadrado ms-auto me-auto pt-0">
                    <h1>Login SIGA</h1>
                    <h2>Faça o login com o SIGA agora e inicie a conversa com o EduSIGA!</h2>
                    <div>
                        <label for="usuarioLoginSIGA">Usuário</label>
                        <input type="text" id="usuarioLoginSIGA" name="usuarioLogin" placeholder="Digite seu usuário">
                    </div>

                    <div>
                        <label for="senhaLoginSIGA">Senha</label>
                        <input type="password" id="senhaLoginSIGA" name="senhaLogin" placeholder="Digite sua senha">

                        <button id="mostrarSenha">
                            <i id="iconeSenha" class="bi bi-eye"></i>
                        </button>
                    </div>

                    <input type="submit" id="confirmarLoginSIGA" name="confirmarLoginSIGA" title="Confirmar acesso" value="Entrar">
                    
                    <div id="notificacoesErros">
                        <span id="spanMensagemErroUsuario"></span>
                        <span id="spanMensagemErroSenha"></span>
                        <span id="spanMensagemErroLogin"></span>
                    </div>
                </div>
            </div>
        </main>

        <div class="modal-background" id="loadingModal">
            <div class="loader-container">
                <div class="loader"></div>
                <div id="loading-text" class="mt-1 fw-bold text-white" style="font-size: 14px;"></div>
            </div>
        </div>

        <%@include file="WEB-INF/jspf/footer.jspf" %>
        <%@include file="WEB-INF/jspf/html-body-libs.jspf" %>
        <script src="scripts/loginSIGA.js"></script>
    </body>
</html>
