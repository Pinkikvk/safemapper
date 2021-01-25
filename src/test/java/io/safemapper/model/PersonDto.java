package io.safemapper.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PersonDto {
    private String firstName;
    private String lastName;
    private String birthDay;
    private int age;
}
