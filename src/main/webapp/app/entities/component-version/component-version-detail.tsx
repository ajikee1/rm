import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './component-version.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ComponentVersionDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const componentVersionEntity = useAppSelector(state => state.componentVersion.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="componentVersionDetailsHeading">ComponentVersion</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{componentVersionEntity.id}</dd>
          <dt>
            <span id="componentVersionNumber">Component Version Number</span>
          </dt>
          <dd>{componentVersionEntity.componentVersionNumber}</dd>
          <dt>
            <span id="componentVersionStatus">Component Version Status</span>
          </dt>
          <dd>{componentVersionEntity.componentVersionStatus}</dd>
          <dt>Component Id</dt>
          <dd>{componentVersionEntity.componentId ? componentVersionEntity.componentId.componentId : ''}</dd>
        </dl>
        <Button tag={Link} to="/component-version" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/component-version/${componentVersionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ComponentVersionDetail;
