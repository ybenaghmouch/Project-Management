import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Client, IMessage } from '@stomp/stompjs';

interface User {
  id: number;
  username: string;
}

interface ChatRoom {
  id: number;
  nom: string;
  users: User[];
}

interface Message {
  id: number;
  content: string;
  exp: User;
  chatRoom: ChatRoom;
  time: Date;
  file?: string;
  fileType?: string;
}

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private stompClient: Client;

  constructor(private http: HttpClient) {
    this.stompClient = new Client({
      brokerURL: 'ws://127.0.0.1:9090/ws/websocket',
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
    });
  }

  connect(): Promise<void> {
    return new Promise((resolve, reject) => {
      this.stompClient.onConnect = () => {
        console.log('Connected to WebSocket');
        resolve();
      };
      this.stompClient.onStompError = (frame) => {
        console.error('WebSocket connection error:', frame);
        reject(frame);
      };
      this.stompClient.activate();
    });
  }

  subscribeToChatRoom(chatRoomId: number, callback: (message: Message) => void) {
    return this.stompClient.subscribe(`/topic/chatroom/${chatRoomId}`, (message: IMessage) => {
      callback(JSON.parse(message.body));
    });
  }

  sendMessage(message: any) {
    this.stompClient.publish({
      destination: '/app/chat.sendMessage',
      body: JSON.stringify(message)
    });
  }

  fetchChatRooms(userId: number) {
    return this.http.get<ChatRoom[]>(`http://127.0.0.1:9090/api/chat/rooms/user/${userId}`);
  }

  fetchChatRoom(chatRoomId: number) {
    return this.http.get<ChatRoom>(`http://127.0.0.1:9090/api/chat/room/${chatRoomId}`);
  }

  fetchMessages(chatRoomId: number, userId: number) {
    return this.http.get<Message[]>(`http://127.0.0.1:9090/api/chat/messages?chatRoomId=${chatRoomId}&exp=${userId}`);
  }
}
