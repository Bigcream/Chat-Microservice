import { IAttachment } from "./attachment.interface";
import { IContact } from "../userdata";
import { IPreviewData } from "./previewData.interface";
import { IRecording } from "./recording.interface";

export interface IMessage {
    id: number;
    type?: string;
    content?: string | IRecording;
    date: string;
    sender: IContact;
    replyTo?: number;
    previewData?: IPreviewData;
    attachments?: IAttachment[];
    state: string;
    roomChatId: number
  }