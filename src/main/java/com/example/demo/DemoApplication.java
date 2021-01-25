package com.example.demo;

import com.example.demo.model.Operation;
import com.example.demo.repository.OperationRepository;
import com.sun.istack.NotNull;
import jdk.jfr.Enabled;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.access.annotation.Secured;

import java.math.BigDecimal;
import java.util.List;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        //SpringApplication.run(DemoApplication.class, args);
        ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class);
        OperationRepository repository = context.getBean(OperationRepository.class);

//        Operation operation = repository.findById(5L).orElse(new Operation(1,
//                        8600_1306,
//                        8600_1204,
//                        new BigDecimal(3_042_000),
//                        new BigDecimal(6_420), "UZS"));
//
//
//        System.out.println(operation.toString().hashCode());
//        System.out.println(operation.toString());

        List<Operation> operationList = List.of(new Operation(0, 8600_1309, 8600_1308, new BigDecimal(1_000_000), new BigDecimal(5_000), "USD"),
                new Operation(0, 8600_1309, 8600_1201, new BigDecimal(12_042_000), new BigDecimal(75_420), "UZS"),
                new Operation(1, 8600_1307, 8600_1202, new BigDecimal(1_042_000), new BigDecimal(8_420), "USD"),
                new Operation(0, 8600_1309, 8600_1203, new BigDecimal(2_042_000), new BigDecimal(7_420), "USD"),
                new Operation(1, 8600_1306, 8600_1204, new BigDecimal(3_042_000), new BigDecimal(6_420), "UZS"),
                new Operation(1, 8600_1305, 8600_1205, new BigDecimal(5_042_000), new BigDecimal(5_420), "UZS"),
                new Operation(0, 8600_1304, 8600_1206, new BigDecimal(7_042_000), new BigDecimal(3_420), "RUB"),
                new Operation(1, 8600_1303, 8600_1207, new BigDecimal(9_042_000), new BigDecimal(1_420), "RUB"));

        operationList.forEach(repository::save);


    }

}
