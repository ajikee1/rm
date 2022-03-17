import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './component-definition.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ComponentDefinitionDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const componentDefinitionEntity = useAppSelector(state => state.componentDefinition.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="componentDefinitionDetailsHeading">ComponentDefinition</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{componentDefinitionEntity.id}</dd>
          <dt>
            <span id="componentId">Component Id</span>
          </dt>
          <dd>{componentDefinitionEntity.componentId}</dd>
          <dt>
            <span id="componentName">Component Name</span>
          </dt>
          <dd>{componentDefinitionEntity.componentName}</dd>
          <dt>Deployment Kind Catalog</dt>
          <dd>{componentDefinitionEntity.deploymentKindCatalog ? componentDefinitionEntity.deploymentKindCatalog.deploymentKind : ''}</dd>
        </dl>
        <Button tag={Link} to="/component-definition" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/component-definition/${componentDefinitionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ComponentDefinitionDetail;
