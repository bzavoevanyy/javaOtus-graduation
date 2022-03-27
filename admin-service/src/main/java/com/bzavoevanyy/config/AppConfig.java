package com.bzavoevanyy.config;

import com.yandex.ydb.auth.iam.CloudAuthProvider;
import com.yandex.ydb.core.grpc.GrpcTransport;
import com.yandex.ydb.table.TableClient;
import com.yandex.ydb.table.rpc.grpc.GrpcTableRpc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import yandex.cloud.sdk.auth.provider.ComputeEngineCredentialProvider;

@Configuration
public class AppConfig {


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public TableClient tableClient(@Value("${DATABASE}") String database,
                                   @Value("${ENDPOINT}") String endpoint
    ) {
        final var credentialProvider = ComputeEngineCredentialProvider.builder().build();
        final var authProvider = CloudAuthProvider.newAuthProvider(credentialProvider);
        final var transport = GrpcTransport.forEndpoint(endpoint, database).withAuthProvider(authProvider)
                .withSecureConnection().build();
        return TableClient.newClient(GrpcTableRpc.useTransport(transport)).build();
    }
}
