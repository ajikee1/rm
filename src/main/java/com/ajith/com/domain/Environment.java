package com.ajith.com.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Environment.
 */
@Entity
@Table(name = "environment")
public class Environment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "environment_name", nullable = false)
    private String environmentName;

    @NotNull
    @Column(name = "environment_type", nullable = false)
    private String environmentType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Environment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEnvironmentName() {
        return this.environmentName;
    }

    public Environment environmentName(String environmentName) {
        this.setEnvironmentName(environmentName);
        return this;
    }

    public void setEnvironmentName(String environmentName) {
        this.environmentName = environmentName;
    }

    public String getEnvironmentType() {
        return this.environmentType;
    }

    public Environment environmentType(String environmentType) {
        this.setEnvironmentType(environmentType);
        return this;
    }

    public void setEnvironmentType(String environmentType) {
        this.environmentType = environmentType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Environment)) {
            return false;
        }
        return id != null && id.equals(((Environment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Environment{" +
            "id=" + getId() +
            ", environmentName='" + getEnvironmentName() + "'" +
            ", environmentType='" + getEnvironmentType() + "'" +
            "}";
    }
}
