import { IRecording } from "./recording.interface";

export interface IMessageRequest {
    id: number;
    type?: string;
    content?: string | IRecording;
    receiverId: number;
    state: String;
    avatar?: String;
    groupName?: String;
    memberIds: number[];
    roomChatId: number;
    contactId: number;
    replyTo: number;
  }