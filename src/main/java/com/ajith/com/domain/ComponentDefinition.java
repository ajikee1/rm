package com.ajith.com.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A ComponentDefinition.
 */
@Entity
@Table(name = "component_definition")
public class ComponentDefinition implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "component_id", nullable = false)
    private Long componentId;

    @NotNull
    @Column(name = "component_name", nullable = false)
    private String componentName;

    @OneToMany(mappedBy = "componentId")
    @JsonIgnoreProperties(value = { "componentId" }, allowSetters = true)
    private Set<ComponentVersion> componentVersions = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "componentDefinitions" }, allowSetters = true)
    private DeploymentKindCatalog deploymentKindCatalog;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ComponentDefinition id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getComponentId() {
        return this.componentId;
    }

    public ComponentDefinition componentId(Long componentId) {
        this.setComponentId(componentId);
        return this;
    }

    public void setComponentId(Long componentId) {
        this.componentId = componentId;
    }

    public String getComponentName() {
        return this.componentName;
    }

    public ComponentDefinition componentName(String componentName) {
        this.setComponentName(componentName);
        return this;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public Set<ComponentVersion> getComponentVersions() {
        return this.componentVersions;
    }

    public void setComponentVersions(Set<ComponentVersion> componentVersions) {
        if (this.componentVersions != null) {
            this.componentVersions.forEach(i -> i.setComponentId(null));
        }
        if (componentVersions != null) {
            componentVersions.forEach(i -> i.setComponentId(this));
        }
        this.componentVersions = componentVersions;
    }

    public ComponentDefinition componentVersions(Set<ComponentVersion> componentVersions) {
        this.setComponentVersions(componentVersions);
        return this;
    }

    public ComponentDefinition addComponentVersion(ComponentVersion componentVersion) {
        this.componentVersions.add(componentVersion);
        componentVersion.setComponentId(this);
        return this;
    }

    public ComponentDefinition removeComponentVersion(ComponentVersion componentVersion) {
        this.componentVersions.remove(componentVersion);
        componentVersion.setComponentId(null);
        return this;
    }

    public DeploymentKindCatalog getDeploymentKindCatalog() {
        return this.deploymentKindCatalog;
    }

    public void setDeploymentKindCatalog(DeploymentKindCatalog deploymentKindCatalog) {
        this.deploymentKindCatalog = deploymentKindCatalog;
    }

    public ComponentDefinition deploymentKindCatalog(DeploymentKindCatalog deploymentKindCatalog) {
        this.setDeploymentKindCatalog(deploymentKindCatalog);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ComponentDefinition)) {
            return false;
        }
        return id != null && id.equals(((ComponentDefinition) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ComponentDefinition{" +
            "id=" + getId() +
            ", componentId=" + getComponentId() +
            ", componentName='" + getComponentName() + "'" +
            "}";
    }
}
