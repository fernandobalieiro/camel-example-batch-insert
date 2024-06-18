package com.acme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MapAggregationStrategy implements AggregationStrategy {

    @SuppressWarnings("unchecked")
    @Override
    public Exchange aggregate(final Exchange oldExchange, final Exchange newExchange) {
        final var newBody = newExchange.getIn().getBody();
        List<Map<String, Object>> list;

//        throw new RuntimeException("forcing an error");

        if (oldExchange == null) {
            list = new ArrayList<>();
            final var person = (Person) newBody;
            addNewEntry(person, list);
            newExchange.getIn().setBody(list);
            if (person.getAge() == 2 || person.getAge() == 7) {
                throw new RuntimeException("forcing an error");
            }
            return newExchange;
        } else {
            list = oldExchange.getIn().getBody(List.class);
            final var person = (Person) newBody;
            addNewEntry(person, list);
            if (person.getAge() == 2 || person.getAge() == 7) {
                throw new RuntimeException("forcing an error");
            }
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
