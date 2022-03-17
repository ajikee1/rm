import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity, updateEntity, createEntity, reset } from './deployment-kind-catalog.reducer';
import { IDeploymentKindCatalog } from 'app/shared/model/deployment-kind-catalog.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const DeploymentKindCatalogUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const deploymentKindCatalogEntity = useAppSelector(state => state.deploymentKindCatalog.entity);
  const loading = useAppSelector(state => state.deploymentKindCatalog.loading);
  const updating = useAppSelector(state => state.deploymentKindCatalog.updating);
  const updateSuccess = useAppSelector(state => state.deploymentKindCatalog.updateSuccess);
  const handleClose = () => {
    props.history.push('/deployment-kind-catalog');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...deploymentKindCatalogEntity,
      ...values,
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
          ...deploymentKindCatalogEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="releaseManagementApp.deploymentKindCatalog.home.createOrEditLabel" data-cy="DeploymentKindCatalogCreateUpdateHeading">
            Create or edit a DeploymentKindCatalog
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
                <ValidatedField name="id" required readOnly id="deployment-kind-catalog-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Deployment Kind"
                id="deployment-kind-catalog-deploymentKind"
                name="deploymentKind"
                data-cy="deploymentKind"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Deployment Definition"
                id="deployment-kind-catalog-deploymentDefinition"
                name="deploymentDefinition"
                data-cy="deploymentDefinition"
                type="textarea"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/deployment-kind-catalog" replace color="info">
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

export default DeploymentKindCatalogUpdate;
