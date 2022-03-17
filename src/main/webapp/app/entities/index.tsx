import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ComponentDefinition from './component-definition';
import DeploymentKindCatalog from './deployment-kind-catalog';
import ComponentVersion from './component-version';
import Environment from './environment';
import ComponentSet from './component-set';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}component-definition`} component={ComponentDefinition} />
      <ErrorBoundaryRoute path={`${match.url}deployment-kind-catalog`} component={DeploymentKindCatalog} />
      <ErrorBoundaryRoute path={`${match.url}component-version`} component={ComponentVersion} />
      <ErrorBoundaryRoute path={`${match.url}environment`} component={Environment} />
      <ErrorBoundaryRoute path={`${match.url}component-set`} component={ComponentSet} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
