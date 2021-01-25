package io.safemapper.configuration;

import io.safemapper.model.Person;
import io.safemapper.model.PersonDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class MapperConfigurationTest {

    @Test
    void foo() {
        Person person = new Person();
        person.setFirstName("Jakub");
        person.setLastName("Krawczyk");

        MapperConfiguration.create(Person.class, PersonDto.class)
                .ignore(PersonDto::setLastName)
                .addMapping(PersonDto::setFirstName, Person::getFirstName)
                .addMapping(PersonDto::setAge, Person::getAge)
                .addMapping(PersonDto::setBirthDay, Person::getBirthDay, LocalDate::toString);
    }
}
