import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IComponentDefinition } from 'app/shared/model/component-definition.model';
import { getEntities as getComponentDefinitions } from 'app/entities/component-definition/component-definition.reducer';
import { getEntity, updateEntity, createEntity, reset } from './component-set.reducer';
import { IComponentSet } from 'app/shared/model/component-set.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ComponentSetUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const componentDefinitions = useAppSelector(state => state.componentDefinition.entities);
  const componentSetEntity = useAppSelector(state => state.componentSet.entity);
  const loading = useAppSelector(state => state.componentSet.loading);
  const updating = useAppSelector(state => state.componentSet.updating);
  const updateSuccess = useAppSelector(state => state.componentSet.updateSuccess);
  const handleClose = () => {
    props.history.push('/component-set');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getComponentDefinitions({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...componentSetEntity,
      ...values,
      componentId: componentDefinitions.find(it => it.id.toString() === values.componentId.toString()),
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
          ...componentSetEntity,
          componentId: componentSetEntity?.componentId?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="releaseManagementApp.componentSet.home.createOrEditLabel" data-cy="ComponentSetCreateUpdateHeading">
            Create or edit a ComponentSet
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
                <ValidatedField name="id" required readOnly id="component-set-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Component Set Name"
                id="component-set-componentSetName"
                name="componentSetName"
                data-cy="componentSetName"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField id="component-set-componentId" name="componentId" data-cy="componentId" label="Component Id" type="select">
                <option value="" key="0" />
                {componentDefinitions
                  ? componentDefinitions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.componentId}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/component-set" replace color="info">
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

export default ComponentSetUpdate;
