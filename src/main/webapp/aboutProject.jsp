<%-- 
    Document   : aboutProject
    Created on : 25 de set. de 2024, 00:10:52
    Author     : Liryou
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-br">
    <head>
        <title>Sobre o projeto</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <%@include file="WEB-INF/jspf/html-head-libs.jspf" %>
        <link rel="stylesheet" href="style/aboutProject.css">
    </head>
    <body>
        <%@include file="WEB-INF/jspf/navbar.jspf" %>

        <main class="content">    
            <div class="about-project">
                <p>
                    A equipe Liryou é formada por três alunos da Fatec de Praia Grande: <a href="https://github.com/isaquesv/">Isaque Silva</a>, <a href="https://github.com/luiza-carla/">Luiza Carla</a> e <a href="https://github.com/ruann-fernandess/">Ruan Fernandes</a>.
                    Juntos, eles compartilham o interesse por tecnologia e inovação, trabalhando em soluções que facilitam a vida acadêmica de seus colegas. Cada integrante traz habilidades únicas, o que torna o grupo eficiente e focado em desenvolver projetos práticos e úteis para o ambiente acadêmico.
                </p>
                <p>
                    No projeto EduSIGA, a equipe Liryou aplicou seus conhecimentos para criar um Assistente Virtual Educacional integrado ao SIGA da Fatec de Praia Grande. Com o apoio da inteligência artificial Gemini do Google, o EduSIGA é projetado para oferecer suporte acadêmico e esclarecer dúvidas dos alunos,
                    tornando o uso do SIGA mais acessível e eficiente. A equipe trabalhou para garantir que o EduSIGA atenda às necessidades reais dos estudantes da Fatec, facilitando o acesso a informações importantes e auxiliando no aprendizado de forma prática e ágil.
                </p>
                <img src="img/lily-rosaEditada.png" id="lirio-rosa" alt="Lírio rosa">
            </div>
        </main>

        <%
            if (isSIGALogado != null) {
                %>
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
                <%
            }
        %>
        
        <%@include file="WEB-INF/jspf/footer.jspf" %>
        <%@include file="WEB-INF/jspf/html-body-libs.jspf" %>
    </body>
</html>
