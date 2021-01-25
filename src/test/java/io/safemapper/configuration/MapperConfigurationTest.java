package io.safemapper.configuration;

import io.safemapper.model.Person;
import io.safemapper.model.PersonDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class MapperConfigurationTest {

    @Test
    void foo() {
        var a = int.class;
        Person person = new Person();
        person.setFirstName("Jakub");
        person.setLastName("Krawczyk");

        var mapper = MapperConfiguration.create(Person.class, PersonDto.class)
                .ignore(PersonDto::setLastName)
                .addMapping(PersonDto::setFirstName, Person::getFirstName)
                .addMapping(PersonDto::setAge, Person::getAge)
                .addMapping(PersonDto::setBirthDay, Person::getBirthDay, (d) -> "1992-07-22")
                .build();

        PersonDto personDto = new PersonDto();

        mapper.map(person, personDto);

        Assertions.assertNull(personDto.getLastName());
        Assertions.assertEquals(personDto.getFirstName(), person.getFirstName());
        Assertions.assertEquals(personDto.getAge(), person.getAge());
        Assertions.assertEquals(personDto.getBirthDay(), "1992-07-22");
    }
}
