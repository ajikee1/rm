import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './release-catalog.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ReleaseCatalogDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const releaseCatalogEntity = useAppSelector(state => state.releaseCatalog.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="releaseCatalogDetailsHeading">ReleaseCatalog</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{releaseCatalogEntity.id}</dd>
          <dt>
            <span id="releaseId">Release Id</span>
          </dt>
          <dd>{releaseCatalogEntity.releaseId}</dd>
          <dt>
            <span id="scheduledDateTime">Scheduled Date Time</span>
          </dt>
          <dd>
            {releaseCatalogEntity.scheduledDateTime ? (
              <TextFormat value={releaseCatalogEntity.scheduledDateTime} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>Component Id</dt>
          <dd>{releaseCatalogEntity.componentId ? releaseCatalogEntity.componentId.componentId : ''}</dd>
          <dt>Component Version Number</dt>
          <dd>{releaseCatalogEntity.componentVersionNumber ? releaseCatalogEntity.componentVersionNumber.componentVersionNumber : ''}</dd>
        </dl>
        <Button tag={Link} to="/release-catalog" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/release-catalog/${releaseCatalogEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ReleaseCatalogDetail;
