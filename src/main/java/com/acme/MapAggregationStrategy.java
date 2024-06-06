package com.acme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

@Component
public class MapAggregationStrategy implements AggregationStrategy {

    @SuppressWarnings("unchecked")
    @Override
    public Exchange aggregate(final Exchange oldExchange, final Exchange newExchange) {
        final var newBody = newExchange.getIn().getBody();
        List<Map<String, Object>> list;

        if (oldExchange == null) {
            list = new ArrayList<>();
            final var person = (Person) newBody;
            addNewEntry(person, list);
            newExchange.getIn().setBody(list);
            return newExchange;
        } else {
            list = oldExchange.getIn().getBody(List.class);
            final var person = (Person) newBody;
            addNewEntry(person, list);
            return oldExchange;
        }
    }

    private void addNewEntry(final Person person, final List<Map<String, Object>> list) {
        final var row = new HashMap<String, Object>();
        row.put("name", person.getName());
        row.put("age", person.getAge());
        list.add(row);
    }
}
