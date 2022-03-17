import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import DeploymentKindCatalog from './deployment-kind-catalog';
import DeploymentKindCatalogDetail from './deployment-kind-catalog-detail';
import DeploymentKindCatalogUpdate from './deployment-kind-catalog-update';
import DeploymentKindCatalogDeleteDialog from './deployment-kind-catalog-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={DeploymentKindCatalogUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={DeploymentKindCatalogUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={DeploymentKindCatalogDetail} />
      <ErrorBoundaryRoute path={match.url} component={DeploymentKindCatalog} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={DeploymentKindCatalogDeleteDialog} />
  </>
);

export default Routes;
