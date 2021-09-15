package io.rad.goaltrack.service.impl;

import io.rad.goaltrack.domain.Person;
import io.rad.goaltrack.repository.PersonRepository;
import io.rad.goaltrack.service.PersonService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Person}.
 */
@Service
public class PersonServiceImpl implements PersonService {

    private final Logger log = LoggerFactory.getLogger(PersonServiceImpl.class);

    private final PersonRepository personRepository;

    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public Person save(Person person) {
        log.debug("Request to save Person : {}", person);
        return personRepository.save(person);
    }

    @Override
    public Optional<Person> partialUpdate(Person person) {
        log.debug("Request to partially update Person : {}", person);

        return personRepository
            .findById(person.getId())
            .map(
                existingPerson -> {
                    if (person.getFirstName() != null) {
                        existingPerson.setFirstName(person.getFirstName());
                    }
                    if (person.getLastName() != null) {
                        existingPerson.setLastName(person.getLastName());
                    }
                    if (person.getEmail() != null) {
                        existingPerson.setEmail(person.getEmail());
                    }
                    if (person.getPhoneNumber() != null) {
                        existingPerson.setPhoneNumber(person.getPhoneNumber());
                    }

                    return existingPerson;
                }
            )
            .map(personRepository::save);
    }

    @Override
    public Page<Person> findAll(Pageable pageable) {
        log.debug("Request to get all People");
        return personRepository.findAll(pageable);
    }

    @Override
    public Optional<Person> findOne(String id) {
        log.debug("Request to get Person : {}", id);
        return personRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Person : {}", id);
        personRepository.deleteById(id);
    }
}
