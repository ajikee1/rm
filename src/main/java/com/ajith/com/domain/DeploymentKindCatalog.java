package com.ajith.com.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A DeploymentKindCatalog.
 */
@Entity
@Table(name = "deployment_kind_catalog")
public class DeploymentKindCatalog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "deployment_kind", nullable = false)
    private String deploymentKind;

    @Lob
    @Column(name = "deployment_definition", nullable = false)
    private String deploymentDefinition;

    @OneToMany(mappedBy = "deploymentKindCatalog")
    @JsonIgnoreProperties(value = { "componentVersions", "componentSets", "deploymentKindCatalog" }, allowSetters = true)
    private Set<ComponentDefinition> componentDefinitions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DeploymentKindCatalog id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeploymentKind() {
        return this.deploymentKind;
    }

    public DeploymentKindCatalog deploymentKind(String deploymentKind) {
        this.setDeploymentKind(deploymentKind);
        return this;
    }

    public void setDeploymentKind(String deploymentKind) {
        this.deploymentKind = deploymentKind;
    }

    public String getDeploymentDefinition() {
        return this.deploymentDefinition;
    }

    public DeploymentKindCatalog deploymentDefinition(String deploymentDefinition) {
        this.setDeploymentDefinition(deploymentDefinition);
        return this;
    }

    public void setDeploymentDefinition(String deploymentDefinition) {
        this.deploymentDefinition = deploymentDefinition;
    }

    public Set<ComponentDefinition> getComponentDefinitions() {
        return this.componentDefinitions;
    }

    public void setComponentDefinitions(Set<ComponentDefinition> componentDefinitions) {
        if (this.componentDefinitions != null) {
            this.componentDefinitions.forEach(i -> i.setDeploymentKindCatalog(null));
        }
        if (componentDefinitions != null) {
            componentDefinitions.forEach(i -> i.setDeploymentKindCatalog(this));
        }
        this.componentDefinitions = componentDefinitions;
    }

    public DeploymentKindCatalog componentDefinitions(Set<ComponentDefinition> componentDefinitions) {
        this.setComponentDefinitions(componentDefinitions);
        return this;
    }

    public DeploymentKindCatalog addComponentDefinition(ComponentDefinition componentDefinition) {
        this.componentDefinitions.add(componentDefinition);
        componentDefinition.setDeploymentKindCatalog(this);
        return this;
    }

    public DeploymentKindCatalog removeComponentDefinition(ComponentDefinition componentDefinition) {
        this.componentDefinitions.remove(componentDefinition);
        componentDefinition.setDeploymentKindCatalog(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeploymentKindCatalog)) {
            return false;
        }
        return id != null && id.equals(((DeploymentKindCatalog) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeploymentKindCatalog{" +
            "id=" + getId() +
            ", deploymentKind='" + getDeploymentKind() + "'" +
            ", deploymentDefinition='" + getDeploymentDefinition() + "'" +
            "}";
    }
}
