package com.example.jpa_hikari_r2dbc_mvc.repository;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.r2dbc.ConnectionFactoryOptionsBuilderCustomizer;
import org.springframework.boot.r2dbc.ConnectionFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;

import java.time.Duration;

import static io.r2dbc.pool.PoolingConnectionFactoryProvider.MAX_SIZE;
import static io.r2dbc.spi.ConnectionFactoryOptions.*;

@Configuration
@EnableR2dbcRepositories
class DatabaseConfiguration extends AbstractR2dbcConfiguration {

    @Value("${spring.r2dbc.username}")
    String username;

    @Value("${spring.r2dbc.password}")
    String password;

    @Value("${spring.r2dbc.max-size}")
    String maxSize;

    @Value("${spring.r2dbc.url}")
    String url;

    private static final String DB_PROTOCOL = "oracle";
    private static final String DB_DRIVER = "pool";
    private int maxClientConnections = 1000;

    @Bean
    public ConnectionFactory connectionFactory() {
        R2DBCURLSplitter myUrl = new R2DBCURLSplitter(url);
        System.out.println(myUrl.toString());
        String host = myUrl.getHost();
        Integer port = myUrl.getPort();
        String database = myUrl.getDatabase();

        ConnectionFactoryOptions options = ConnectionFactoryOptions.builder()
//                .option(CONNECT_TIMEOUT, Duration.ofSeconds(2))
                .option(DRIVER, DB_DRIVER)
                .option(PROTOCOL, DB_PROTOCOL)
                .option(MAX_SIZE, Integer.valueOf(maxSize))
                .option(HOST, host)
                .option(PORT, port)
                .option(USER, username)
                .option(PASSWORD, password)
                .option(DATABASE, database)
                .build();
        ConnectionFactoryBuilder builder = ConnectionFactoryBuilder.withOptions(options.mutate());

        return builder.build();
        //return ConnectionFactories.get(options);
        //unlimited connections below?
        //return new PostgresqlConnectionFactory(
        //        PostgresqlConnectionConfiguration.builder()
        //                .host(host)
        //                .port(port)
        //                .database(database)
        //                .username(username)
        //                .password(password)
        //                .build()
        //);
    }

    @Bean
    ReactiveTransactionManager transactionManager(ConnectionFactory connectionFactory) {
        return new R2dbcTransactionManager(connectionFactory);

    }

    @Bean
    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {

        ConnectionFactoryInitializer  initializer = new ConnectionFactoryInitializer();

        initializer.setConnectionFactory(connectionFactory);

        CompositeDatabasePopulator populator = new CompositeDatabasePopulator();
        ClassPathResource cpr = new ClassPathResource("schema.sql");
        if (cpr.exists()) {
            populator.addPopulators(new ResourceDatabasePopulator(cpr));

        }
        initializer.setDatabasePopulator(populator);

        return initializer;
    }
}