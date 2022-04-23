package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
public class StockServiceTests {

    @Autowired
    private StockService subject;

    @BeforeEach
    void setup() {

        this.subject.saveAll( List.of(
                new Stock( null, "Test 1", "TST1" ),
                new Stock( null, "Test 2", "TST2" ),
                new Stock( null, "Test 3", "TST3" ),
                new Stock( null, "Test 4", "TST4" )
        ));

    }

    @Test
    void testFindAll() {

        var actual = this.subject.findAll();

        assertThat( actual ).hasSize( 4 );

    }

    @Test
    void testUpdateById() {

        var actual = this.subject.updateById( 1L, "Test 1, Updated" );

        assertThat( actual ).isTrue();

    }
}
