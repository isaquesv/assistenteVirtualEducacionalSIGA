package liryou.assistentevirtualeducacionalsiga.classes;

// Permite estabelecer conexões com o banco de dados
import java.sql.Connection;
// Gerencia a configuração e criação da conexão com o banco de dados
import java.sql.DriverManager;
import java.sql.SQLException;
// Permite executar comandos SQL no banco de dados
import java.sql.Statement;

// Classes que contém as consultas SQL disponíveis para todas as tabelas criadas
import liryou.assistentevirtualeducacionalsiga.classes.database.consultasSQL.*;

// Permite a manipulação de Arrays e Objetos JSON
import org.json.JSONArray;
import org.json.JSONObject;

public class DriverBD implements Alunos, Unidades_Ensino, Cursos, Mensagens, Unidades_Ensino_Cursos, Matrizes_Curriculares {
    // Método para obter a conexão com o banco de dados
    public static Connection getConnection() throws SQLException {
        // Registrando o driver SQLite manualmente para evitar o seguinte erro: No suitable driver found for jdbc:sqlite:edusiga.db
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC não encontrado.");
            e.printStackTrace();
        }
        
        // Estabelecendo conexão com o banco de dados SQLite
        // Usando Write-Ahead Logging para melhorar o desempenho do banco em tempo real, permitindo leituras e gravações simultâneas sem bloqueios
        Connection connectionBD = DriverManager.getConnection("jdbc:sqlite:edusiga.db?journal_mode=WAL");
        
        // Criando o Statement depois de ter a conexão com o banco
        Statement statement = connectionBD.createStatement();

        // Criando as tabelas e verificando se estão vazias
        criarTabelas(statement);
        verificarTabelasVazias(connectionBD);

        return connectionBD;
    }

    // Método para fechar a conexão com o banco de dados
    public static void closeConnection(Connection connectionBD) {
        try {
            if (connectionBD != null) {
                connectionBD.close();
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fechar a conexão: " + e.getMessage());
        }
    }
    
    public static void criarTabelas(Statement statement) throws SQLException {
        // Criando a tabela Unidades_Ensino
        statement.executeUpdate(
            "CREATE TABLE IF NOT EXISTS unidades_ensino " +
            "(cd_unidade_ensino INTEGER PRIMARY KEY, nm_unidade_ensino VARCHAR(100) NOT NULL, ds_endereco_unidade_ensino VARCHAR(255), ds_unidade_ensino VARCHAR(3500))"
        );
        // Criando a tabela Cursos
        statement.executeUpdate(
            "CREATE TABLE IF NOT EXISTS cursos " +
            "(cd_curso INTEGER PRIMARY KEY, nm_curso VARCHAR(100) NOT NULL, sg_curso VARCHAR(10), ds_curso VARCHAR(2700))"
        );
        // Criando a tabela Unidades_Ensino_Cursos
        statement.executeUpdate(
            "CREATE TABLE IF NOT EXISTS unidades_ensino_cursos " +
            "(id_unidade_ensino INTEGER NOT NULL, id_curso INTEGER NOT NULL, PRIMARY KEY (id_unidade_ensino, id_curso))"
        );
        // Criando a tabela Matrizes_Curriculares
        statement.executeUpdate(
            "CREATE TABLE IF NOT EXISTS matrizes_curriculares " +
            "(cd_matriz_curricular INTEGER PRIMARY KEY, ds_matriz_curricular VARCHAR(860) NOT NULL, id_curso INTEGER NOT NULL, FOREIGN KEY (id_curso) REFERENCES cursos(cd_curso))"
        );
        // Criando a tabela Alunos
        statement.executeUpdate(
            "CREATE TABLE IF NOT EXISTS alunos " +
            "(cd_aluno INTEGER PRIMARY KEY, cpf_aluno VARCHAR(11) NOT NULL, is_aluno_ativo TINYINT(1) NOT NULL)"
        );
        // Criando a tabela Conversas
        statement.executeUpdate(
            "CREATE TABLE IF NOT EXISTS conversas " +
            "(cd_conversa INTEGER PRIMARY KEY, id_aluno INTEGER NOT NULL)"
        );
        // Criando a tabela Mensagens
        statement.executeUpdate(
            "CREATE TABLE IF NOT EXISTS mensagens " +
            "(cd_mensagem INTEGER PRIMARY KEY, id_conversa INTEGER NOT NULL, id_aluno INTEGER NOT NULL, ds_mensagem TEXT, is_enviada_por_ia TINYINT(1) NOT NULL, is_mensagem_ativa TINYINT(1) NOT NULL, dt_criacao_mensagem DATETIME NOT NULL)"
        );
    }
    
    public static void verificarTabelasVazias(Connection connectionBD) throws SQLException {
        int quantidadeTotalRegistrosUnidadeEnsino = Unidades_Ensino.SelectUnidadesEnsinoContagemRegistros(connectionBD);
        int codigoUnidadeEnsino = 0;
        String nomeUnidadeEnsino = "Faculdade de Tecnologia de Praia Grande";
            
        // Validação para cadastrar a Fatec de Praia Grande
        if (quantidadeTotalRegistrosUnidadeEnsino == 0) {
            String enderecoUnidadeEnsino = "Endereço da faculdade: Praça 19 de Janeiro, 144 - Boqueirão. CEP: 11700-100. Cidade: Praia Grande. Estado: São Paulo (Baixada Santista)";
            String descricaoUnidadeEnsino = "Descrição histórica: A Faculdade de Tecnologia de Praia Grande iniciou suas atividades em 3 de setembro de 2002, com 80 vagas para o curso de Tecnologia em Informática com Ênfase em Gestão de Negócios, divididas entre os turnos vespertino e noturno. Em 11 de março de 2003, começou a funcionar a Escola Técnica Estadual “Adolfo Berezin” – Extensão Praia Grande, com 120 vagas para os cursos Técnico em Informática e Técnico em Logística. Ainda em março de 2003, em parceria com o IPEN/CEETEPS, iniciou-se a oferta de cursos de pós-graduação lato sensu, como Gestão Empresarial e Consultoria Web. Em 2 de março de 2006, a unidade de Praia Grande deixou de ser extensão da Faculdade de Tecnologia da Baixada Santista, tornando-se oficialmente a Faculdade de Tecnologia de Praia Grande. Em fevereiro de 2009, começou o curso de graduação em Tecnologia em Comércio Exterior, e em 2010, o curso de Tecnologia em Informática para Gestão de Negócios foi reestruturado, resultando nos cursos de Análise e Desenvolvimento de Sistemas e Gestão Empresarial. No primeiro semestre de 2011, esses cursos, junto ao de Processos Químicos, passaram a ser oferecidos na Fatec Praia Grande. Com isso, a instituição passou a oferecer cursos nos níveis tecnológico, de graduação e de pós-graduação, atendendo cerca de 1.000 alunos nos períodos manhã, tarde e noite. A Fatec Praia Grande tem como missão formar profissionais competentes e éticos para enfrentar desafios tecnológicos, sociais e econômicos, com o slogan “Compromisso e Respeito com o Aluno e a Comunidade”. Além disso, oferece cursos de Verão e Inverno para aperfeiçoamento tecnológico e realiza, durante o aniversário da instituição, o Workshop de Tecnologia da Informação, visando a atualização de alunos, professores e a comunidade. Administração da Unidade de Praia Grande: Direção - Profª. Meª. Viviam Ester de Souza; Assistente Técnico Administrativo - Vanusa Santos de Barros; Diretoria de Serviços Administrativos - Rosângela Cândida da Costa; Assistente Administrativo - Kleber do Carmo Cesar do Nascimento; Diretora de Serviços Acadêmicos - Denise Silva de Souza Vilar. Telefone: (13) 3591-1303 / 3591-6968. E-mails: Diretoria: f129dir@cps.sp.gov.br, Diretoria Administrativa: f129adm@cps.sp.gov.br, Diretoria Acadêmica: f129acad@cps.sp.gov.br. Site: http://www.fatecpg.com.br/";
            
            // Inserindo a Fatec de Praia Grande no banco e capturando sua chave primária
            codigoUnidadeEnsino = Unidades_Ensino.InsertUnidadeEnsino(
                connectionBD,
                nomeUnidadeEnsino,
                enderecoUnidadeEnsino,
                descricaoUnidadeEnsino
            );
        } else {
            // Capturando a chave primária da Fatec de Praia Grande
            codigoUnidadeEnsino = Unidades_Ensino.SelectUnidadeEnsinoCodigo(
                connectionBD,
                nomeUnidadeEnsino
            );
        }
            
        int quantidadeTotalRegistrosCursos = Cursos.SelectCursosContagemRegistros(connectionBD);
        JSONArray cursos = new JSONArray();
        // Validação para cadastrar os cursos presentes na Fatec de Praia Grande
        if (quantidadeTotalRegistrosCursos == 0) {
            cursos.put(new JSONObject()
                .put("nome_curso", "Análise e Desenvolvimento de Sistemas")
                .put("sigla_curso", "ADS")
                .put("descricao_curso", "A matemática, especialmente raciocínio lógico e cálculo, é fundamental para o aluno otimizar computadores e desenvolver softwares. O curso abrange Bancos de Dados, sistemas web e programação distribuída. Além de disciplinas de Administração, Contabilidade, Economia, Estatística e Inglês, também desenvolve habilidades em leitura e interpretação de textos. Duração: 3 anos (6 semestres). Períodos: Vespertino e Noturno. Coordenador: Prof.º Nilson Carlos Duarte da Silva. Tipo: Curso Superior de Tecnologia Presencial. Eixo: Informação e Comunicação. O tecnólogo projeta, desenvolve e audita sistemas, além de prestar consultoria e pesquisa. Atua em empresas públicas, privadas e instituições financeiras.")
            );
            cursos.put(new JSONObject()
                .put("nome_curso", "Comércio Exterior")
                .put("sigla_curso", "COMEX")
                .put("descricao_curso", "O curso tem como base Administração, Economia e Comunicação, além de disciplinas como matemática, gestão financeira, logística e idiomas (Inglês e Espanhol). Estuda-se também blocos econômicos, logística internacional, exportação e importação, marketing internacional, entre outros. Duração: 3 anos (6 semestres). Períodos: Matutino e Noturno. Coordenador: Prof. Me. Ulysses C. C. Diegues. Tipo: Curso Superior de Tecnologia Presencial. Eixo: Gestão e Negócios. O tecnólogo gerencia operações de comércio exterior, negociações internacionais e documentação. Pode atuar em empresas nacionais, multinacionais, consultorias e órgãos públicos.")
            );
            cursos.put(new JSONObject()
                .put("nome_curso", "Desenvolvimento de Software Multiplataforma")
                .put("sigla_curso", "DSM")
                .put("descricao_curso", "O curso abrange disciplinas como lógica de programação, programação para desktop, dispositivos móveis e web, além de computação em nuvem, inteligência artificial, segurança da informação e internet das coisas. Duração: 3 anos (6 semestres). Período: Vespertino. Coordenador: Profº. Dr. Gilmar Aquino. Tipo: Curso Superior de Tecnologia Presencial. Eixo: Informação e Comunicação. O tecnólogo projeta, desenvolve e testa softwares multiplataforma, oferecendo soluções tecnológicas baseadas em linguagens de programação, banco de dados e IA. Pode atuar em empresas de diversos setores ou empreender.")
            );
            cursos.put(new JSONObject()
                .put("nome_curso", "Gestão Empresarial")
                .put("sigla_curso", "GE")
                .put("descricao_curso", "Com bases em Contabilidade, Economia e Administração, o curso inclui Direito Tributário, logística, gestão ambiental e planejamento estratégico. Também é oferecido na modalidade a distância. Duração: 3 anos (6 semestres). Períodos: Matutino e Noturno. Coordenador: Prof.º João Carlos Gomes. Tipo: Curso Superior de Tecnologia Presencial e a Distância. Eixo: Gestão e Negócios. O tecnólogo atua na organização de trabalho, gestão de pessoas e controle de atividades. Trabalha principalmente em pequenas e médias empresas, podendo também empreender.")
            );
            cursos.put(new JSONObject()
                .put("nome_curso", "Processos Químicos")
                .put("sigla_curso", "PQ")
                .put("descricao_curso", "O curso inclui disciplinas de química, cálculo, física, microbiologia e processos químicos industriais. Também aborda gestão ambiental e tratamento de efluentes. Duração: 3 anos (6 semestres). Períodos: Matutino e Noturno. Coordenador: Prof.º Waldemar Alves Ribeiro Filho. Tipo: Curso Superior de Tecnologia Presencial. Eixo: Controle e Processos Industriais. O tecnólogo gerencia processos laboratoriais e industriais, com foco em qualidade e sustentabilidade. Atua em indústrias químicas, petroquímicas, eletroquímicas e farmoquímicas.")
            );

            // Inserindo cursos no banco
            for (int i = 0; i < cursos.length(); i++) {
                JSONObject cursoAtual = cursos.getJSONObject(i); 
                String nomeCursoAtual = cursoAtual.getString("nome_curso");
                String siglaCursoAtual = cursoAtual.getString("sigla_curso");
                String descricaoCursoAtual = cursoAtual.getString("descricao_curso");        

                // Inserindo o curso no banco e capturando sua chave primária
                int codigoCursoInserido = Cursos.InsertCurso(
                    connectionBD,
                    nomeCursoAtual,
                    siglaCursoAtual,
                    descricaoCursoAtual
                );
                // Adicionando a chave primária do curso
                cursoAtual.put("codigo_curso", codigoCursoInserido);
            }
        }

        int quantidadeTotalRegistrosUnidadesEnsinoCursos = Unidades_Ensino_Cursos.SelectUnidadesEnsinoCursosContagemRegistros(connectionBD, codigoUnidadeEnsino);
        // Validação para cadastrar as relações entre a unidade de ensino e seus respectivos cursos
        if (quantidadeTotalRegistrosUnidadesEnsinoCursos == 0) {
            for (int i = 0; i < cursos.length(); i++) {
                JSONObject cursoAtual = cursos.getJSONObject(i); 
                int codigoCursoAtual = cursoAtual.getInt("codigo_curso");
                
                // Inserindo a relação entre a unidade de ensino e curso
                Unidades_Ensino_Cursos.InsertUnidadeEnsinoCurso(
                    connectionBD,
                    codigoUnidadeEnsino,
                    codigoCursoAtual
                );
            }       
        }

        int quantidadeTotalRegistrosMatrizesCurriculares = Matrizes_Curriculares.SelectMatrizesCurricularesContagemRegistros(connectionBD);
        JSONArray matrizesCurriculares = new JSONArray();

        // Validação para cadastrar as matrizes curriculares dos cursos da Fatec de Praia Grande
        if (quantidadeTotalRegistrosMatrizesCurriculares == 0) {
            // Matrizes curriculares de ADS
            matrizesCurriculares.put(new JSONObject()
                .put("semestre_1", "1º ciclo/semestre: Administração Geral (carga horária: 80h), Arquitetura e Organização de Computadores (carga horária: 80h), Algoritmo e Lógica de Programação (carga horária: 80h), Laboratório de Hardware (carga horária: 40h), Programação em Microinformática (carga horária: 80h), Inglês I (carga horária: 40h), Matemática Discreta (carga horária: 80h)")
                .put("semestre_2", "2º ciclo/semestre: Engenharia de Software I (carga horária: 80h), Linguagem de Programação (carga horária: 80h), Sistema da Informação (carga horária: 80h), Contabilidade (carga horária: 40h), Cálculo (carga horária: 80h), Comunicação e Expressão (carga horária: 80h), Inglês II (carga horária: 40h)")
                .put("semestre_3", "3º ciclo/semestre: Engenharia de Software II (carga horária: 80h), Estrutura de Dados (carga horária: 80h), Interação Humano Computador (carga horária: 40h), Sistemas Operacionais I (carga horária: 80h), Economia e Finanças (carga horária: 40h), Estatística Aplicada (carga horária: 80h), Sociedade e Tecnologia (carga horária: 40h), Inglês III (carga horária: 40h)")
                .put("semestre_4", "4º ciclo/semestre: Engenharia de Software III (carga horária: 80h), Programação Orientada a Objetos (carga horária: 80h), Banco de Dados (carga horária: 80h), Sistemas Operacionais II (carga horária: 80h), Linguagem de Programação IV - Internet (carga horária: 80h), Metodologia da Pesquisa Científico-Tecnológica (carga horária: 40h), Inglês IV (carga horária: 40h)")
                .put("semestre_5", "5º ciclo/semestre: Gestão de Projetos (carga horária: 80h), Laboratório de Banco de Dados (carga horária: 80h), Laboratório de Engenharia de Software (carga horária: 80h), Programação para Dispositivos Móveis (carga horária: 80h), Redes de Computadores (carga horária: 80h), Segurança da Informação (carga horária: 40h), Trabalho de Graduação I (carga horária: 80h), Inglês V (carga horária: 40h)")
                .put("semestre_6", "6º ciclo/semestre: Gestão de Equipes (carga horária: 40h), Empreendedorismo (carga horária: 40h), Ética e Responsabilidade Profissional (carga horária: 40h), Inteligência Artificial (carga horária: 80h), Tópicos Especiais em Informática (carga horária: 80h), Gestão e Governança de Tecnologia da Informação (carga horária: 80h), Programação Linear e Aplicações (carga horária: 80h), Trabalho de Graduação II (carga horária: 80h), Inglês VI (carga horária: 40h)" + " | " + "Estágio de ADS: O estágio é obrigatório para o diploma e visa desenvolver competências profissionais, com 240 horas exigidas e entrega de documentação a partir do 4º semestre. Alunos que trabalham na área podem solicitar dispensa e atividades acadêmicas específicas podem contar como estágio, se previstas no projeto do curso. Há também a opção de estágio não remunerado nos setores da Fatec Praia Grande. Coordenação: Profa. Fernanda Schimitz de Almeida Larguesa (f129.estagioads@fatec.sp.gov.br).")
                .put("estagio", "Estágio de ADS: O estágio é obrigatório para o diploma e visa desenvolver competências profissionais, com 240 horas exigidas e entrega de documentação a partir do 4º semestre. Alunos que trabalham na área podem solicitar dispensa e atividades acadêmicas específicas podem contar como estágio, se previstas no projeto do curso. Há também a opção de estágio não remunerado nos setores da Fatec Praia Grande. Coordenação: Profa. Fernanda Schimitz de Almeida Larguesa (f129.estagioads@fatec.sp.gov.br).")
            );
            // Matrizes curriculares de COMEX
            matrizesCurriculares.put(new JSONObject()
                .put("semestre_1", "1º ciclo/semestre: Comércio Exterior (carga horária: 80h), Métodos para Produção do Conhecimento (carga horária: 40h), Fundamentos do Direito Público e Privado (carga horária: 80h), Administração Geral (carga horária: 80h), Matemática Aplicada ao Comércio Exterior (carga horária: 80h), Comunicação e Expressão I (carga horária: 40h), Língua Inglesa I (carga horária: 80h)")
                .put("semestre_2", "2º ciclo/semestre: Projeto em Comércio Exterior I (carga horária: 40h), Contabilidade Gerencial (carga horária: 80h), Política Comercial Externa (carga horária: 40h), Direito Internacional (carga horária: 40h), Economia (carga horária: 80h), Estatística Aplicada ao Comércio Exterior (carga horária: 80h), Comunicação e Expressão II (carga horária: 40h), Língua Inglesa II (carga horária: 80h)")
                .put("semestre_3", "3º ciclo/semestre: Projeto em Comércio Exterior II (carga horária: 40h), Economia Internacional (carga horária: 80h), Gestão Financeira (carga horária: 80h), Logística Aplicada (carga horária: 80h), Modais de Transporte e Seguro de Carga (carga horária: 80h), Espanhol I (carga horária: 40h), Língua Inglesa III (carga horária: 80h)")
                .put("semestre_4", "4º ciclo/semestre: Projeto em Comércio Exterior III (carga horária: 40h), Sistemática do Comércio Exterior (carga horária: 80h), Logística Internacional (carga horária: 80h), Legislação Aduaneira (carga horária: 80h), Marketing Internacional (carga horária: 80h), Espanhol II (carga horária: 40h), Língua Inglesa IV (carga horária: 80h)")
                .put("semestre_5", "5º ciclo/semestre: Projeto em Comércio Exterior IV (carga horária: 80h), Regimes Aduaneiros Especiais (carga horária: 40h), Gestão Ambiental Portuária (carga horária: 80h), Teoria e Prática Cambial (carga horária: 80h), Trabalho de Graduação I (carga horária: 160h), Espanhol III (carga horária: 40h), Língua Inglesa V (carga horária: 80h)")
                .put("semestre_6", "6º ciclo/semestre: Projeto em Comércio Exterior V (carga horária: 80h), Técnicas de Negociação Internacional (carga horária: 80h), Gestão Estratégica Internacional (carga horária: 80h), Mercados e Finanças Internacionais (carga horária: 80h), Gestão de Operações Portuárias (carga horária: 80h), Trabalho de Graduação II (carga horária: 80h), Espanhol IV (carga horária: 40h), Língua Inglesa VI (carga horária: 40h)")
                .put("estagio", "Estágio de COMEX: O estágio é obrigatório para o diploma, focado no desenvolvimento profissional e cidadão, com carga horária de 240 horas, sem coincidir com o horário acadêmico, e entrega de documentação a partir do 2º semestre. Alunos que trabalham na área podem solicitar dispensa, e atividades acadêmicas específicas podem contar como estágio, se previstas no projeto do curso. Há também a opção de estágio não remunerado nos setores da Fatec Praia Grande. Coordenação: Prof. Esp. Rubens Cury (f129.estagiocomex@fatec.sp.gov.br).")
            );
            // Matrizes curriculares de DSM
            matrizesCurriculares.put(new JSONObject()
                .put("semestre_1", "1º ciclo/semestre: Algoritmo e Lógica de Programação (carga horária: 80h), Modelagem de Banco de Dados (carga horária: 80h), Engenharia de Software I (carga horária: 80h), Sistemas Operacionais e Redes de Computadores (carga horária: 80h), Desenvolvimento Web I (carga horária: 80h), Design Digital (carga horária: 80h)")
                .put("semestre_2", "2º ciclo/semestre: Banco de Dados Relacional (carga horária: 80h), Estrutura de Dados (carga horária: 80h), Engenharia de Software II (carga horária: 80h), Técnicas de Programação I (carga horária: 80h), Desenvolvimento Web II (carga horária: 80h), Matemática para Computação (carga horária: 80h)")
                .put("semestre_3", "3º ciclo/semestre: Gestão Ágil de Projetos de Software (carga horária: 80h), Banco de Dados Não Relacional (carga horária: 80h), Interação Humano Computador (carga horária: 40h), Técnicas de Programação II (carga horária: 80h), Desenvolvimento Web III (carga horária: 80h), Álgebra Linear (carga horária: 80h), Inglês I (carga horária: 40h)")
                .put("semestre_4", "4º ciclo/semestre: Internet das Coisas e Aplicações (carga horária: 80h), Experiência do Usuário (carga horária: 40h), Programação de Dispositivos Móveis I (carga horária: 80h), Integração e Entrega Contínua (carga horária: 80h), Laboratório de Desenvolvimento Web (carga horária: 80h), Estatística Aplicada (carga horária: 80h), Inglês II (carga horária: 40h)")
                .put("semestre_5", "5º ciclo/semestre: Programação de Dispositivos Móveis II (carga horária: 80h), Segurança no Desenvolvimento de Aplicações (carga horária: 80h), Computação em Nuvem I (carga horária: 80h), Laboratório de Programação de Dispositivos Móveis (carga horária: 80h), Aprendizagem de Máquina (carga horária: 80h), Fundamentos da Redação Técnica (carga horária: 40h), Inglês III (carga horária: 40h)")
                .put("semestre_6", "6º ciclo/semestre: Ética Profissional e Patente (carga horária: 40h), Estágio Supervisionado em Desenvolvimento de Software Multiplataforma (carga horária: 240h), Qualidade e Testes de Software (carga horária: 80h), Computação em Nuvem II (carga horária: 80h), Processamento de Linguagem Natural (carga horária: 80h), Laboratório de Desenvolvimento Multiplataforma (carga horária: 80h), Inglês IV (carga horária: 40h)")
                .put("estagio", "Estágio de DSM: O estágio é obrigatório para o diploma, visando o desenvolvimento profissional e cidadão, com 240 horas exigidas, sem coincidência com o horário acadêmico, e entrega de documentação a partir do 1º semestre. Alunos que já trabalham na área podem solicitar dispensa, e atividades acadêmicas específicas podem contar como estágio, se previstas no projeto do curso. Há também a opção de estágio não remunerado nos setores da Fatec Praia Grande. Coordenação: Prof. Alessandro Ferreira Paz Lima (f129.estagiodsm@fatec.sp.gov.br).")
            );
            // Matrizes curriculares de GE
            matrizesCurriculares.put(new JSONObject()
                .put("semestre_1", "1º ciclo/semestre: Informática aplicada à Gestão (carga horária: 40h), Contabilidade (carga horária: 40h), Matemática (carga horária: 80h), Administração Geral (carga horária: 80h), Sociedade, Tecnologia e Inovação (carga horária: 40h), Comunicação e Expressão (carga horária: 80h), Projeto Integrador I (carga horária: 80h), Inglês (carga horária: 40h)")
                .put("semestre_2", "2º ciclo/semestre: Economia (carga horária: 80h), Gestão Ambiental (carga horária: 40h), Sociologia das Organizações (carga horária: 40h), Estatística aplicada à Gestão (carga horária: 80h), Métodos para a Produção do Conhecimento (carga horária: 40h), Comportamento Organizacional (carga horária: 80h), Projeto Integrador II (carga horária: 80h), Inglês II (carga horária: 40h)")
                .put("semestre_3", "3º ciclo/semestre: Gestão de Marketing (carga horária: 80h), Gestão de Pessoas (carga horária: 80h), Matemática Financeira (carga horária: 40h), Organização, Sistemas e Métodos (carga horária: 80h), Organização, Sistemas e Métodos - Projeto Integrador (carga horária: 80h), Sistemas de Informação (carga horária: 80h), Inglês III (carga horária: 40h)")
                .put("semestre_4", "4º ciclo/semestre: Comunicação Empresarial Geral (carga horária: 40h), Direito Empresarial (carga horária: 80h), Gestão Financeira (carga horária: 80h), Logística (carga horária: 80h), Planejamento de Marketing (carga horária: 80h), Planejamento de Marketing - Projeto Integrador (carga horária: 80h), Inglês IV (carga horária: 40h)")
                .put("semestre_5", "5º ciclo/semestre: Análise de Investimentos (carga horária: 80h), Gestão da Produção (carga horária: 80h), Fundamentos da Gestão da Qualidade (carga horária: 40h), Gestão de Projetos Empresariais (carga horária: 80h), Gestão de Projetos Empresariais - Projeto Integrador (carga horária: 80h), Projeto de Trabalho de Graduação (carga horária: 40h), Espanhol I (carga horária: 40h), Inglês V (carga horária: 40h)")
                .put("semestre_6", "6º ciclo/semestre: Desenvolvimento de Negócios (carga horária: 80h), Desenvolvimento de Negócios - Projeto Integrador VI (carga horária: 80h), Negócios Internacionais (carga horária: 80h), Planejamento e Gestão Estratégica (carga horária: 80h), Sistemas Integrados de Gestão (carga horária: 80h), Trabalho de Graduação II (carga horária: 80h), Espanhol II (carga horária: 40h), Inglês VI (carga horária: 40h)")
                .put("estagio", "Estágio de GE: O estágio é obrigatório para a obtenção do diploma, visando desenvolver competências profissionais e a vida cidadã, com carga horária total de 240 horas, sem coincidência com atividades acadêmicas, e documentação a ser entregue a partir do 4º semestre. Alunos que já atuam na área podem solicitar dispensa, e atividades acadêmicas específicas podem ser consideradas estágio, se previstas no projeto do curso. Há também a opção de estágio não remunerado na Empresa Júnior da Fatec Praia Grande. Coordenação: Prof. Marcelo Pereira de Andrade (matutino) e Prof. Denilson Luiz de Carvalho (noturno), contato: f129.estagioge@fatec.sp.gov.br.")
            );
            // Matrizes curriculares de PQ
            matrizesCurriculares.put(new JSONObject()
                .put("semestre_1", "1º ciclo/semestre: Fundamentos de Comunicação e Expressão (carga horária: 40h), Química Geral e Experimental (carga horária: 80h), Tecnologia da Informação (carga horária: 40h), Gestão Ambiental (carga horária: 40h), Fundamentos de Matemática para Cálculo (carga horária: 80h), Química Inorgânica (carga horária: 40h), Física (carga horária: 80h), Espanhol I (carga horária: 40h), Inglês I (carga horária: 40h)")
                .put("semestre_2", "2º ciclo/semestre: Físico-Química (carga horária: 80h), Desenho Técnico Assistido por Computador (carga horária: 80h), Química Orgânica (carga horária: 80h), Cálculo (carga horária: 80h), Química Analítica (carga horária: 80h), Espanhol II (carga horária: 40h), Inglês II (carga horária: 40h)")
                .put("semestre_3", "3º ciclo/semestre: Análise Instrumental (carga horária: 80h), Mecânica de Fluídos (carga horária: 80h), Microbiologia (carga horária: 40h), Transferência de Calor (carga horária: 80h), Planejamento e Controle de Produção (carga horária: 80h), Manutenção Industrial (carga horária: 80h), Inglês III (carga horária: 40h)")
                .put("semestre_4", "4º ciclo/semestre: Química do Meio Ambiente (carga horária: 80h), Estatística Descritiva (carga horária: 40h), Operações Unitárias (carga horária: 40h), Engenharia da Energia (carga horária: 40h), Bioquímica e Tecnologia das Fermentações (carga horária: 80h), Metodologia da Pesquisa Científico-Tecnológica (carga horária: 40h), Geologia e Mineralogia (carga horária: 80h), Inglês IV (carga horária: 40h)")
                .put("semestre_5", "5º ciclo/semestre: Tecnologia dos Materiais (carga horária: 80h), Controle e Garantia de Qualidade na Indústria Química (carga horária: 80h), Fundamentos de Logística (carga horária: 40h), Processos Químicos I (carga horária: 80h), Corrosão (carga horária: 80h), Trabalho de Graduação I (carga horária: 80h), Operações Unitárias Aplicadas à Processos Químicos I (carga horária: 80h), Inglês V (carga horária: 40h)")
                .put("semestre_6", "6º ciclo/semestre: Processamento de Petróleo e Gás Natural (carga horária: 80h), Legislação na Indústria Química (carga horária: 40h), Saúde e Segurança Ocupacional (carga horária: 40h), Controle de Processos Químicos (carga horária: 40h), Gestão Econômica e Administrativa na Indústria Química (carga horária: 40h), Instrumentação Industrial (carga horária: 40h), Tratamento de Efluentes (carga horária: 80h), Processo Químicos II (carga horária: 80h), Trabalho de Graduação II (carga horária: 80h), Espanhol II (carga horária: 40h), Inglês VI (carga horária: 40h)")
                .put("estagio", "Estágio de PQ: O estágio é um requisito obrigatório para a obtenção do diploma, visando ao desenvolvimento de competências profissionais e à vida cidadã. Regulamentado pela Lei Federal 11.788/2008, define as responsabilidades de todas as partes envolvidas. A carga horária do estágio é de 240 horas, sem coincidência com as atividades acadêmicas, e a documentação pode ser entregue a partir do 4º semestre. Alunos com experiência na área podem obter equivalência para o estágio, dispensando o supervisionado, desde que cumpram a carga horária. Atividades acadêmicas específicas podem ser equiparadas ao estágio, se previstas no projeto do curso. Há também a opção de estágio não remunerado na Empresa Júnior da Fatec Praia Grande. O coordenador de estágio é o Prof. Ricardo Santos de Oliveira, com contato pelo e-mail f129.estagiopq@fatec.sp.gov.br.")
            );
            
            for (int i = 0; i < cursos.length(); i++) {
                JSONObject cursoAtual = cursos.getJSONObject(i);
                JSONObject matrizCurricularCursoAtual = matrizesCurriculares.getJSONObject(i);

                int codigoCursoAtual = cursoAtual.getInt("codigo_curso");
                String matrizCurricular1SemestreCursoAtual = matrizCurricularCursoAtual.getString("semestre_1");
                String matrizCurricular2SemestreCursoAtual = matrizCurricularCursoAtual.getString("semestre_2");
                String matrizCurricular3SemestreCursoAtual = matrizCurricularCursoAtual.getString("semestre_3");
                String matrizCurricular4SemestreCursoAtual = matrizCurricularCursoAtual.getString("semestre_4");
                String matrizCurricular5SemestreCursoAtual = matrizCurricularCursoAtual.getString("semestre_5");
                String matrizCurricular6SemestreCursoAtual = matrizCurricularCursoAtual.getString("semestre_6");
                String matrizCurricularEstagioCursoAtual = matrizCurricularCursoAtual.getString("estagio");
                    
                // Inserindo as matrizes curriculares do curso
                Matrizes_Curriculares.InsertMatrizCurricular(
                    connectionBD,
                    matrizCurricular1SemestreCursoAtual,
                    codigoCursoAtual
                );                    
                Matrizes_Curriculares.InsertMatrizCurricular(
                    connectionBD,
                    matrizCurricular2SemestreCursoAtual,
                    codigoCursoAtual
                );
                Matrizes_Curriculares.InsertMatrizCurricular(
                    connectionBD,
                    matrizCurricular3SemestreCursoAtual,
                    codigoCursoAtual
                );
                Matrizes_Curriculares.InsertMatrizCurricular(
                    connectionBD,
                    matrizCurricular4SemestreCursoAtual,
                    codigoCursoAtual
                );
                Matrizes_Curriculares.InsertMatrizCurricular(
                    connectionBD,
                    matrizCurricular5SemestreCursoAtual,
                    codigoCursoAtual
                );
                Matrizes_Curriculares.InsertMatrizCurricular(
                    connectionBD,
                    matrizCurricular6SemestreCursoAtual,
                    codigoCursoAtual
                );
                Matrizes_Curriculares.InsertMatrizCurricular(
                    connectionBD,
                    matrizCurricularEstagioCursoAtual,
                    codigoCursoAtual
                );
            }
        }
    }
}