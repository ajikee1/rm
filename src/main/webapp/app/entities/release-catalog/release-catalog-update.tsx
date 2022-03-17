import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IComponentDefinition } from 'app/shared/model/component-definition.model';
import { getEntities as getComponentDefinitions } from 'app/entities/component-definition/component-definition.reducer';
import { IComponentVersion } from 'app/shared/model/component-version.model';
import { getEntities as getComponentVersions } from 'app/entities/component-version/component-version.reducer';
import { getEntity, updateEntity, createEntity, reset } from './release-catalog.reducer';
import { IReleaseCatalog } from 'app/shared/model/release-catalog.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ReleaseCatalogUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const componentDefinitions = useAppSelector(state => state.componentDefinition.entities);
  const componentVersions = useAppSelector(state => state.componentVersion.entities);
  const releaseCatalogEntity = useAppSelector(state => state.releaseCatalog.entity);
  const loading = useAppSelector(state => state.releaseCatalog.loading);
  const updating = useAppSelector(state => state.releaseCatalog.updating);
  const updateSuccess = useAppSelector(state => state.releaseCatalog.updateSuccess);
  const handleClose = () => {
    props.history.push('/release-catalog');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getComponentDefinitions({}));
    dispatch(getComponentVersions({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...releaseCatalogEntity,
      ...values,
      componentId: componentDefinitions.find(it => it.id.toString() === values.componentId.toString()),
      componentVersionNumber: componentVersions.find(it => it.id.toString() === values.componentVersionNumber.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...releaseCatalogEntity,
          componentId: releaseCatalogEntity?.componentId?.id,
          componentVersionNumber: releaseCatalogEntity?.componentVersionNumber?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="releaseManagementApp.releaseCatalog.home.createOrEditLabel" data-cy="ReleaseCatalogCreateUpdateHeading">
            Create or edit a ReleaseCatalog
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField name="id" required readOnly id="release-catalog-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Release Id"
                id="release-catalog-releaseId"
                name="releaseId"
                data-cy="releaseId"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Scheduled Date Time"
                id="release-catalog-scheduledDateTime"
                name="scheduledDateTime"
                data-cy="scheduledDateTime"
                type="date"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField id="release-catalog-componentId" name="componentId" data-cy="componentId" label="Component Id" type="select">
                <option value="" key="0" />
                {componentDefinitions
                  ? componentDefinitions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.componentId}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="release-catalog-componentVersionNumber"
                name="componentVersionNumber"
                data-cy="componentVersionNumber"
                label="Component Version Number"
                type="select"
              >
                <option value="" key="0" />
                {componentVersions
                  ? componentVersions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.componentVersionNumber}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/release-catalog" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ReleaseCatalogUpdate;
