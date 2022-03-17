import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './component-definition.reducer';
import { IComponentDefinition } from 'app/shared/model/component-definition.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ComponentDefinition = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const componentDefinitionList = useAppSelector(state => state.componentDefinition.entities);
  const loading = useAppSelector(state => state.componentDefinition.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="component-definition-heading" data-cy="ComponentDefinitionHeading">
        Component Definitions
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Component Definition
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {componentDefinitionList && componentDefinitionList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Component Id</th>
                <th>Component Name</th>
                <th>Deployment Kind Catalog</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {componentDefinitionList.map((componentDefinition, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${componentDefinition.id}`} color="link" size="sm">
                      {componentDefinition.id}
                    </Button>
                  </td>
                  <td>{componentDefinition.componentId}</td>
                  <td>{componentDefinition.componentName}</td>
                  <td>
                    {componentDefinition.deploymentKindCatalog ? (
                      <Link to={`deployment-kind-catalog/${componentDefinition.deploymentKindCatalog.id}`}>
                        {componentDefinition.deploymentKindCatalog.deploymentKind}
                      </Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${componentDefinition.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${componentDefinition.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${componentDefinition.id}/delete`}
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
          !loading && <div className="alert alert-warning">No Component Definitions found</div>
        )}
      </div>
    </div>
  );
};

export default ComponentDefinition;
