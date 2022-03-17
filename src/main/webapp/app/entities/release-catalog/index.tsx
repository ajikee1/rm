import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ReleaseCatalog from './release-catalog';
import ReleaseCatalogDetail from './release-catalog-detail';
import ReleaseCatalogUpdate from './release-catalog-update';
import ReleaseCatalogDeleteDialog from './release-catalog-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ReleaseCatalogUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ReleaseCatalogUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ReleaseCatalogDetail} />
      <ErrorBoundaryRoute path={match.url} component={ReleaseCatalog} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ReleaseCatalogDeleteDialog} />
  </>
);

export default Routes;
