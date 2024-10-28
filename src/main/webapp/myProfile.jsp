<%-- 
    Document   : myProfile
    Created on : 25 de set. de 2024, 00:13:40
    Author     : Liryou
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-br">
    <head>
        <%@include file="WEB-INF/jspf/head.jspf" %>
        <title>Meu perfil</title>
    </head>
    <body>
        <%
            String isUsuarioLogado = (String) session.getAttribute("isUsuarioLogado");
            
            //  Se o usuário não estiver logado no site redireciona para index
            if (isUsuarioLogado == null) {
                //  Redireciona o usuário para 'index.jsp'
                response.sendRedirect("index.jsp");
            }
        %>
        
         <%@include file="WEB-INF/jspf/navbar.jspf" %>
        
        <main class="content">
            <div class="container">
                <h2>Bem-vindo ao nosso site!</h2>
                <p>HOME</p>
            </div>
        </main>
        
        <%@include file="WEB-INF/jspf/footer.jspf" %>
    </body>
    
    <%-- Importando a biblioteca JS do Bootstrap para utilizar determinadas ações --%>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
    <%-- Script --%>
    <script src="scripts/script.js"></script>
</html>
