<%-- 
    Document   : accountLogin
    Created on : 25 de set. de 2024, 00:10:52
    Author     : Liryou
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-br">
    <head>
        <%@include file="WEB-INF/jspf/head.jspf" %>
        <title>Login</title>
    </head>
    <body>
        <%
            String isUsuarioLogado = (String) session.getAttribute("isUsuarioLogado");

            //  Se o usuário estiver logado no site redireciona para loginSIGA
            if (isUsuarioLogado != null) {
                //  Redireciona o usuário para 'loginSIGA.jsp'
                response.sendRedirect("loginSIGA.jsp");
            }
        %>

        <%@include file="WEB-INF/jspf/navbar.jspf" %>

        <main class="content">
            <div class="container">
                <div>
                    <label for="emailUsuarioLogin">Email:</label>
                    <input type="email" id="emailUsuarioLogin" name="emailUsuarioLogin" placeholder="Digite seu email">
                </div>
                <div>
                    <label for="senhaUsuarioLogin">Senha:</label>
                    <input type="password" id="senhaUsuarioLogin" name="senhaUsuarioLogin" placeholder="********">
                </div>

                <input type="submit" id="confirmarLogin" name="confirmarLogin" title="Confirmar acesso" value="Confirmar">

                <div id="notificacaoErros" style="display: none;">
                    <span style="color: red;">
                        <small id="mensagemErro"></small>
                    </span>
                </div>
            </div>
        </main>

        <%@include file="WEB-INF/jspf/footer.jspf" %>
    </body>

    <%-- Importando a biblioteca jQuery para permitir e facilitar o uso de AJAX --%>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <%-- Importando a biblioteca JS do Bootstrap para utilizar determinadas ações --%>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
    <%-- Script --%>
    <script src="scripts/script.js"></script>
    <script src="scripts/accountLogin.js"></script>
</html>