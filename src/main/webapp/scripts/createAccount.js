//  Executando ações assim que o conteúdo do documento estiver carregado
document.addEventListener("DOMContentLoaded", function () {
    const inputUsuario = document.querySelector("#nomeUsuarioCadastro");
    const inputEmail = document.querySelector("#emailUsuarioCadastro");
    const inputSenha = document.querySelector("#senhaUsuarioCadastro");
    const inputConfirmar = document.querySelector("#confirmarCadastro");

    //  Verificando se a tecla "Enter" foi pressionada enquanto um dos campos estiver em foco
    inputUsuario.addEventListener("keydown", (event) => {
        if (event.key === "Enter") {
            validandoCadastro(inputUsuario.value.trim(), inputEmail.value.trim(), inputSenha.value.trim());
        }
    });
    inputEmail.addEventListener("keydown", (event) => {
        if (event.key === "Enter") {
            validandoCadastro(inputUsuario.value.trim(), inputEmail.value.trim(), inputSenha.value.trim());
        }
    });
    inputSenha.addEventListener("keydown", (event) => {
        if (event.key === "Enter") {
            validandoCadastro(inputUsuario.value.trim(), inputEmail.value.trim(), inputSenha.value.trim());
        }
    });
    inputConfirmar.addEventListener("click", function () {
        validandoCadastro(inputUsuario.value.trim(), inputEmail.value.trim(), inputSenha.value.trim());
    });
});

//  Função responsável por validar o cadastro do usuário
function validandoCadastro(nomeUsuario, emailUsuario, senhaUsuario) {
    if (nomeUsuario === "" && emailUsuario === "" && senhaUsuario === "") {
        document.querySelector("#notificacaoErros").style.display = "block";
        document.querySelector("#mensagemErro").innerHTML = "Usuário invalido - Forneça o usuário<br>Email invalido - Forneça o email<br>Senha invalida - Forneça a senha";
    } else {
        if (nomeUsuario === "") {
            document.querySelector("#notificacaoErros").style.display = "block";
            document.querySelector("#mensagemErro").innerHTML = "Usuário invalido - Forneça o usuário";
        } else if (emailUsuario === "") {
            document.querySelector("#notificacaoErros").style.display = "block";
            document.querySelector("#mensagemErro").innerHTML = "Email invalido - Forneça o email";
        } else if (senhaUsuario === "") {
            document.querySelector("#notificacaoErros").style.display = "block";
            document.querySelector("#mensagemErro").innerHTML = "Senha invalida - Forneça a senha";
        } else {

            //  Realizando uma requisição assíncrona ao servidor usando jQuery AJAX, permitindo validar o cadastro do usuário dinamicamente sem recarregar a página
            $.ajax({
                method: "POST",
                url: "validandoCadastroUsuario",
                data: { //  Dados que serão enviados:
                    nomeUsuario: nomeUsuario,
                    emailUsuario: emailUsuario,
                    senhaUsuario: senhaUsuario
                },
                success: function (response) {  //  Se a requisição for bem-sucedida: 
                    let isCadastroUsuarioValido = response;

                    if (isCadastroUsuarioValido === "true") {
                        window.location.href = "aiChat.jsp";
                    } else {
                        document.querySelector("#notificacaoErros").style.display = "block";
                        document.querySelector("#mensagemErro").innerHTML = "Email já cadastrado!";
                    }
                },
                error: function (xhr, status, error) {  //  Se houver um erro na requisição:
                    console.log("Erro: " + error);
                }
            });
        }
    }
}