package com.ajith.com.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ajith.com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DeploymentKindCatalogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeploymentKindCatalog.class);
        DeploymentKindCatalog deploymentKindCatalog1 = new DeploymentKindCatalog();
        deploymentKindCatalog1.setId(1L);
        DeploymentKindCatalog deploymentKindCatalog2 = new DeploymentKindCatalog();
        deploymentKindCatalog2.setId(deploymentKindCatalog1.getId());
        assertThat(deploymentKindCatalog1).isEqualTo(deploymentKindCatalog2);
        deploymentKindCatalog2.setId(2L);
        assertThat(deploymentKindCatalog1).isNotEqualTo(deploymentKindCatalog2);
        deploymentKindCatalog1.setId(null);
        assertThat(deploymentKindCatalog1).isNotEqualTo(deploymentKindCatalog2);
    }
}
