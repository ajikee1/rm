package com.ajith.com.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ajith.com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ComponentVersionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ComponentVersion.class);
        ComponentVersion componentVersion1 = new ComponentVersion();
        componentVersion1.setId(1L);
        ComponentVersion componentVersion2 = new ComponentVersion();
        componentVersion2.setId(componentVersion1.getId());
        assertThat(componentVersion1).isEqualTo(componentVersion2);
        componentVersion2.setId(2L);
        assertThat(componentVersion1).isNotEqualTo(componentVersion2);
        componentVersion1.setId(null);
        assertThat(componentVersion1).isNotEqualTo(componentVersion2);
    }
}
