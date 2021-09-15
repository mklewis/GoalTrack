package io.rad.goaltrack.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.rad.goaltrack.domain.enumeration.Status;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Goal entity.\n@author The JHipster team.
 */
@ApiModel(description = "Goal entity.\n@author The JHipster team.")
@Document(collection = "goal")
public class Goal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("title")
    private String title;

    @Field("description")
    private String description;

    @Field("target_date")
    private Instant targetDate;

    @Field("start_date")
    private Instant startDate;

    @Field("end_date")
    private Instant endDate;

    @Field("status")
    private Status status;

    @DBRef
    @Field("people")
    @JsonIgnoreProperties(value = { "goals" }, allowSetters = true)
    private Set<Person> people = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Goal id(String id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public Goal title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public Goal description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getTargetDate() {
        return this.targetDate;
    }

    public Goal targetDate(Instant targetDate) {
        this.targetDate = targetDate;
        return this;
    }

    public void setTargetDate(Instant targetDate) {
        this.targetDate = targetDate;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public Goal startDate(Instant startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public Goal endDate(Instant endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Status getStatus() {
        return this.status;
    }

    public Goal status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Set<Person> getPeople() {
        return this.people;
    }

    public Goal people(Set<Person> people) {
        this.setPeople(people);
        return this;
    }

    public Goal addPerson(Person person) {
        this.people.add(person);
        person.getGoals().add(this);
        return this;
    }

    public Goal removePerson(Person person) {
        this.people.remove(person);
        person.getGoals().remove(this);
        return this;
    }

    public void setPeople(Set<Person> people) {
        if (this.people != null) {
            this.people.forEach(i -> i.removeGoal(this));
        }
        if (people != null) {
            people.forEach(i -> i.addGoal(this));
        }
        this.people = people;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Goal)) {
            return false;
        }
        return id != null && id.equals(((Goal) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Goal{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", targetDate='" + getTargetDate() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
