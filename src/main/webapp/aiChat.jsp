<%-- 
    Document   : aiChat
    Created on : 25 de set. de 2024, 00:12:54
    Author     : Liryou
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-br">
    <head>
        <%@include file="WEB-INF/jspf/head.jspf" %>
        <title>Chat IA</title>
        <link rel="stylesheet" href="style/aiChatCSS.css">
    </head>
    <body>
        <%
            String isUsuarioLogado = (String) session.getAttribute("isUsuarioLogado");
            String isSIGALogado = (String) session.getAttribute("isSIGALogado");
            String contextoInicial = (String) session.getAttribute("contextoInicial");
            
            //  Se o usuário não estiver logado no site redireciona para index
            if (isUsuarioLogado == null) {
                //  Redireciona o usuário para 'index.jsp'
                response.sendRedirect("index.jsp");
            } else {
                //  Se o usuário não tiver validado seu SIGA anteriormente redireciona para loginSIGA
                if (isSIGALogado == null) {
                    //  Redireciona o usuário para 'loginSIGA.jsp'
                    response.sendRedirect("loginSIGA.jsp");
                }
            }
        %>

        <%@include file="WEB-INF/jspf/navbar.jspf" %>
        
        <main class="content">
            <div class="bg-success container">
                <div id="teste" class="bg-dark mt-2 mb-2 me-auto ms-auto p-0" style="width: 754px; display: flex; flex-direction: column;">
                    <header>
                        <button id="finalizarSessao">Finalizar sessão</button>
                    </header>
                    <main class="chat-history p-3">
                        
                    </main>
                    <footer>
                        <div class="d-flex">
                            <input type="text" class="form-control" id="inputPergunta" placeholder="Digite uma mensagem..">
                            <button id="enviarPergunta"><img src="img/sendIcon.svg"></button>
                        </div>
                    </footer>
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
    <script type="importmap">
        {
            "imports": {
                "@google/generative-ai": "https://esm.run/@google/generative-ai"
            }
        }
    </script>
    <script type="module">
        import { GoogleGenerativeAI } from "@google/generative-ai";

        //  Acessando a chave da API
        const API_KEY = "AIzaSyAoOT4y7xZ-ygRaU3VGLAuChKSzw6mOOY8";
        const genAI = new GoogleGenerativeAI(API_KEY);

        //  Iniciando o modelo de chat
        const model = genAI.getGenerativeModel({model: "gemini-1.0-pro"});
        let chat;

        async function initChat() {
            chat = model.startChat({
                history: [
                    /*
                     LAÇO DE REPETIÇÃO PARA TODAS AS MENSAGENS ANTERIORES CADASTRADAS NO BANCO DE DADOS
                     
                     int contadorMensagensAnteriores = 10;
                     String role = null;
                     for (int i = 0; i < contadorMensagensAnteriores; i++) {
                     if (autorMensagemAtual === "USUARIO") {
                     role = "user";
                     } else {
                     role = "model";
                     }
                                     
                     */
                    {
                        role: "user",
                        parts: [
                            {text: "<%= contextoInicial %>"},
                            {text: "Endereço da faculdade: Praça 19 de Janeiro, 144 - Boqueirão. CEP: 11700-100. Cidade: Praia Grande. Estado: São Paulo (Baixada Santista)"},
                            {text: "Descrição sobre a faculdade: Descrição histórica: A Faculdade de Tecnologia de Praia Grande iniciou suas atividades em 3 de setembro de 2002, com 80 vagas para o curso de Tecnologia em Informática com Ênfase em Gestão de Negócios, divididas entre os turnos vespertino e noturno. Em 11 de março de 2003, começou a funcionar a Escola Técnica Estadual “Adolfo Berezin” – Extensão Praia Grande, com 120 vagas para os cursos Técnico em Informática e Técnico em Logística. Ainda em março de 2003, em parceria com o IPEN/CEETEPS, iniciou-se a oferta de cursos de pós-graduação lato sensu, como Gestão Empresarial e Consultoria Web. Em 2 de março de 2006, a unidade de Praia Grande deixou de ser extensão da Faculdade de Tecnologia da Baixada Santista, tornando-se oficialmente a Faculdade de Tecnologia de Praia Grande. Em fevereiro de 2009, começou o curso de graduação em Tecnologia em Comércio Exterior, e em 2010, o curso de Tecnologia em Informática para Gestão de Negócios foi reestruturado, resultando nos cursos de Análise e Desenvolvimento de Sistemas e Gestão Empresarial. No primeiro semestre de 2011, esses cursos, junto ao de Processos Químicos, passaram a ser oferecidos na Fatec Praia Grande. Com isso, a instituição passou a oferecer cursos nos níveis tecnológico, de graduação e de pós-graduação, atendendo cerca de 1.000 alunos nos períodos manhã, tarde e noite. A Fatec Praia Grande tem como missão formar profissionais competentes e éticos para enfrentar desafios tecnológicos, sociais e econômicos, com o slogan “Compromisso e Respeito com o Aluno e a Comunidade”. Além disso, oferece cursos de Verão e Inverno para aperfeiçoamento tecnológico e realiza, durante o aniversário da instituição, o Workshop de Tecnologia da Informação, visando a atualização de alunos, professores e a comunidade. Administração da Unidade de Praia Grande: Direção - Profª. Meª. Viviam Ester de Souza; Assistente Técnico Administrativo - Vanusa Santos de Barros; Diretoria de Serviços Administrativos - Rosângela Cândida da Costa; Assistente Administrativo - Kleber do Carmo Cesar do Nascimento; Diretora de Serviços Acadêmicos - Denise Silva de Souza Vilar. Telefone: (13) 3591-1303 / 3591-6968. E-mails: Diretoria: f129dir@cps.sp.gov.br, Diretoria Administrativa: f129adm@cps.sp.gov.br, Diretoria Acadêmica: f129acad@cps.sp.gov.br. Site: http://www.fatecpg.com.br/"},
                            {text: "Cursos oferecidos: Análise e Desenvolvimento de Sistemas (ADS): A matemática, especialmente raciocínio lógico e cálculo, é fundamental para o aluno otimizar computadores e desenvolver softwares. O curso abrange Bancos de Dados, sistemas web e programação distribuída. Além de disciplinas de Administração, Contabilidade, Economia, Estatística e Inglês, também desenvolve habilidades em leitura e interpretação de textos. Duração: 3 anos (6 semestres). Períodos: Vespertino e Noturno. Coordenador: Prof.º Nilson Carlos Duarte da Silva. Tipo: Curso Superior de Tecnologia Presencial. Eixo: Informação e Comunicação. O tecnólogo projeta, desenvolve e audita sistemas, além de prestar consultoria e pesquisa. Atua em empresas públicas, privadas e instituições financeiras. Comércio Exterior (COMEX): O curso tem como base Administração, Economia e Comunicação, além de disciplinas como matemática, gestão financeira, logística e idiomas (Inglês e Espanhol). Estuda-se também blocos econômicos, logística internacional, exportação e importação, marketing internacional, entre outros. Duração: 3 anos (6 semestres). Períodos: Matutino e Noturno. Coordenador: Prof. Me. Ulysses C. C. Diegues. Tipo: Curso Superior de Tecnologia Presencial. Eixo: Gestão e Negócios. O tecnólogo gerencia operações de comércio exterior, negociações internacionais e documentação. Pode atuar em empresas nacionais, multinacionais, consultorias e órgãos públicos. Desenvolvimento de Software Multiplataforma (DSM): O curso abrange disciplinas como lógica de programação, programação para desktop, dispositivos móveis e web, além de computação em nuvem, inteligência artificial, segurança da informação e internet das coisas. Duração: 3 anos (6 semestres). Período: Vespertino. Coordenador: Profº. Dr. Gilmar Aquino. Tipo: Curso Superior de Tecnologia Presencial. Eixo: Informação e Comunicação. O tecnólogo projeta, desenvolve e testa softwares multiplataforma, oferecendo soluções tecnológicas baseadas em linguagens de programação, banco de dados e IA. Pode atuar em empresas de diversos setores ou empreender. Gestão Empresarial (GE): Com bases em Contabilidade, Economia e Administração, o curso inclui Direito Tributário, logística, gestão ambiental e planejamento estratégico. Também é oferecido na modalidade a distância. Duração: 3 anos (6 semestres). Períodos: Matutino e Noturno. Coordenador: Prof.º João Carlos Gomes. Tipo: Curso Superior de Tecnologia Presencial e a Distância. Eixo: Gestão e Negócios. O tecnólogo atua na organização de trabalho, gestão de pessoas e controle de atividades. Trabalha principalmente em pequenas e médias empresas, podendo também empreender. Processos Químicos (PQ): O curso inclui disciplinas de química, cálculo, física, microbiologia e processos químicos industriais. Também aborda gestão ambiental e tratamento de efluentes. Duração: 3 anos (6 semestres). Períodos: Matutino e Noturno. Coordenador: Prof.º Waldemar Alves Ribeiro Filho. Tipo: Curso Superior de Tecnologia Presencial. Eixo: Controle e Processos Industriais. O tecnólogo gerencia processos laboratoriais e industriais, com foco em qualidade e sustentabilidade. Atua em indústrias químicas, petroquímicas, eletroquímicas e farmoquímicas."}
                        ]
                    },
                    {
                        role: "model",
                        parts: [
                            {text: "Função principal: Você é um chatbot de IA que ajuda os usuários com suas dúvidas, problemas e solicitações. Seu objetivo é fornecer respostas excelentes, amigáveis ​​e eficientes em todos os momentos. Seu papel é ouvir atentamente o usuário, entender suas necessidades e fazer o possível para atendê-lo ou direcioná-lo aos recursos adequados. Se uma pergunta não estiver clara, faça perguntas esclarecedoras. Certifique-se de terminar suas respostas com uma nota positiva."},
                            {text: "Caso você liste uma série de respostas separadas por vírgulas, formate-as para que cada item apareça em uma nova linha, substituindo a vírgula e o espaço por uma quebra de linha (<br>). Isso torna a lista mais legível para o usuário."},
                            {text: "Se você deseja destacar uma determinada parte da resposta, utilize o negrito envolvendo a seção específica entre as tags <b> e </b>. Isso fará com que o texto dentro dessas tags seja exibido em negrito, chamando a atenção do usuário para essa parte específica da resposta."}
                        ]
                    }
                ]
            });
        }

        document.addEventListener("DOMContentLoaded", function () {
            let teste = document.querySelector("header.navbar").clientHeight + document.querySelector("footer.footer").clientHeight;
            document.querySelector("#teste").style.height = "calc(100vh - " + teste + "px)";

            //  Iniciando o chat quando a página é carregada
            initChat();

            document.getElementById("inputPergunta").addEventListener("keypress", function (event) {
                // Quando a tecla "Enter" for pressionada enquanto a caixa de mensagem estiver em foco
                if (event.key === "Enter") {
                    enviarMensagem();
                }
            });

            document.querySelector("#enviarPergunta").addEventListener("click", enviarMensagem);
            document.querySelector("#finalizarSessao").addEventListener("click", finalizarSessao);
        });

        //  Função responsável pelo envio de uma mensagem
        async function enviarMensagem() {
            const inputElement = document.getElementById("inputPergunta");
            const msg = inputElement.value.trim();

            if (msg.trim() === "") {
                //  Impedindo o envio de mensagens vazias
                return;
            }

            //  Resetando a caixa de mensagem após o envio
            inputElement.value = "";

            //  Gerando e exibindo a pergunta do usuário
            gerarElementoPergunta(msg);

            //  Enviando a mensagem para o chat
            const result = await chat.sendMessage(msg); //  Solicitando e aguardando o envio da mensagem para o chat
            const response = await result.response; //  Solicitando e aguardando a resposta da mensagem
            const text = response.text(); //  Recebendo a resposta da mensagem

            //  Gerando e exibindo a resposta do Gemini
            gerarElementoResposta(text);
        }

        //  Função responsável por gerar o "balão" de pergunta do usuário
        function gerarElementoPergunta(perguntaDoUsuario) {            
            //  Verificando se o texto contém "*"
            if (/\*/.test(perguntaDoUsuario)) {
                //  Substituindo de "*" por "<br>"
                perguntaDoUsuario = perguntaDoUsuario.replace(/\* /g, "<br>");
            }
            // Verificando se o texto contém " - "
            if (/\ - /.test(perguntaDoUsuario)) {
                // Substituindo de " - " por "<br>"
                perguntaDoUsuario = perguntaDoUsuario.replace(/\ - /g, "<br>");
            }
            //  Verificando se o texto contém "**"
            if (/\*\*/.test(perguntaDoUsuario)) {
                //  Substituindo de "**" por "<b>" e "</b>"
                perguntaDoUsuario = perguntaDoUsuario.replace(/\*\*(.*?)\*\*/g, "<b>$1</b>");
            }
            
            var divPergunta = document.createElement("div");
            divPergunta.classList.add("message-container", "message-question", "ms-auto");

            var divMessageArrow = document.createElement("div");
            divMessageArrow.classList.add("message-arrow");

            var pMessageContent = document.createElement("p");
            pMessageContent.classList.add("message-content");
            pMessageContent.innerHTML = "<b>Você: </b><br>" + perguntaDoUsuario;

            divPergunta.appendChild(divMessageArrow);
            divPergunta.appendChild(pMessageContent);
            document.querySelector("main.chat-history").appendChild(divPergunta);

            //  Enviando o scroll da conversa para baixo
            document.querySelector("main.chat-history").scrollTop = document.querySelector("main.chat-history").scrollHeight;
        }

        //  Função responsável por gerar o "balão" de resposta do Gemini
        function gerarElementoResposta(respostaDoGemini) {
            //  Verificando se o texto contém "*"
            if (/\*/.test(respostaDoGemini)) {
                //  Substituindo de "*" por "<br>"
                respostaDoGemini = respostaDoGemini.replace(/\* /g, "<br>");
            }
            // Verificando se o texto contém " - "
            if (/\ - /.test(respostaDoGemini)) {
                // Substituindo de " - " por "<br>"
                respostaDoGemini = respostaDoGemini.replace(/\ - /g, "<br>");
            }
            //  Verificando se o texto contém "**"
            if (/\*\*/.test(respostaDoGemini)) {
                //  Substituindo de "**" por "<b>" e "</b>"
                respostaDoGemini = respostaDoGemini.replace(/\*\*(.*?)\*\*/g, "<b>$1</b>");
            }
            
            var divResposta = document.createElement("div");
            divResposta.classList.add("message-container", "message-answer");

            var divMessageArrow = document.createElement("div");
            divMessageArrow.classList.add("message-arrow");

            var pMessageContent = document.createElement("p");
            pMessageContent.classList.add("message-content");
            pMessageContent.innerHTML = "<b>Gemini: </b><br>" + respostaDoGemini;

            divResposta.appendChild(divMessageArrow);
            divResposta.appendChild(pMessageContent);
            document.querySelector("main.chat-history").appendChild(divResposta);

            //  Enviando o scroll da conversa para baixo
            document.querySelector("main.chat-history").scrollTop = document.querySelector("main.chat-history").scrollHeight;
        }

        //  Função responsável por encerrar a sessão do usuário
        function finalizarSessao() {
            //  Realizando uma requisição assíncrona ao servidor usando jQuery AJAX, permitindo encerrar a sessão do usuário dinamicamente sem recarregar a página
            $.ajax({
                method: "POST",
                url: "finalizandoSessao",
                success: function (response) {
                    let isSessaoFinalizada = response;

                    if (isSessaoFinalizada === "true") {
                        window.location.href = "index.jsp";
                    } else {
                        alert("Não foi possível encerrar sua sessão. Tente novamente mais tarde!");
                    }
                },
                error: function (xhr, status, error) {
                    console.log("Erro: " + error);
                    window.location.href = "index.jsp";
                }
            });
        }
    </script>
</html>