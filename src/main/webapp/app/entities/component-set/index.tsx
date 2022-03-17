import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ComponentSet from './component-set';
import ComponentSetDetail from './component-set-detail';
import ComponentSetUpdate from './component-set-update';
import ComponentSetDeleteDialog from './component-set-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ComponentSetUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ComponentSetUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ComponentSetDetail} />
      <ErrorBoundaryRoute path={match.url} component={ComponentSet} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ComponentSetDeleteDialog} />
  </>
);

export default Routes;
