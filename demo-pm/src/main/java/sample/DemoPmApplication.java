package sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import sample.add.IamAccessTokenFeignApi;

/**
 * @author Joe Grandja
 * @since 0.0.1
 */
@SpringBootApplication
@EnableFeignClients(
		basePackageClasses = IamAccessTokenFeignApi.class
)
public class DemoPmApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoPmApplication.class, args);
	}

}
