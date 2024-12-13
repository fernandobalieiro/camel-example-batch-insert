package com.acme;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

@Component
public class MapAggregationStrategy implements AggregationStrategy {

    private static final String NAME = "name";
    private static final String AGE = "age";

    @SuppressWarnings("unchecked")
    @Override
    public Exchange aggregate(final Exchange oldExchange, final Exchange newExchange) {
        final var person = newExchange.getIn().getBody(Person.class);
        List<Map<String, Object>> list;

        if (oldExchange == null) {
            list = new ArrayList<>();
            addNewEntry(person, list);
            newExchange.getIn().setBody(list);
            return newExchange;
        } else {
            list = oldExchange.getIn().getBody(List.class);
            addNewEntry(person, list);
            return oldExchange;
        }
    }

    private void addNewEntry(final Person person, final List<Map<String, Object>> list) {
        final var row = Map.<String, Object>of(
                NAME, person.getName(),
                AGE, person.getAge()
        );
        list.add(row);
    }
}
