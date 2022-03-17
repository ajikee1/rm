export interface IEnvironment {
  id?: number;
  environmentName?: string;
  environmentType?: string;
}

export const defaultValue: Readonly<IEnvironment> = {};
