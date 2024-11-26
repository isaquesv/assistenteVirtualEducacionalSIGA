<%-- 
    Document   : index
    Created on : 25 de set. de 2024, 00:07:38
    Author     : Liryou
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-br">
    <head>
        <title>Home</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <%@include file="WEB-INF/jspf/html-head-libs.jspf" %>
        <link rel="stylesheet" href="style/inicio.css">
    </head>
    <body>
        <%@include file="WEB-INF/jspf/navbar.jspf" %>

        <main class="content">    
            <div class="background-rectangle">
                <h1 class="title">Faça <a href="loginSIGA.jsp">login</a> agora e converse com o nosso assistente SIGA</h1>
                <p>
                    O assistente SIGA está aqui para esclarecer suas dúvidas sobre a FATEC de
                    Praia Grande, incluindo informações sobre cursos, disciplinas e professores. 
                    Além disso, ele pode ajudar você a se organizar com suas matérias deste semestre!
                </p>    
            </div>
            <p id="acesse">Acesse o site oficial do <a href="https://siga.cps.sp.gov.br/aluno/login.aspx">SIGA</a> | Acesse o site oficial da <a href="https://www.fatecpg.edu.br/">FATEC</p>
        </main>

        <%@include file="WEB-INF/jspf/footer.jspf" %>
        <%@include file="WEB-INF/jspf/html-body-libs.jspf" %>
    </body>
</html>
