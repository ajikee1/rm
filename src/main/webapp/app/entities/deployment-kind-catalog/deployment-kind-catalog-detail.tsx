import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './deployment-kind-catalog.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const DeploymentKindCatalogDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const deploymentKindCatalogEntity = useAppSelector(state => state.deploymentKindCatalog.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="deploymentKindCatalogDetailsHeading">DeploymentKindCatalog</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{deploymentKindCatalogEntity.id}</dd>
          <dt>
            <span id="deploymentKind">Deployment Kind</span>
          </dt>
          <dd>{deploymentKindCatalogEntity.deploymentKind}</dd>
          <dt>
            <span id="deploymentDefinition">Deployment Definition</span>
          </dt>
          <dd>{deploymentKindCatalogEntity.deploymentDefinition}</dd>
        </dl>
        <Button tag={Link} to="/deployment-kind-catalog" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/deployment-kind-catalog/${deploymentKindCatalogEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default DeploymentKindCatalogDetail;
