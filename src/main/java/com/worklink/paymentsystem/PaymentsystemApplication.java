package com.worklink.paymentsystem;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PaymentsystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentsystemApplication.class, args);
	}
	
}
