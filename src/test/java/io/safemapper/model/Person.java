package io.safemapper.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Person {
    private String firstName;
    private String lastName;
    private LocalDate birthDay;
    private int age;
}
