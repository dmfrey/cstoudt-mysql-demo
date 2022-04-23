package com.example.demo;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@AutoConfigureTestDatabase( replace = AutoConfigureTestDatabase.Replace.NONE )
@ActiveProfiles( "mysql" )
@Testcontainers
@Tag( "IntegrationTest" )
public class MysqlDbIntegrationTests {

    @Container
    private static MySQLContainer<?> MY_SQL_CONTAINER = new MySQLContainer<>( "mysql:latest" );

    @Autowired
    private StockService subject;

    @Test
    void integrationTest() {

        await().until( MY_SQL_CONTAINER::isRunning );

        var foundStocks = this.subject.findAll();
        assertThat( foundStocks ).hasSize( 4 );

    }

}
