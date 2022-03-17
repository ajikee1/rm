import { IComponentVersion } from 'app/shared/model/component-version.model';
import { IComponentSet } from 'app/shared/model/component-set.model';
import { IDeploymentKindCatalog } from 'app/shared/model/deployment-kind-catalog.model';

export interface IComponentDefinition {
  id?: number;
  componentId?: number;
  componentName?: string;
  componentVersions?: IComponentVersion[] | null;
  componentSets?: IComponentSet[] | null;
  deploymentKindCatalog?: IDeploymentKindCatalog | null;
}

export const defaultValue: Readonly<IComponentDefinition> = {};
