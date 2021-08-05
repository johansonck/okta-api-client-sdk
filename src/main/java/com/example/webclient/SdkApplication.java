package com.example.webclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.okta.sdk.authc.credentials.TokenClientCredentials;
import com.okta.sdk.client.AuthorizationMode;
import com.okta.sdk.client.Client;
import com.okta.sdk.client.Clients;
import com.okta.sdk.resource.user.User;
import java.io.InputStream;
import java.util.Objects;
import java.util.Set;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@SpringBootApplication
@EnableScheduling
public class SdkApplication implements ApplicationRunner {

  Logger logger = LoggerFactory.getLogger(CommandLineRunner.class);

  public static void main(String[] args) {
    SpringApplication.run(SdkApplication.class, args);
  }

  @Override
  public void run(ApplicationArguments args) {
    Client client = getClientWithOAuth(args);

    client.listUsers().forEach(user -> logger.info(toString(user)));
  }

  private Client getClientWithApiToken(ApplicationArguments args) {
    String oktaDomain = args.getOptionValues("okta-domain").stream().findFirst().orElseThrow();
    String apiToken = args.getOptionValues("api-token").stream().findFirst().orElseThrow();

    return getClientWithApiToken(oktaDomain, apiToken);
  }

  private Client getClientWithOAuth(ApplicationArguments args) {
    String oktaDomain = args.getOptionValues("okta-domain").stream().findFirst().orElseThrow();
    String clientId = args.getOptionValues("client-id").stream().findFirst().orElseThrow();

    return getClientWithOAuth(oktaDomain, clientId);
  }

  private Client getClientWithApiToken(String oktaDomain, String apiToken) {
    return Clients.builder()
        .setOrgUrl("https://" + oktaDomain)
        // see https://developer.okta.com/docs/guides/create-an-api-token/create-the-token/
        .setClientCredentials(new TokenClientCredentials(apiToken))
        .build();
  }

  private Client getClientWithOAuth(String oktaDomain, String clientId) {
    return Clients.builder()
        .setOrgUrl("https://" + oktaDomain)
        .setAuthorizationMode(AuthorizationMode.PRIVATE_KEY)
        .setClientId(clientId)
        .setScopes(Set.of("okta.users.read"))
        .setPrivateKey(getKeyStream())
        .build();
  }

  private InputStream getKeyStream() {
    return Objects.requireNonNull(getClass().getResourceAsStream("/privateKey.pem"));
  }

  @SneakyThrows
  private String toString(User user) {
    return new ObjectMapper().writeValueAsString(user);
  }
}