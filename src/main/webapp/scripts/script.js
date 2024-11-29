// Script JS presente em todas as páginas
const elementoButtonFinalizarSessao = document.querySelector(".botaoSimFinalizarSessao");

if (elementoButtonFinalizarSessao) {
    elementoButtonFinalizarSessao.addEventListener("click", finalizarSessao);
}

window.addEventListener("resize", function () {
    // Largura atual do body
    let bodyWidth = document.body.clientWidth;

    // Se a largura da tela for maior que 768 e a navbar mobile estiver visível
    if (bodyWidth > 768 && document.querySelector("#navbarToggleExternalContent").classList.contains("show")) {
        document.querySelector("#navbarToggleExternalContent").classList.remove("show");
    }
});

function finalizarSessao() {
    // Realizando uma requisição assíncrona ao servidor usando jQuery AJAX, permitindo encerrar a sessão do usuário dinamicamente sem recarregar a página
    $.ajax({
        method: "POST",
        url: "FinalizarSessao",
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
        }
    });
}
