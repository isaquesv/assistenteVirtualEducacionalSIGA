<%-- 
    Document   : loginSIGA
    Created on : 25 de set. de 2024, 00:11:59
    Author     : Liryou
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-br">
    <head>
        <%@include file="WEB-INF/jspf/head.jspf" %>
        <title>Login - Sistema SIGA</title>
        <style>
            .modal-background {
                position: fixed;
                display: none;
                align-items: center;
                justify-content: center;
                z-index: 2; /*  Sobrepondo (posicionando acima dos outros elementos) */
                top: 0;
                left: 0;
                right: 0;
                bottom: 0;
                background-color: rgba(0, 0, 0, 0.5);
            }

            .loader {
                border: 4px solid #f3f3f3;
                border-top: 4px solid #3498db;
                border-radius: 50%;
                width: 40px;
                height: 40px;
                animation: spin 1s linear infinite;
            }

            @keyframes spin {
                0% {
                    transform: rotate(0deg);
                }
                100% {
                    transform: rotate(360deg);
                }
            }

            .loader-container {
                display: flex;
                flex-direction: column;
                align-items: center;
            }
        </style>
    </head>
    <body>
        <%
            String isUsuarioLogado = (String) session.getAttribute("isUsuarioLogado");
            String isSIGALogado = (String) session.getAttribute("isSIGALogado");
            
            //  Se o usuário não estiver logado no site redireciona para index
            if (isUsuarioLogado == null) {
                //  Redireciona o usuário para 'index.jsp'
                response.sendRedirect("index.jsp");
            } else {
                //  Se o usuário tiver validado seu SIGA anteriormente redireciona para aiChat
                if (isSIGALogado != null) {
                    //  Redireciona o usuário para 'aiChat.jsp'
                    response.sendRedirect("aiChat.jsp");
                }
            }
        %>
        
        <%@include file="WEB-INF/jspf/navbar.jspf" %>
        
        <main class="content">
            <div class="container">
                <div>
                    <label for="usuarioLoginSIGA">Usuário:</label>
                    <input type="text" id="usuarioLoginSIGA" name="usuarioLogin" placeholder="Digite seu nome de usuário">
                </div>

                <div>
                    <label for="senhaLoginSIGA">Senha:</label>
                    <input type="password" id="senhaLoginSIGA" name="senhaLogin" placeholder="Digite sua senha">
                </div>

                <input type="submit" id="confirmarLoginSIGA" name="confirmarLoginSIGA" title="Confirmar acesso" value="Confirmar">

                <div id="notificacaoErros" style="display: none;">
                    <span style="color: red;">
                        <small id="mensagemErro"></small>
                    </span>
                </div>
            </div>
        </main>
        
        <div class="modal-background" id="loadingModal">
            <div class="loader-container">
                <div class="loader"></div>
                <div class="mt-1 fw-bold text-white loading-text" style="font-size: 14px;">Validando dados...</div>
            </div>
        </div>
        
        <%@include file="WEB-INF/jspf/footer.jspf" %>
    </body>
    
    <%-- Importando a biblioteca jQuery para permitir e facilitar o uso de AJAX --%>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <%-- Importando a biblioteca JS do Bootstrap para utilizar determinadas ações --%>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
    <%-- Script --%>
    <script src="scripts/script.js"></script>
    <script src="scripts/loginSIGA.js"></script>
</html>