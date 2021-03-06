package com.ajith.com.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A ReleaseCatalog.
 */
@Entity
@Table(name = "release_catalog")
public class ReleaseCatalog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "release_id", nullable = false)
    private String releaseId;

    @NotNull
    @Column(name = "scheduled_date_time", nullable = false)
    private LocalDate scheduledDateTime;

    @ManyToOne
    @JsonIgnoreProperties(value = { "componentVersions", "deploymentKindCatalog" }, allowSetters = true)
    private ComponentDefinition componentId;

    @ManyToOne
    @JsonIgnoreProperties(value = { "componentId" }, allowSetters = true)
    private ComponentVersion componentVersionNumber;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ReleaseCatalog id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReleaseId() {
        return this.releaseId;
    }

    public ReleaseCatalog releaseId(String releaseId) {
        this.setReleaseId(releaseId);
        return this;
    }

    public void setReleaseId(String releaseId) {
        this.releaseId = releaseId;
    }

    public LocalDate getScheduledDateTime() {
        return this.scheduledDateTime;
    }

    public ReleaseCatalog scheduledDateTime(LocalDate scheduledDateTime) {
        this.setScheduledDateTime(scheduledDateTime);
        return this;
    }

    public void setScheduledDateTime(LocalDate scheduledDateTime) {
        this.scheduledDateTime = scheduledDateTime;
    }

    public ComponentDefinition getComponentId() {
        return this.componentId;
    }

    public void setComponentId(ComponentDefinition componentDefinition) {
        this.componentId = componentDefinition;
    }

    public ReleaseCatalog componentId(ComponentDefinition componentDefinition) {
        this.setComponentId(componentDefinition);
        return this;
    }

    public ComponentVersion getComponentVersionNumber() {
        return this.componentVersionNumber;
    }

    public void setComponentVersionNumber(ComponentVersion componentVersion) {
        this.componentVersionNumber = componentVersion;
    }

    public ReleaseCatalog componentVersionNumber(ComponentVersion componentVersion) {
        this.setComponentVersionNumber(componentVersion);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReleaseCatalog)) {
            return false;
        }
        return id != null && id.equals(((ReleaseCatalog) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReleaseCatalog{" +
            "id=" + getId() +
            ", releaseId='" + getReleaseId() + "'" +
            ", scheduledDateTime='" + getScheduledDateTime() + "'" +
            "}";
    }
}
