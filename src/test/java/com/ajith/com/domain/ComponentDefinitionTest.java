package com.ajith.com.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ajith.com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ComponentDefinitionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ComponentDefinition.class);
        ComponentDefinition componentDefinition1 = new ComponentDefinition();
        componentDefinition1.setId(1L);
        ComponentDefinition componentDefinition2 = new ComponentDefinition();
        componentDefinition2.setId(componentDefinition1.getId());
        assertThat(componentDefinition1).isEqualTo(componentDefinition2);
        componentDefinition2.setId(2L);
        assertThat(componentDefinition1).isNotEqualTo(componentDefinition2);
        componentDefinition1.setId(null);
        assertThat(componentDefinition1).isNotEqualTo(componentDefinition2);
    }
}
