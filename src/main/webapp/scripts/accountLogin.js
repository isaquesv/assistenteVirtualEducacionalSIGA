//  Executando ações assim que o conteúdo do documento estiver carregado
document.addEventListener("DOMContentLoaded", function () {
    const inputEmail = document.querySelector("#emailUsuarioLogin");
    const inputSenha = document.querySelector("#senhaUsuarioLogin");
    const inputConfirmar = document.querySelector("#confirmarLogin");

    //  Verificando se a tecla "Enter" foi pressionada enquanto um dos campos estiver em foco
    inputEmail.addEventListener("keydown", (event) => {
        if (event.key === "Enter") {
            validandoLogin(inputEmail.value.trim(), inputSenha.value.trim());
        }
    });
    inputSenha.addEventListener("keydown", (event) => {
        if (event.key === "Enter") {
            validandoLogin(inputEmail.value.trim(), inputSenha.value.trim());
        }
    });

    inputConfirmar.addEventListener("click", function () {
        validandoLogin(inputEmail.value.trim(), inputSenha.value.trim());
    });
});

//  Função responsável por validar o cadastro do usuário
function validandoLogin(emailUsuario, senhaUsuario) {
    if (emailUsuario === "" && senhaUsuario === "") {
        document.querySelector("#notificacaoErros").style.display = "block";
        document.querySelector("#mensagemErro").innerHTML = "Email invalido - Forneça o email<br>Senha invalida - Forneça a senha";
    } else {
        if (emailUsuario === "") {
            document.querySelector("#notificacaoErros").style.display = "block";
            document.querySelector("#mensagemErro").innerHTML = "Email invalido - Forneça o email";
        } else if (senhaUsuario === "") {
            document.querySelector("#notificacaoErros").style.display = "block";
            document.querySelector("#mensagemErro").innerHTML = "Senha invalida - Forneça a senha";
        } else {

            //  Realizando uma requisição assíncrona ao servidor usando jQuery AJAX, permitindo validar o login do usuário dinamicamente sem recarregar a página
            $.ajax({
                method: "POST",
                url: "validandoLoginUsuario",
                data: { //  Dados que serão enviados
                    emailUsuario: emailUsuario,
                    senhaUsuario: senhaUsuario
                },
                success: function (response) {  //  Se a requisição for bem-sucedida 
                    let isLoginUsuarioValido = response;

                    if (isLoginUsuarioValido === "true") {
                        window.location.href = "loginSIGA.jsp";
                    } else {
                        document.querySelector("#notificacaoErros").style.display = "block";
                        document.querySelector("#mensagemErro").innerHTML = "Não confere Email e Senha!";
                    }
                },
                error: function (xhr, status, error) {  //  Se houver um erro na requisição
                    console.log("Erro: " + error);
                }
            });
        }
    }
}