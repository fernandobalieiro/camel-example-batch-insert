package com.acme;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

@CsvRecord(separator = ",")
@Data
@NoArgsConstructor
public final class Person {
    @DataField(pos = 1, trim=true)
    private String name;

    @DataField(pos = 2, trim=true)
    private int age;


}
