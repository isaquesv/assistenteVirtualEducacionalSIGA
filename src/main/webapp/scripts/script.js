//  Executando ações assim que o conteúdo do documento estiver carregado
document.addEventListener("DOMContentLoaded", function () {
    const mainConteudoPrincipal = document.querySelector("main.content");
    const alturaNavbarPrincipal = document.querySelector("header.navbar").clientHeight;
    const navbarMobile = document.querySelector("#navbarToggleExternalContent");
    const buttonToggleNavbarMobile = document.querySelector("button.navbar-toggler");

    mainConteudoPrincipal.style.marginTop = document.querySelector("header.navbar").clientHeight + "px";

    buttonToggleNavbarMobile.addEventListener("click", function () {
        setTimeout(function () {
            // Se a navbar mobile estiver visível
            if (navbarMobile.classList.contains("show")) {
                mainConteudoPrincipal.style.marginTop = 0;
                navbarMobile.style.marginTop = alturaNavbarPrincipal + "px";
            } else {
                mainConteudoPrincipal.style.marginTop = alturaNavbarPrincipal + "px";
            }
        }, 5);
    });
});