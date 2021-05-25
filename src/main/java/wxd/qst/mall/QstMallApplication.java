package wxd.qst.mall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//本身是spring的组件
@MapperScan("wxd.qst.mall.dao")
@SpringBootApplication
public class QstMallApplication {
    public static void main(String[] args) {
        SpringApplication.run(QstMallApplication.class, args);
    }
}
