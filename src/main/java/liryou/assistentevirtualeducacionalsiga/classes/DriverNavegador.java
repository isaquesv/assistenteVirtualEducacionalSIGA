package liryou.assistentevirtualeducacionalsiga.classes;

import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import com.gargoylesoftware.htmlunit.WebClient;

public class DriverNavegador {
    // Atributo que armazena uma instância do HtmlUnitDriver
    private HtmlUnitDriver driver;

    // Construtor da classe que inicializa o driver ao chamar o método createDriver
    public DriverNavegador() {
        this.driver = createDriver();
    }

    // Método para criar e configurar o HtmlUnitDriver
    private HtmlUnitDriver createDriver() {
        return new HtmlUnitDriver(true) {  // Iniciando o driver com o modo JavaScript habilitado
            @Override
            protected WebClient modifyWebClient(WebClient client) {
                // Desabilita o processamento de CSS no WebClient para evitar erros relacionados ao carregamento de estilos
                client.getOptions().setCssEnabled(false);
                return client;
            }
        };
    }

    // Método para obter a instância do driver
    public HtmlUnitDriver getDriver() {
        return driver;
    }

    // Método para encerrar o driver
    public void quitDriver() {
        if (driver != null) {
            driver.quit();
        }
    }
}