import { IComponentDefinition } from 'app/shared/model/component-definition.model';

export interface IComponentSet {
  id?: number;
  componentSetName?: string;
  componentId?: IComponentDefinition | null;
}

export const defaultValue: Readonly<IComponentSet> = {};
