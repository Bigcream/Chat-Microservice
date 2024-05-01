import { IMessage } from "./message.interface";

export interface IRoomChat {
  id: number;
  type: string;
  name?: string;
  avatar?: String;
  admins?: number[];
  message: IMessage;
  pinnedMessage?: IMessage;
  pinnedMessageHidden?: boolean;
  replyMessage?: IMessage;
  unread?: number;
  draftMessage: string;
}