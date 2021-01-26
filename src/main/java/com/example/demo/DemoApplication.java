package com.example.demo;

import com.example.demo.model.Operation;
import com.example.demo.repository.OperationRepository;
import com.sun.istack.NotNull;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import jdk.jfr.Enabled;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.annotation.Secured;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@EnableCaching
@SpringBootApplication
public class DemoApplication {

    public static int getRandIntFromInterval(int lb, int rb){
        return (int)(lb + Math.random()*(rb - lb));
    }

    public static Operation operationGenerator(){
        String[] currencies = new String[]{"AUD", "UZS", "EUR", "ALL",
        "DZD", "XCD", "AOA","ARS","AMD","AWG","AFN","BSD","BDT","BBD",
                "BHD","BZD","XOF","BGN","BOB","BIF","BTN","VES", "GHS"};

        Operation operation = new Operation(
                getRandIntFromInterval(0, 10),
                getRandIntFromInterval(8600, Integer.MAX_VALUE),
                getRandIntFromInterval(8600, Integer.MAX_VALUE),
                new BigDecimal(getRandIntFromInterval(8600, Integer.MAX_VALUE)),
                new BigDecimal(getRandIntFromInterval(8600, Integer.MAX_VALUE)),
                currencies[getRandIntFromInterval(0, currencies.length)]
                );
        operation.setPaymentCommission(operation.getPaymentCommission().divide(new BigDecimal(100)));

        return operation;
    }

    public static void generate5kOperations(OperationRepository repository) {
        List<Operation> operationList = new ArrayList<>();
        for (int i = 0; i < 5000; i++)
            operationList.add(operationGenerator());
        operationList.forEach(repository::save);
    }

    public static void main(String[] args) {
        //SpringApplication.run(DemoApplication.class, args);
        ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class);
        OperationRepository repository = context.getBean(OperationRepository.class);

        generate5kOperations(repository);

    }

    @Bean
    public OpenAPI customOpenApi(@Value("${application-description}") String appDesciption,
                                 @Value("${application-version}") String appVersion){
            return new OpenAPI()
                    .info(
                            new Info()
                            .title("Spring-boot-rest-api")
                            .version(appVersion)
                            .description(appDesciption)
                            .termsOfService("http://swagger.io/terms/")
                            .license(new License().name("Apache 2.0").url("http://springdoc.org")
                    ));

    }

}
