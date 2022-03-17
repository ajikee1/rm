import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './component-version.reducer';
import { IComponentVersion } from 'app/shared/model/component-version.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ComponentVersion = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const componentVersionList = useAppSelector(state => state.componentVersion.entities);
  const loading = useAppSelector(state => state.componentVersion.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="component-version-heading" data-cy="ComponentVersionHeading">
        Component Versions
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Component Version
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {componentVersionList && componentVersionList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Component Version Number</th>
                <th>Component Version Status</th>
                <th>Component Id</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {componentVersionList.map((componentVersion, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${componentVersion.id}`} color="link" size="sm">
                      {componentVersion.id}
                    </Button>
                  </td>
                  <td>{componentVersion.componentVersionNumber}</td>
                  <td>{componentVersion.componentVersionStatus}</td>
                  <td>
                    {componentVersion.componentId ? (
                      <Link to={`component-definition/${componentVersion.componentId.id}`}>{componentVersion.componentId.componentId}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${componentVersion.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${componentVersion.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${componentVersion.id}/delete`}
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
          !loading && <div className="alert alert-warning">No Component Versions found</div>
        )}
      </div>
    </div>
  );
};

export default ComponentVersion;
