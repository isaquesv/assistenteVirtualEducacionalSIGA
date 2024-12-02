# Assistente Virtual Educacional integrado ao SIGA

</p>

# Índice 
* [Título](#assistente-virtual-educacional-integrado-ao-siga)
* [Índice](#índice)
* [Descrição do Projeto](#descrição-do-projeto)
* [Andamento do projeto](#status-do-projeto)
* [Funcionalidades do projeto](#funcionalidades-do-projeto)
* [Acesso ao projeto](#acesso-ao-projeto)
* [Abrir e rodar o projeto](#abrir-e-rodar-o-projeto)
* [Tecnologias utilizadas](#tecnologias-utilizadas)
* [Autores do projeto](#autores)


# Descrição do projeto
Projeto desenvolvido durante as aulas ministradas pelo professor [Ricardo Pupo Larguesa](https://github.com/larguesa) na matéria de <b>Programação Orientada à Objetos (POO)</b>.
Este projeto tem o objetivo de desenvolver um sistema integrado com a IA do <b>Google (Gemini)</b> responsável por oferecer suporte aos alunos que utilizam do <b>“SIGA”</b>, ajudando com o aprendizado e respondendo dúvidas atreladas às informações acadêmicas do aluno.

# Status do projeto
<p align="center">
<img loading="lazy" src="https://img.shields.io/badge/STATUS-concluído-projeto?style=flat-square&logoColor=white&labelColor=%2366416c&color=%23ee7895"/>

# Funcionalidades do projeto

* 1ª funcionalidade: realizar login através do SIGA, inserindo o seu usuário e senha;
* 2ª funcionalidade: conversar com a IA assistente nomeada "EduSIGA", capacitada para responder perguntas sobre a FATEC de Praia Grande e seus cursos;
* 3ª funcionalidade: apagar conversa com o EduSIGA a qualquer momento;
* 4ª funcionalidade: o usuário poderá apagar sua conta a qualquer momento.

# Acesso ao projeto
Você pode baixar o projeto inteiro em sua máquina em <b><>Code</b>, e depois em <b>Download ZIP.</b>

# Abrir e rodar o projeto
Feito o download do projeto, pode ser necessário realizar a descompactação do arquivo em formato ZIP. Para isso, é recomendado o uso do WinRAR. 
Feita a descompactação, recomendamos o uso da IDE <b>NetBeans</b> para a execução do projeto, e a inserção da chave da IA no arquivo "IAGemini.java" para o funcionamento correto do EduSIGA.

# Tecnologias utilizadas

* <b>IDE</b>: Apache NetBeans 22;
* <b>Java EE Version</b>: Jakarta EE 10 Web;
* <b>Java Platform</b>: JDK 19;
* <b>Server</b>: Glassfish 7.0;
* <b>Projeto</b>: Java com Maven, Aplicação Web;
* <b>Banco de dados</b>: SQLite.
  
* <b>Bibliotecas</b>:
<p>Gemini 1.5 Flash: IA utilizada no projeto, modelo da série Gemini da Google, escolhida por ser uma versão com o menor custo por token entre todos os modelos da série Gemini;</p>
<p>HtmlUnit: simula um navegador da web sem interface gráfica, utlizado para o web scraping do SIGA;</p>
<p>Selenium e Headless: o selenium é uma ferramenta que faz a simulação da interação do usuário, e o headless é um modo capaz de executar o navegador sem necessidade de interface gráfica. Utilizado nesse projeto para a automatização do processo de login no SIGA;</p>
<p>Bootstrap 5: utilizada para criar layouts de página;</p>
<p>Google Fonts: uso de fontes personalizadas;</p>
<p>Ajax: é um gerenciador de requisições, ele permite que uma página envie e receba dados de um servidor de forma assíncrona.</p>

# Autores
* [Isaque Silva Venancio](https://github.com/isaquesv)
* [Luiza Carla Simões dos Santos](https://github.com/luiza-carla)
* [Ruan Fernandes Mendonça](https://github.com/ruann-fernandess)
