import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IDeploymentKindCatalog } from 'app/shared/model/deployment-kind-catalog.model';
import { getEntities as getDeploymentKindCatalogs } from 'app/entities/deployment-kind-catalog/deployment-kind-catalog.reducer';
import { getEntity, updateEntity, createEntity, reset } from './component-definition.reducer';
import { IComponentDefinition } from 'app/shared/model/component-definition.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ComponentDefinitionUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const deploymentKindCatalogs = useAppSelector(state => state.deploymentKindCatalog.entities);
  const componentDefinitionEntity = useAppSelector(state => state.componentDefinition.entity);
  const loading = useAppSelector(state => state.componentDefinition.loading);
  const updating = useAppSelector(state => state.componentDefinition.updating);
  const updateSuccess = useAppSelector(state => state.componentDefinition.updateSuccess);
  const handleClose = () => {
    props.history.push('/component-definition');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getDeploymentKindCatalogs({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...componentDefinitionEntity,
      ...values,
      deploymentKindCatalog: deploymentKindCatalogs.find(it => it.id.toString() === values.deploymentKindCatalog.toString()),
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
          ...componentDefinitionEntity,
          deploymentKindCatalog: componentDefinitionEntity?.deploymentKindCatalog?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="releaseManagementApp.componentDefinition.home.createOrEditLabel" data-cy="ComponentDefinitionCreateUpdateHeading">
            Create or edit a ComponentDefinition
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
                <ValidatedField name="id" required readOnly id="component-definition-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Component Id"
                id="component-definition-componentId"
                name="componentId"
                data-cy="componentId"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField
                label="Component Name"
                id="component-definition-componentName"
                name="componentName"
                data-cy="componentName"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                id="component-definition-deploymentKindCatalog"
                name="deploymentKindCatalog"
                data-cy="deploymentKindCatalog"
                label="Deployment Kind Catalog"
                type="select"
              >
                <option value="" key="0" />
                {deploymentKindCatalogs
                  ? deploymentKindCatalogs.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.deploymentKind}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/component-definition" replace color="info">
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

export default ComponentDefinitionUpdate;
