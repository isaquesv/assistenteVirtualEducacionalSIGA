// Script JS presente na página apagarConta
const elementoButtonExcluirConta = document.querySelector(".botaoSimApagarConta");

if (elementoButtonExcluirConta) {
    elementoButtonExcluirConta.addEventListener("click", excluirConta);
}

function excluirConta() {
    let isMensagensExcluidas = excluirConversa();

    if (isMensagensExcluidas === "true") {
        // Adicionando um curto período de espera para evitar problemas com o banco de dados
        setTimeout(() => {
            // Realizando uma requisição assíncrona ao servidor usando jQuery AJAX, permitindo excluir a conta do usuário dinamicamente sem recarregar a página
            $.ajax({
                method: "POST",
                url: "ExcluirConta",
                success: function (response) {
                    let isContaDesativada = response;
    
                    if (isContaDesativada === "true") {
                        finalizarSessao();
                    } else {
                        alert("Não foi possível excluir sua conta. Tente novamente mais tarde!");
                    }
                },
                error: function (xhr, status, error) {
                    console.log("Erro: " + error);
                }
            });
        }, 100);
    } else {
        alert("Não foi possível excluir sua conversa. Tente novamente mais tarde!");
    }
}

function excluirConversa() {
    // Realizando uma requisição assíncrona ao servidor usando jQuery AJAX, permitindo excluir a conversa do usuário dinamicamente sem recarregar a página
    $.ajax({
        method: "POST",
        url: "ExcluirConversa",
        success: function (response) {
            let isMensagensExcluidas = response;
            return isMensagensExcluidas;
        },
        error: function (xhr, status, error) {
            console.log("Erro: " + error);
        }
    });
}
