import { IContact } from "../userdata";
import { IMessage } from "./message.interface";

export interface IConversation {
    id: number;
    type: string;
    name?: string;
    avatar?: String;
    admins?: number[];
    contacts: IContact[];
    messages: IMessage[];
    pinnedMessage?: IMessage;
    pinnedMessageHidden?: boolean;
    replyMessage?: IMessage;
    unread?: number;
    draftMessage: string;
  }