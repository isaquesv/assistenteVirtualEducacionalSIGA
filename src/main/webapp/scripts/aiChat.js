// Script JS presente na página aiChat
const alturaNavbar = document.querySelector("header.navbar").clientHeight;
const alturaFooter = document.querySelector("footer.footer").clientHeight;
const somaAlturasNavbarEFooter = alturaNavbar + alturaFooter;
const elementoBackgroundChat = document.querySelector("#chatBackground");

elementoBackgroundChat.style.height = "calc(100vh - " + somaAlturasNavbarEFooter + "px)";

//
let isTempoDeEsperaAtivo = false;
const elementoButtonExcluirConversa = document.querySelector(".botaoSimExcluirConversa");
const elementoButtonEnviarPergunta = document.querySelector("#enviarPergunta");
const elementoInputPergunta = document.querySelector("#inputPergunta");

// Deixando o campo de mensagem em foco
elementoInputPergunta.focus();

elementoInputPergunta.addEventListener("keypress", function (event) {
    // Quando a tecla "Enter" for pressionada enquanto a caixa de mensagem estiver em foco
    if (event.key === "Enter") {
        event.preventDefault();
        enviarMensagem();
    }
});

elementoButtonEnviarPergunta.addEventListener("click", enviarMensagem);
elementoButtonExcluirConversa.addEventListener("click", excluirConversa);

// Adicionando a primeira mensagem da conversa
gerarElementoMensagem(
    "Olá! Como vai? 👋😊 Sou o <b>EduSIGA</b>, o assistente virtual educacional da sua instituição, a <b>Faculdade de Tecnologia de Praia Grande</b>! Estou aqui para ajudar você com <b>dúvidas acadêmicas</b> e <b>orientações</b>. Fique à vontade para perguntar sobre <b>matérias</b>, <b>horários</b>, <b>cursos</b> e tudo o que precisar para facilitar sua jornada de aprendizado. Vamos começar?",
    1
);

// Adicionando o histórico de mensagens
adicionarMensagensAnterioresAConversa();

function enviarMensagem() {
    const elementoInputPergunta = document.querySelector("#inputPergunta");
    const promptAluno = elementoInputPergunta.value.trim();

    // Impedindo o envio de mensagens se a mensagem estiver vazia ou se o tempo de espera estiver ativo
    if (promptAluno === "" || isTempoDeEsperaAtivo === true) {
        return;
    } else {
        isTempoDeEsperaAtivo = true;
        // Alterando o ícone do botão de envio
        document.querySelector("#enviarPergunta i").classList.remove("bi-send-fill");
        document.querySelector("#enviarPergunta i").classList.add("bi-stopwatch");
        document.querySelector("#enviarPergunta").style.backgroundColor = "#CCCCCC";

        elementoInputPergunta.value = "";
        gerarElementoMensagem(promptAluno, 0);
        processarMensagem(promptAluno);
    }
}

function processarMensagem(promptAluno) {
    // Realizando uma requisição assíncrona ao servidor usando jQuery AJAX, permitindo a coleta de dados do SIGA do aluno dinamicamente sem recarregar a página
    $.ajax({
        method: "POST",
        url: "ProcessarMensagemIA",
        data: {
            promptAluno: promptAluno
        },
        success: function (response) {
            let respostaIA = response;

            isTempoDeEsperaAtivo = false;
            // Alterando o ícone do botão de envio
            document.querySelector("#enviarPergunta i").classList.remove("bi-stopwatch");
            document.querySelector("#enviarPergunta i").classList.add("bi-send-fill");
            document.querySelector("#enviarPergunta").style.backgroundColor = "#ee7895";

            if (respostaIA.includes("Erro na requisição:")) {
                isTempoDeEsperaAtivo = true;
                // Alterando o ícone do botão de envio
                document.querySelector("#enviarPergunta i").classList.remove("bi-send-fill");
                document.querySelector("#enviarPergunta i").classList.add("bi-stopwatch");
                document.querySelector("#enviarPergunta").style.backgroundColor = "#CCCCCC";
                console.log(respostaIA);
                
                if (respostaIA.includes("Erro na requisição: 429")) {
                    gerarElementoMensagem("Você realizou muitas solicitações em um curto período de tempo. Por favor, atualize a página, aguarde alguns minutos e tente novamente. Agradecemos pela compreensão!", 1);
                } else if (respostaIA.includes("Erro na requisição: 400")) {
                    gerarElementoMensagem("Houve um problema com a sua solicitação. Verifique se sua chave de acesso está correta, atualize a página e tente novamente.", 1);
                } else if (respostaIA.includes("Erro na requisição: 503")) {
                    gerarElementoMensagem("O serviço está temporariamente indisponível. Isso pode ser devido a manutenção ou sobrecarga do sistema. Por favor, atualize a página, espere alguns minutos e tente novamente. Agradecemos pela compreensão!", 1);
                } else {
                    gerarElementoMensagem("Ocorreu um erro inesperado ao processar sua solicitação. Por favor, atualize a página e tente novamente mais tarde.", 1);
                }
            } else {
                gerarElementoMensagem(respostaIA, 1);
            }
        },
        error: function (xhr, status, error) {
            console.log("Erro: " + error);
        }
    });
}

function gerarElementoMensagem(mensagem, isEnviadoPorIA) {
    mensagem = formatarMensagem(mensagem);

    const elementoMainChat = document.querySelector("main#chat");
    // Criando o "balão" de mensagem
    let divMensagem = document.createElement("div");
    divMensagem.classList.add("message-container");

    let divSetaMensagem = document.createElement("div");
    divSetaMensagem.classList.add("message-arrow");

    let pConteudoMensagem = document.createElement("p");
    pConteudoMensagem.classList.add("message-content");

    // Se a mensagem for uma pergunta
    if (isEnviadoPorIA == 0) {
        divMensagem.classList.add("message-question", "ms-auto", "me-1");
        pConteudoMensagem.innerHTML = "<b>Você:</b><br>" + mensagem;
    } else {
        divMensagem.classList.add("message-answer", "ms-1");
        pConteudoMensagem.innerHTML = "<b>EduSIGA:</b><br>" + mensagem;
    }

    divMensagem.appendChild(divSetaMensagem);
    divMensagem.appendChild(pConteudoMensagem);
    elementoMainChat.appendChild(divMensagem);

    // Enviando o scroll da conversa para baixo
    elementoMainChat.scrollTop = elementoMainChat.scrollHeight;
}

function adicionarMensagensAnterioresAConversa() {
    // Realizando uma requisição assíncrona ao servidor usando jQuery AJAX, permitindo a captura das mensagens anterioes ativas da conversa usuário dinamicamente sem recarregar a página
    $.ajax({
        method: "POST",
        url: "CapturarMensagensAnterioresConversa",
        // Garantindo que a resposta seja interpretada como JSON
        dataType: "json",
        success: function (listaMensagensCapturadas) {
            for (let i = 0; i < listaMensagensCapturadas.length; i++) {
                gerarElementoMensagem(
                    listaMensagensCapturadas[i].texto_mensagem,
                    listaMensagensCapturadas[i].is_mensagem_enviada_por_ia
                );
            }
        },
        error: function (xhr, status, error) {
            console.log("Erro: " + error);
        }
    });
}

function excluirConversa() {
    // Realizando uma requisição assíncrona ao servidor usando jQuery AJAX, permitindo excluir a conversa do usuário dinamicamente sem recarregar a página
    $.ajax({
        method: "POST",
        url: "ExcluirConversa",
        success: function (response) {
            let isMensagensExcluidas = response;

            if (isMensagensExcluidas === "true") {
                window.location.href = "aiChat.jsp";
            } else {
                alert("Não foi possível deletar sua conversa. Tente novamente mais tarde!");
            }
        },
        error: function (xhr, status, error) {
            console.log("Erro: " + error);
        }
    });
}

function formatarMensagem(mensagem) {
    mensagem = mensagem
        .replace(/\*\*(.*?)\*\*/g, "<b>$1</b>") // Substitui "**" por "<b>" e "</b>"
        .replace(/\*/g, "")                     // Remove todos os "*"
        .replace(/\\u003c/g, "<")               // Substitui todos os "\u003c" por "<"
        .replace(/<\s+/g, "<")                  // Remove o espaço depois de "<"
        .replace(/<\\?/g, "<")                  // Substitui todos os "<\" por "<"
        .replace(/\\u003e/g, ">")               // Substitui todos os "\u003e" por ">"
        .replace(/\s+>/g, ">")                  // Remove o espaço antes de ">"
        .replace(/\\n/g, "")                    // Remove todos os "\n"
        .replace(/#/g, "")                      // Remove todos os "#"
        .replace(/ \!/g, "!")                   // Substitui todos os " !" por "!"
        .replace(/ \?/g, "?")                   // Substitui todos os " ?" por "?"
        .replace(/ \./g, ".")                   // Substitui todos os " ." por "."
        .replace(/ \,/g, ",")                   // Substitui todos os " ," por ","
        .replace(/ \:/g, ":")                   // Substitui todos os " :" por ":"
        .replace(/ \;/g, ";")                   // Substitui todos os " ;" por ";"
        .replace(/```html/g, "")                // Remove todos os "```html"
        .replace(/```/g, "");                   // Remove todos os "```"

    return mensagem.trim();
}
