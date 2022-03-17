package com.ajith.com.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ajith.com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReleaseCatalogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReleaseCatalog.class);
        ReleaseCatalog releaseCatalog1 = new ReleaseCatalog();
        releaseCatalog1.setId(1L);
        ReleaseCatalog releaseCatalog2 = new ReleaseCatalog();
        releaseCatalog2.setId(releaseCatalog1.getId());
        assertThat(releaseCatalog1).isEqualTo(releaseCatalog2);
        releaseCatalog2.setId(2L);
        assertThat(releaseCatalog1).isNotEqualTo(releaseCatalog2);
        releaseCatalog1.setId(null);
        assertThat(releaseCatalog1).isNotEqualTo(releaseCatalog2);
    }
}
