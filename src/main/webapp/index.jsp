<%-- 
    Document   : index
    Created on : 25 de set. de 2024, 00:07:38
    Author     : Liryou
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-br">
    <head>
        <%@include file="WEB-INF/jspf/head.jspf" %>
        <title>Home</title>
        <meta name="description" content="Este projeto tem o objetivo de desenvolver um sistema integrado com a IA do Google (Gemini) responsável por oferecer suporte aos alunos que utilizam do 'SIGA', ajudando com o aprendizado e respondendo dúvidas atreladas às informações acadêmicas do aluno.">
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
                
            </div>
        </main>
        
        <%@include file="WEB-INF/jspf/footer.jspf" %>
    </body>
    
    <%-- Importando a biblioteca JS do Bootstrap para utilizar determinadas ações --%>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
    <%-- Script --%>
    <script src="scripts/script.js"></script>
</html>
