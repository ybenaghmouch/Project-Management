import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { ChatService } from './service/chat.service';

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

@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule, NgbModule],
  providers: [ChatService],
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit, OnDestroy {
  userId: number = 0;  // Initialize userId to a default value
  chatRooms: ChatRoom[] = [];
  selectedChatRoom: ChatRoom | null = null;
  messages: Message[] = [];
  messageContent: string = '';
  currentSubscription: any;
  selectedFile: File | null = null;

  constructor(private route: ActivatedRoute, private router: Router, private chatService: ChatService) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.userId = +params['senderId'];
      this.fetchChatRooms();

      if (params['chatRoomId']) {
        this.fetchChatRoom(+params['chatRoomId']);
      }
    });

    this.chatService.connect().then(() => {
      console.log('WebSocket connection established');
    }).catch(error => {
      console.error('WebSocket connection failed:', error);
    });
  }

  ngOnDestroy(): void {
    if (this.currentSubscription) {
      this.currentSubscription.unsubscribe();
    }
  }

  fetchChatRooms() {
    this.chatService.fetchChatRooms(this.userId).subscribe((data: ChatRoom[]) => {
      this.chatRooms = data;
    });
  }

  fetchChatRoom(chatRoomId: number) {
    this.chatService.fetchChatRoom(chatRoomId).subscribe((data: ChatRoom) => {
      this.selectedChatRoom = data;
      this.fetchMessages(chatRoomId);

      // Unsubscribe from any previous chat room subscriptions
      if (this.currentSubscription) {
        this.currentSubscription.unsubscribe();
      }

      // Subscribe to the specific chat room topic
      this.currentSubscription = this.chatService.subscribeToChatRoom(chatRoomId, (message: Message) => {
        this.messages.push(message);
      });
    });
  }

  selectChatRoom(chatRoom: ChatRoom) {
    this.router.navigate([`/chat/chatroom/${chatRoom.id}/sender/${this.userId}`]);
  }

  fetchMessages(chatRoomId: number) {
    this.chatService.fetchMessages(chatRoomId, this.userId).subscribe((data: Message[]) => {
      this.messages = data;
    });
  }

  sendMessage() {
    if (!this.selectedChatRoom) {
      return;
    }
    const message: any = {
      chatRoom: { id: this.selectedChatRoom.id },
      content: this.messageContent,
      exp: { id: this.userId },
      time: new Date()
    };

    if (this.selectedFile) {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        message.file = e.target.result;
        message.fileType = this.selectedFile?.type;

        this.chatService.sendMessage(message);
        this.messageContent = '';
        this.selectedFile = null;
      };
      reader.readAsDataURL(this.selectedFile);
    } else {
      this.chatService.sendMessage(message);
      this.messageContent = '';
    }
  }

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
  }

  getMessageSenderName(message: Message): string {
    if (!this.selectedChatRoom || !this.selectedChatRoom.users || this.selectedChatRoom.users.length <= 2) {
      return '';
    }
    return message.exp.id === this.userId 
      ? 'You' 
      : (this.selectedChatRoom.users.find((user: User) => user.id === message.exp.id)?.username || 'Unknown');
  }
}
