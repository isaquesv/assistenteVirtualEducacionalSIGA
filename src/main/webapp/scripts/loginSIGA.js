// Script JS presente na página loginSIGA
const inputUsuario = document.querySelector("#usuarioLoginSIGA");
const inputSenha = document.querySelector("#senhaLoginSIGA");
const iconeExibirSenha = document.querySelector("#iconeSenha");
const inputConfirmar = document.querySelector("#confirmarLoginSIGA");

const modalLoading = document.querySelector("#loadingModal");
const loadingText = document.querySelector("#loading-text");

// Deixando o campo de usuário em foco
inputUsuario.focus();

// Verificando se a tecla "Enter" foi pressionada enquanto um dos inputs estiver em foco
inputUsuario.addEventListener("keydown", (event) => {
    if (event.key === "Enter") {
        validarAcessoSIGA(inputUsuario.value.trim(), inputSenha.value.trim());
    }
});
inputSenha.addEventListener("keydown", (event) => {
    if (event.key === "Enter") {
        validarAcessoSIGA(inputUsuario.value.trim(), inputSenha.value.trim());
    }
});

inputConfirmar.addEventListener("click", function() {
    validarAcessoSIGA(inputUsuario.value.trim(), inputSenha.value.trim());
});

iconeExibirSenha.addEventListener("click", function() {
    exibirSenha();
});

function exibirSenha() {
    if (inputSenha.type === "password") {
        // Exibe a senha
        inputSenha.type = "text";
        // Trocando ícones (olho normal -> olho riscado)
        iconeExibirSenha.classList.remove("bi-eye");
        iconeExibirSenha.classList.add("bi-eye-slash");
    } else {
        // Oculta a senha
        inputSenha.type = "password";
        // Trocando ícones (olho riscado -> olho normal)
        iconeExibirSenha.classList.remove("bi-eye-slash");
        iconeExibirSenha.classList.add("bi-eye");
    }
}

function esconderOuExibirElemento(seletor, valorDisplay) {
    const elemento = document.querySelector(seletor);
    
    if (elemento && elemento.style.display !== valorDisplay) {
        elemento.style.display = valorDisplay;
    }
}

function validarCampoUsuario(usuarioSIGA) {
    if (usuarioSIGA === "") {
        // Exibindo elementos e mensagem de erro
        esconderOuExibirElemento("#notificacoesErros", "block");
        esconderOuExibirElemento("span#spanMensagemErroUsuario", "block");
        document.querySelector("span#spanMensagemErroUsuario").innerHTML = "Usuário invalido - Forneça o usuário";
        
        return false;
    } else {
        // Escondendo elementos e mensagem de erro
        esconderOuExibirElemento("span#spanMensagemErroUsuario", "none");
        document.querySelector("span#spanMensagemErroUsuario").innerHTML = "";
        
        return true;
    }
}

function validarCampoSenha(senhaSIGA) {
    if (senhaSIGA === "") {
        // Exibindo elementos e mensagem de erro
        esconderOuExibirElemento("#notificacoesErros", "block");
        esconderOuExibirElemento("span#spanMensagemErroSenha", "block");
        document.querySelector("span#spanMensagemErroSenha").innerHTML = "Senha invalida - Forneça a senha";

        return false;
    } else {
        // Escondendo elementos e mensagem de erro
        esconderOuExibirElemento("span#spanMensagemErroSenha", "none");
        document.querySelector("span#spanMensagemErroSenha").innerHTML = "";
        
        return true;
    }
}

function validarAcessoSIGA(usuarioSIGA, senhaSIGA) {
    let resultadoValidacaoUsuario = validarCampoUsuario(usuarioSIGA);
    let resultadoValidacaoSenha = validarCampoSenha(senhaSIGA);

    if (resultadoValidacaoUsuario === true && resultadoValidacaoSenha === true) {  
        // Escondendo elemento que armazena todas as mensagens de erro
        esconderOuExibirElemento("#notificacoesErros", "none");

        // Exibindo o modal de "loading"
        esconderOuExibirElemento("#loadingModal", "flex");
        
        if (loadingText) {
            loadingText.innerHTML = "Aguarde, estamos validando seus dados...";
        }

        // Realizando uma requisição assíncrona ao servidor usando jQuery AJAX, permitindo validar o login do usuário dinamicamente sem recarregar a página
        $.ajax({
            method: "POST",
            url: "ValidarAcessoSIGA",
            data: {
                usuarioSIGA: usuarioSIGA,
                senhaSIGA: senhaSIGA
            },
            success: function (response) {
                let mensagemResultadoValidacaoAcessoSIGA = response;

                if (mensagemResultadoValidacaoAcessoSIGA === "Sucesso no login") {
                    esconderOuExibirElemento("span#spanMensagemErroLogin", "none");
                    document.querySelector("span#spanMensagemErroLogin").innerHTML = "";
                    document.querySelector("#loading-text").innerHTML = "Aguarde, estamos coletando os dados. Esta ação pode levar alguns instantes...";
                    
                    coletarDadosSIGA(usuarioSIGA, senhaSIGA);
                } else {
                    esconderOuExibirElemento("#notificacoesErros", "block");
                    esconderOuExibirElemento("#loadingModal", "none");
                    esconderOuExibirElemento("span#spanMensagemErroLogin", "block");
                    document.querySelector("span#spanMensagemErroLogin").innerHTML = mensagemResultadoValidacaoAcessoSIGA;
                }
            },
            error: function (xhr, status, error) {
                esconderOuExibirElemento("#loadingModal", "none");
                esconderOuExibirElemento("#notificacoesErros", "block");
                esconderOuExibirElemento("span#spanMensagemErroLogin", "block");
                document.querySelector("span#spanMensagemErroLogin").innerHTML = "Desculpe, ocorreu um erro inesperado. Por favor, execute este projeto novamente em sua IDE e tente novamente.";

                console.log("Erro: " + error);
            }
        });
    }
}

function coletarDadosSIGA(usuarioSIGA, senhaSIGA) {
    // Realizando uma requisição assíncrona ao servidor usando jQuery AJAX, permitindo a coleta de dados do SIGA do aluno dinamicamente sem recarregar a página
    $.ajax({
        method: "POST",
        url: "ColetarDadosSIGA",
        data: {
            usuarioSIGA: usuarioSIGA,
            senhaSIGA: senhaSIGA
        },
        success: function (response) {
            let mensagemResultadoColetaDadosSIGA = response;
            
            if (mensagemResultadoColetaDadosSIGA === "Sucesso coleta de dados") {
                window.location.href = "aiChat.jsp";
            } else {
                esconderOuExibirElemento("#notificacoesErros", "block");
                esconderOuExibirElemento("#loadingModal", "none");
                esconderOuExibirElemento("span#spanMensagemErroLogin", "block");
                document.querySelector("span#spanMensagemErroLogin").innerHTML = "Desculpe, ocorreu um erro inesperado. Por favor, tente novamente mais tarde";

                console.log(mensagemResultadoColetaDadosSIGA);
            }
        },
        error: function (xhr, status, error) {
            esconderOuExibirElemento("#loadingModal", "none");
            esconderOuExibirElemento("#notificacoesErros", "block");
            esconderOuExibirElemento("span#spanMensagemErroLogin", "block");
            document.querySelector("span#spanMensagemErroLogin").innerHTML = "Desculpe, ocorreu um erro inesperado. Por favor, tente novamente mais tarde";
            
            console.log("Erro: " + error);
        }
    });
}
