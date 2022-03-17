import dayjs from 'dayjs';

export interface IReleaseCatalog {
  id?: number;
  releaseId?: string;
  scheduledDateTime?: string;
}

export const defaultValue: Readonly<IReleaseCatalog> = {};
