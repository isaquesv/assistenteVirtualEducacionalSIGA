//  Executando ações assim que o conteúdo do documento estiver carregado
document.addEventListener("DOMContentLoaded", function () {
    const inputUsuario = document.querySelector("#usuarioLoginSIGA");
    const inputSenha = document.querySelector("#senhaLoginSIGA");
    const inputConfirmar = document.querySelector("#confirmarLoginSIGA");

    //  Verificando se a tecla "Enter" foi pressionada enquanto um dos inputs estiver em foco
    inputUsuario.addEventListener("keydown", (event) => {
        if (event.key === "Enter") {
            validandoAcessoSIGA(inputUsuario.value.trim(), inputSenha.value.trim());
        }
    });
    inputSenha.addEventListener("keydown", (event) => {
        if (event.key === "Enter") {
            validandoAcessoSIGA(inputUsuario.value.trim(), inputSenha.value.trim());
        }
    });

    inputConfirmar.addEventListener("click", function () {
        validandoAcessoSIGA(inputUsuario.value.trim(), inputSenha.value.trim());
    });
});

//  Função responsável por verificar se o acesso ao SIGA é válido ou não
function validandoAcessoSIGA(usuarioSIGA, senhaSIGA) {
    if (usuarioSIGA === "" && senhaSIGA === "") {
        document.querySelector("#notificacaoErros").style.display = "block";
        document.querySelector("#mensagemErro").innerHTML = "Usuário invalido - Forneça o usuário<br>Senha invalida - Forneça a senha";
    } else {
        if (usuarioSIGA === "") {
            document.querySelector("#notificacaoErros").style.display = "block";
            document.querySelector("#mensagemErro").innerHTML = "Usuário invalido - Forneça o usuário";
        } else if (senhaSIGA === "") {
            document.querySelector("#notificacaoErros").style.display = "block";
            document.querySelector("#mensagemErro").innerHTML = "Senha invalida - Forneça a senha";
        } else {
            //  Exibindo o modal de loading
            document.querySelector("#loadingModal").style.display = "flex";

            //  Realizando uma requisição assíncrona ao servidor usando jQuery AJAX, permitindo validar o login do usuário dinamicamente sem recarregar a página
            $.ajax({
                method: "POST",
                url: "validandoAcessoSIGA",
                data: { //  Dados que serão enviados:
                    usuarioSIGA: usuarioSIGA,
                    senhaSIGA: senhaSIGA
                },
                success: function (response) {  //  Se a requisição for bem-sucedida:
                    let isAcessoSIGAValido = response;

                    if (isAcessoSIGAValido === "true") {
                        coletandoDadosSIGA(usuarioSIGA, senhaSIGA);
                    } else {
                        //  Interrompendo o loading se a validação falhar
                        document.querySelector("#loadingModal").style.display = "none";
                        document.querySelector("#notificacaoErros").style.display = "block";
                        document.querySelector("#mensagemErro").innerHTML = "Não confere Login e Senha";
                    }
                },
                error: function (xhr, status, error) {  //  Se houver um erro na requisição:
                    document.querySelector("#loadingModal").style.display = "none";
                    document.querySelector("#notificacaoErros").style.display = "block";
                    document.querySelector("#mensagemErro").innerHTML = "Desculpe, ocorreu um erro inesperado. Por favor, tente novamente mais tarde";
                    console.log("Erro: " + error);
                }
            });
        }
    }
}

//  Função responsável por capturar os dados do SIGA
function coletandoDadosSIGA(usuarioSIGA, senhaSIGA) {
    //  Realizando uma requisição assíncrona ao servidor usando jQuery AJAX, permitindo a coleta de dados do SIGA do aluno dinamicamente sem recarregar a página
    $.ajax({
        method: "POST",
        url: "coletandoDadosSIGA",
        data: {
            usuarioSIGA: usuarioSIGA,
            senhaSIGA: senhaSIGA
        },
        success: function (response) {
            let isColetandoDadosSIGASucesso = response;

            if (isColetandoDadosSIGASucesso === "true") {
                window.location.href = "aiChat.jsp";
            } else {
                document.querySelector("#loadingModal").style.display = "none";
                document.querySelector("#notificacaoErros").style.display = "block";
                document.querySelector("#mensagemErro").innerHTML = "Desculpe, ocorreu um erro inesperado. Por favor, tente novamente mais tarde";
            }
        },
        error: function (xhr, status, error) {
            alert("Desculpe, ocorreu um erro inesperado. Por favor, tente novamente mais tarde.");
            console.log("Erro: " + error);
        }
    });
}