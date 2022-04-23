package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest( StocksEndpoint.class )
public class WebMvcTests {

    @MockBean
    private StockService mockStockService;

    @Autowired
    private MockMvc mockMvc;

    private Long fakeId = 1L;
    private String fakeName = "Test";
    private String fakeSymbol = "TST";

    @Test
    void testStocksEndpoint() throws Exception {

        when( this.mockStockService.findAll() ).thenReturn( List.of( new Stock( fakeId, fakeName, fakeSymbol ) ) );

        this.mockMvc
                .perform( get( "/api/stocks" ) )
                .andDo( print() )
                .andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_VALUE ) )
                .andExpect( jsonPath( "$" ).isArray() )
                .andExpect( jsonPath( "$[0].id" ).value( fakeId ) )
                .andExpect( jsonPath( "$[0].name" ).value( fakeName ) )
                .andExpect( jsonPath( "$[0].symbol" ).value( fakeSymbol ) );

        verify( this.mockStockService ).findAll();
        verifyNoMoreInteractions( this.mockStockService );

    }

    @Test
    void testStocksByIdEndpoint() throws Exception {

        when( this.mockStockService.findById( fakeId ) ).thenReturn( Optional.of( new Stock( fakeId, fakeName, fakeSymbol ) ) );

        this.mockMvc
                .perform( get( "/api/stocks/{id}", fakeId ) )
                .andDo( print() )
                .andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_VALUE ) )
                .andExpect( jsonPath( "$.id" ).value( fakeId ) )
                .andExpect( jsonPath( "$.name" ).value( fakeName ) )
                .andExpect( jsonPath( "$.symbol" ).value( fakeSymbol ) );

        verify( this.mockStockService ).findById( fakeId );
        verifyNoMoreInteractions( this.mockStockService );

    }

    @Test
    void testStocksByIdEndpointWhenIdNotFound() throws Exception {

        when( this.mockStockService.findById( fakeId ) ).thenReturn( Optional.empty() );

        this.mockMvc
                .perform( get( "/api/stocks/{id}", fakeId ) )
                .andDo( print() )
                .andExpect( status().isNotFound() );

        verify( this.mockStockService ).findById( fakeId );
        verifyNoMoreInteractions( this.mockStockService );

    }

    @Test
    void testUpdateStockNameByIdEndpoint() throws Exception {

        var fakeUpdatedName = "Test, updated";
        when( this.mockStockService.updateById( fakeId, fakeUpdatedName ) ).thenReturn( Boolean.TRUE );

        this.mockMvc
                .perform( put( "/api/stocks/{id}", fakeId ).param( "name", fakeUpdatedName ) )
                .andDo( print() )
                .andExpect( status().isAccepted() );

        verify( this.mockStockService ).updateById( fakeId, fakeUpdatedName );
        verifyNoMoreInteractions( this.mockStockService );

    }

}
