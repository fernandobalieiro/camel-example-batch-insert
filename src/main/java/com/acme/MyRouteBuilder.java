package com.acme;

import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import static org.apache.camel.LoggingLevel.DEBUG;
import static org.apache.camel.model.dataformat.BindyType.Csv;

/**
 * A Camel Java DSL Router
 */
@Component
@RequiredArgsConstructor
public class MyRouteBuilder extends RouteBuilder {

    private final MapAggregationStrategy mapAggregationStrategy;

    /**
     * Let's configure the Camel routing rules using Java code...
     */
    public void configure() {
        from("file:src/data?noop=true&initialDelay=0&antInclude=*.zip").id("fromDir")
            .log(">>> Start processing zip file: ${header.CamelFileName}")
            .onCompletion()
                .log("<<< Finished processing file: ${header.CamelFileName}")
            .end()
            .unmarshal().zipFile()
            .log(">>> Start processing file: ${header.CamelFileName}")
            .split().tokenize("\n")
                .streaming().parallelProcessing()
            .unmarshal().bindy(Csv, Person.class)
            .aggregate(mapAggregationStrategy)
                .constant(true)
                .completionSize(1000)
                .completionTimeout(2000)
            .parallelProcessing()
            .log(DEBUG, "Aggregated ${body.size()} records")
            .to("sql:insert into people (name, age) values (:#name, :#age)?dataSource=#dataSource&batch=true").id("toSql")
            .log(DEBUG, "Inserted ${body.size()} records into the database")
        ;
    }

}
