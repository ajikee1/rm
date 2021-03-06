import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import authentication from './authentication';
import applicationProfile from './application-profile';

import administration from 'app/modules/administration/administration.reducer';
import userManagement from 'app/modules/administration/user-management/user-management.reducer';
import register from 'app/modules/account/register/register.reducer';
import activate from 'app/modules/account/activate/activate.reducer';
import password from 'app/modules/account/password/password.reducer';
import settings from 'app/modules/account/settings/settings.reducer';
import passwordReset from 'app/modules/account/password-reset/password-reset.reducer';
// prettier-ignore
import componentDefinition from 'app/entities/component-definition/component-definition.reducer';
// prettier-ignore
import deploymentKindCatalog from 'app/entities/deployment-kind-catalog/deployment-kind-catalog.reducer';
// prettier-ignore
import componentVersion from 'app/entities/component-version/component-version.reducer';
// prettier-ignore
import environment from 'app/entities/environment/environment.reducer';
// prettier-ignore
import componentSet from 'app/entities/component-set/component-set.reducer';
// prettier-ignore
import releaseCatalog from 'app/entities/release-catalog/release-catalog.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const rootReducer = {
  authentication,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  componentDefinition,
  deploymentKindCatalog,
  componentVersion,
  environment,
  componentSet,
  releaseCatalog,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar,
};

export default rootReducer;
