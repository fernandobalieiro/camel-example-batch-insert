package com.acme;

import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import static org.apache.camel.LoggingLevel.DEBUG;
import static org.apache.camel.LoggingLevel.WARN;
import static org.apache.camel.model.dataformat.BindyType.Csv;

/**
 * A Camel Java DSL Router
 */
@Component
@RequiredArgsConstructor
public class MyRouteBuilder extends RouteBuilder {

    private final MapAggregationStrategy mapAggregationStrategy;

    public void configure() {

        //jtaTransactionErrorHandler().maximumRedeliveries(0).retryAttemptedLogLevel(WARN);
        //errorHandler(defaultErrorHandler().maximumRedeliveries(0).retryAttemptedLogLevel(WARN));

        from("file:src/data?noop=true&initialDelay=0&antInclude=*.zip").id("fromDir")
            .onCompletion()
                .log("<<< Finished processing file: ${header.CamelFileName}")
            .end()
            .transacted("propagationRequired")
            .to("sql:delete from people").id("deleteFromSql")
//            .process(exchange -> {
//                throw new RuntimeException("forcing an error when deleting from the database");
//            })
            //.delay(5000)
            .log(">>> Start processing zip file: ${header.CamelFileName}")
            .unmarshal().zipFile()
            .log(">>> Start processing file: ${header.CamelFileName}")
            .split().tokenize("\n")
                .streaming().parallelProcessing()
            .unmarshal().bindy(Csv, Person.class)
            .aggregate(mapAggregationStrategy)
                .constant(true)
                .completionSize(2)
                .completionTimeout(2000)
                .parallelProcessing()
            .log(DEBUG, "Aggregated ${body.size()} records")
//            .to("sql:upsert into people (name, age) values (:#name, :#age)?batch=true").id("toSql")
            .to("""
                    sql:insert into people (name, age) values (:#name, :#age) ON CONFLICT(age) DO NOTHING
                    ?batch=true""").id("toSql")
            .log(DEBUG, "Inserted ${body.size()} records into the database")
        ;
    }

}
