package top.openfbi.mdnote.config;


import nl.altindag.ssl.SSLFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

/**
 * ElasticSearch 客户端配置
 */
@Configuration
public class RestClientConfig extends ElasticsearchConfiguration {
    @Value(value = "${es.user}")
    private String user;
    @Value(value = "${es.password}")
    private String password;
    @Value(value = "${es.url}")
    private String url;
    @Value(value = "${es.port}")
    private String port;

    /**
     * 设置elastic数据库的链接秘钥和账户
     */
    @Override
    public ClientConfiguration clientConfiguration() {
        SSLFactory sslFactory = SSLFactory.builder()
                .withUnsafeTrustMaterial()
                .withUnsafeHostnameVerifier()
                .build();

        return ClientConfiguration.builder()
                .connectedTo(url + ':' + port)
                .usingSsl(sslFactory.getSslContext(), sslFactory.getHostnameVerifier())
                .withBasicAuth(user, password)
                .build();
    }
}
