import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ComponentDefinition from './component-definition';
import ComponentDefinitionDetail from './component-definition-detail';
import ComponentDefinitionUpdate from './component-definition-update';
import ComponentDefinitionDeleteDialog from './component-definition-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ComponentDefinitionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ComponentDefinitionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ComponentDefinitionDetail} />
      <ErrorBoundaryRoute path={match.url} component={ComponentDefinition} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ComponentDefinitionDeleteDialog} />
  </>
);

export default Routes;
