import { INotificationPayload } from "./payload.notification.interface";


export interface INotification {
    id: number,
    payload: INotificationPayload,
    roomChatId: number;
    avatar: string;
    type: string;
    timeFormat: string;
    sender: string;
    receiver: string;
}
  