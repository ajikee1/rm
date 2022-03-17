package com.ajith.com.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ajith.com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ComponentSetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ComponentSet.class);
        ComponentSet componentSet1 = new ComponentSet();
        componentSet1.setId(1L);
        ComponentSet componentSet2 = new ComponentSet();
        componentSet2.setId(componentSet1.getId());
        assertThat(componentSet1).isEqualTo(componentSet2);
        componentSet2.setId(2L);
        assertThat(componentSet1).isNotEqualTo(componentSet2);
        componentSet1.setId(null);
        assertThat(componentSet1).isNotEqualTo(componentSet2);
    }
}
