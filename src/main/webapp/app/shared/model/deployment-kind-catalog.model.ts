import { IComponentDefinition } from 'app/shared/model/component-definition.model';

export interface IDeploymentKindCatalog {
  id?: number;
  deploymentKind?: string;
  deploymentDefinition?: string;
  componentDefinitions?: IComponentDefinition[] | null;
}

export const defaultValue: Readonly<IDeploymentKindCatalog> = {};
