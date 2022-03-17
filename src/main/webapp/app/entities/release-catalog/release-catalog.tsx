import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './release-catalog.reducer';
import { IReleaseCatalog } from 'app/shared/model/release-catalog.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ReleaseCatalog = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const releaseCatalogList = useAppSelector(state => state.releaseCatalog.entities);
  const loading = useAppSelector(state => state.releaseCatalog.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="release-catalog-heading" data-cy="ReleaseCatalogHeading">
        Release Catalogs
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Release Catalog
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {releaseCatalogList && releaseCatalogList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Release Id</th>
                <th>Scheduled Date Time</th>
                <th>Component Id</th>
                <th>Component Version Number</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {releaseCatalogList.map((releaseCatalog, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${releaseCatalog.id}`} color="link" size="sm">
                      {releaseCatalog.id}
                    </Button>
                  </td>
                  <td>{releaseCatalog.releaseId}</td>
                  <td>
                    {releaseCatalog.scheduledDateTime ? (
                      <TextFormat type="date" value={releaseCatalog.scheduledDateTime} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {releaseCatalog.componentId ? (
                      <Link to={`component-definition/${releaseCatalog.componentId.id}`}>{releaseCatalog.componentId.componentId}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {releaseCatalog.componentVersionNumber ? (
                      <Link to={`component-version/${releaseCatalog.componentVersionNumber.id}`}>
                        {releaseCatalog.componentVersionNumber.componentVersionNumber}
                      </Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${releaseCatalog.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${releaseCatalog.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${releaseCatalog.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Release Catalogs found</div>
        )}
      </div>
    </div>
  );
};

export default ReleaseCatalog;
