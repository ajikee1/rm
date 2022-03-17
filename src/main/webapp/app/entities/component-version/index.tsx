import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ComponentVersion from './component-version';
import ComponentVersionDetail from './component-version-detail';
import ComponentVersionUpdate from './component-version-update';
import ComponentVersionDeleteDialog from './component-version-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ComponentVersionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ComponentVersionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ComponentVersionDetail} />
      <ErrorBoundaryRoute path={match.url} component={ComponentVersion} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ComponentVersionDeleteDialog} />
  </>
);

export default Routes;
