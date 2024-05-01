export interface IAttachment {
  id: number;
  type: string;
  name: string;
  size: string;
  url: string;
  thumbnail?: string;
  file?: File;
}