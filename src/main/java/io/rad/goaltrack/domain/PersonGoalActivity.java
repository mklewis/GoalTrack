package io.rad.goaltrack.domain;

import io.rad.goaltrack.domain.enumeration.Status;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A PersonGoalActivity.
 */
@Document(collection = "person_goal_activity")
public class PersonGoalActivity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("target_date")
    private Instant targetDate;

    @Field("start_date")
    private Instant startDate;

    @Field("end_date")
    private Instant endDate;

    @Field("status")
    private Status status;

    @DBRef
    @Field("goal")
    private Goal goal;

    @DBRef
    @Field("person")
    private Person person;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PersonGoalActivity id(String id) {
        this.id = id;
        return this;
    }

    public Instant getTargetDate() {
        return this.targetDate;
    }

    public PersonGoalActivity targetDate(Instant targetDate) {
        this.targetDate = targetDate;
        return this;
    }

    public void setTargetDate(Instant targetDate) {
        this.targetDate = targetDate;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public PersonGoalActivity startDate(Instant startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public PersonGoalActivity endDate(Instant endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Status getStatus() {
        return this.status;
    }

    public PersonGoalActivity status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Goal getGoal() {
        return this.goal;
    }

    public PersonGoalActivity goal(Goal goal) {
        this.setGoal(goal);
        return this;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    public Person getPerson() {
        return this.person;
    }

    public PersonGoalActivity person(Person person) {
        this.setPerson(person);
        return this;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PersonGoalActivity)) {
            return false;
        }
        return id != null && id.equals(((PersonGoalActivity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonGoalActivity{" +
            "id=" + getId() +
            ", targetDate='" + getTargetDate() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
