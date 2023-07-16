package top.openfbi.mdnote;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication()
@MapperScan(basePackages = { "top.openfbi.mdnote.*.dao" })
public class MkDownJavaApplication {
    public static void main(String[] args) {
        try {
            SpringApplication.run(MkDownJavaApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}