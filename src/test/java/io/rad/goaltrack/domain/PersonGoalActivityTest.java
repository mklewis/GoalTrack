package io.rad.goaltrack.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.rad.goaltrack.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PersonGoalActivityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PersonGoalActivity.class);
        PersonGoalActivity personGoalActivity1 = new PersonGoalActivity();
        personGoalActivity1.setId("id1");
        PersonGoalActivity personGoalActivity2 = new PersonGoalActivity();
        personGoalActivity2.setId(personGoalActivity1.getId());
        assertThat(personGoalActivity1).isEqualTo(personGoalActivity2);
        personGoalActivity2.setId("id2");
        assertThat(personGoalActivity1).isNotEqualTo(personGoalActivity2);
        personGoalActivity1.setId(null);
        assertThat(personGoalActivity1).isNotEqualTo(personGoalActivity2);
    }
}
