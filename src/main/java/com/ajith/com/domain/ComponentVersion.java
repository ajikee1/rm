package com.ajith.com.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A ComponentVersion.
 */
@Entity
@Table(name = "component_version")
public class ComponentVersion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "component_version_number", nullable = false)
    private Long componentVersionNumber;

    @ManyToOne
    @JsonIgnoreProperties(value = { "componentVersions", "deploymentKindCatalog" }, allowSetters = true)
    private ComponentDefinition componentId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ComponentVersion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getComponentVersionNumber() {
        return this.componentVersionNumber;
    }

    public ComponentVersion componentVersionNumber(Long componentVersionNumber) {
        this.setComponentVersionNumber(componentVersionNumber);
        return this;
    }

    public void setComponentVersionNumber(Long componentVersionNumber) {
        this.componentVersionNumber = componentVersionNumber;
    }

    public ComponentDefinition getComponentId() {
        return this.componentId;
    }

    public void setComponentId(ComponentDefinition componentDefinition) {
        this.componentId = componentDefinition;
    }

    public ComponentVersion componentId(ComponentDefinition componentDefinition) {
        this.setComponentId(componentDefinition);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ComponentVersion)) {
            return false;
        }
        return id != null && id.equals(((ComponentVersion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ComponentVersion{" +
            "id=" + getId() +
            ", componentVersionNumber=" + getComponentVersionNumber() +
            "}";
    }
}
