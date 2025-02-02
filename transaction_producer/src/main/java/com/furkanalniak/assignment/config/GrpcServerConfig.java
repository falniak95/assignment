package com.furkanalniak.assignment.config;

import com.furkanalniak.assignment.grpc.BankingServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcServerConfig {

    @Value("${grpc.server.port}")
    private int grpcPort;

    @Bean
    public Server grpcServer(BankingServiceImpl bankingService) throws Exception {
        Server server = ServerBuilder.forPort(grpcPort)
                .addService(bankingService)
                .build();

        server.start();
        return server;
    }
} 