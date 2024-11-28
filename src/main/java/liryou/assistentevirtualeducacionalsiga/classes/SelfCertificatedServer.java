package liryou.assistentevirtualeducacionalsiga.classes;

import java.net.http.HttpClient;
import java.security.cert.X509Certificate;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.SSLContext;

public class SelfCertificatedServer {
    // Método para criar um HttpClient que desabilita a validação de certificados SSL/TLS
    public static HttpClient createHttpClient() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() { return null; }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                }
            };

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            return HttpClient.newBuilder()
                .sslContext(sslContext)
                .build();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao configurar HttpClient: " + e.getMessage());
        }
    }
}