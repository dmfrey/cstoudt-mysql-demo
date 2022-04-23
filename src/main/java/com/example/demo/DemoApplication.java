package com.example.demo;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class DemoApplication {

	public static void main( String[] args ) {

		SpringApplication.run( DemoApplication.class, args );

	}

}

@RestController
class StocksEndpoint {

	private final StockService stockService;

	StocksEndpoint( final StockService stockService ) {

		this.stockService = stockService;

	}

	@GetMapping( "/api/stocks" )
	@Tag( name = "/api/stocks" )
	List<Stock> stocks() {

		return this.stockService.findAll();
	}

	@GetMapping( "/api/stocks/{id}" )
	@Tag( name = "/api/stocks/{id}" )
	ResponseEntity<Stock> stockById( @PathVariable Long id ) {

		Optional<Stock> foundStock = this.stockService.findById( id );
		return foundStock.map( ResponseEntity::ok )
				.orElse( ResponseEntity.notFound().build() );
	}

	@PutMapping( "/api/stocks/{id}" )
	@Tag( name = "/api/stocks/{id}" )
	ResponseEntity<?> updateNameById( @PathVariable Long id, @RequestParam( "name" ) String name ) {

		boolean updated = this.stockService.updateById( id, name );
		if( updated ) {
			return ResponseEntity.accepted().build();
		}

		return ResponseEntity.notFound().build();
	}

}

@Repository
interface StockService extends CrudRepository<Stock, Long> {

	List<Stock> findAll();

	@Modifying
	@Query( "UPDATE stock set name = :name where id = :id" )
	boolean updateById( @Param( "id" ) Long id, @Param( "name" ) String name );

}

record Stock( @Id Long id, String name, String symbol ) { }

/**
 * This is a component that just seeds data in the db after the schema gets created
 *
 * Schema is located in `/src/main/resources/schema.sql`
 */
@Component
class ApplicationStartupSeeder implements CommandLineRunner {

	private final StockService stockService;

	ApplicationStartupSeeder( final StockService stockService ) {

		this.stockService = stockService;

	}

	@Override
	public void run( String... args ) throws Exception {

		this.stockService.saveAll( List.of(
				new Stock( null, "Tesla", "TSLA" ),
				new Stock( null, "VMware", "VMW" ),
				new Stock( null, "Google", "GOOGLE" ),
				new Stock( null, "Amazon", "AMZN" )
		));

	}

}