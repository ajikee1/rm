package com.ajith.com.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A ComponentSet.
 */
@Entity
@Table(name = "component_set")
public class ComponentSet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "component_set_name", nullable = false)
    private String componentSetName;

    @ManyToOne
    @JsonIgnoreProperties(value = { "componentVersions", "deploymentKindCatalog" }, allowSetters = true)
    private ComponentDefinition componentId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ComponentSet id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComponentSetName() {
        return this.componentSetName;
    }

    public ComponentSet componentSetName(String componentSetName) {
        this.setComponentSetName(componentSetName);
        return this;
    }

    public void setComponentSetName(String componentSetName) {
        this.componentSetName = componentSetName;
    }

    public ComponentDefinition getComponentId() {
        return this.componentId;
    }

    public void setComponentId(ComponentDefinition componentDefinition) {
        this.componentId = componentDefinition;
    }

    public ComponentSet componentId(ComponentDefinition componentDefinition) {
        this.setComponentId(componentDefinition);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ComponentSet)) {
            return false;
        }
        return id != null && id.equals(((ComponentSet) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ComponentSet{" +
            "id=" + getId() +
            ", componentSetName='" + getComponentSetName() + "'" +
            "}";
    }
}
