import { IComponentDefinition } from 'app/shared/model/component-definition.model';

export interface IComponentVersion {
  id?: number;
  componentVersionNumber?: number;
  componentId?: IComponentDefinition | null;
}

export const defaultValue: Readonly<IComponentVersion> = {};
