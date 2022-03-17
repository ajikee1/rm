import dayjs from 'dayjs';
import { IComponentDefinition } from 'app/shared/model/component-definition.model';
import { IComponentVersion } from 'app/shared/model/component-version.model';

export interface IReleaseCatalog {
  id?: number;
  releaseId?: string;
  scheduledDateTime?: string;
  componentId?: IComponentDefinition | null;
  componentVersionNumber?: IComponentVersion | null;
}

export const defaultValue: Readonly<IReleaseCatalog> = {};
