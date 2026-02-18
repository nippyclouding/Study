<template>
    <v-container>
        <v-row justify="center">
            <v-col cols="12" md="8"> 
                <v-card>
                    <v-card-title class="text-center text-h5">
                        채팅
                    </v-card-title>
                    <v-card-text>
                        <div class="chat-box">
                            <div 
                            v-for="(msg, index) in messages"
                            :key="index"
                            :class="['chat-message', msg.senderEmail === this.senderEmail ? 'sent' : 'received']"
                            >
                                <strong>{{ msg.senderEmail }}: </strong> {{ msg.message }}
                            </div>
                        </div>
                        <v-text-field
                            v-model="newMessage"
                            label="메시지 입력"
                            @keyup.enter="sendMessage"
                        />
                        <v-btn color="primary" block @click="sendMessage">전송</v-btn>
                    </v-card-text>
                </v-card>
            </v-col>
        </v-row>
    </v-container>
</template>
<script>

// 외부 라이브러리 
import SockJS from 'sockjs-client';
import Stomp from 'webstomp-client';
//import axios from 'axios'; 

export default {
    data() {
        return {
            messages: [],  // 현재 채팅방 전체 메시지 
            newMessage: "", // 서버에 전달할 새 메시지
            stompClient: null,
            token: "",
            senderEmail: null
        }
    },
    created() { // 훅 함수 (화면이 실행할 때)
        this.senderEmail = localStorage.getItem("email");
        this.connectWebsocket(); // 화면이 실행되면 바로 웹소켓 연결 메서드 실행
    },
    beforeRouteLeave(to, from, next) { // 화면을 이동할 때 동작하는 함수
        if (this.stompClient?.connected) {
            this.stompClient.disconnect();
            next(); // 다음 화면으로 넘어간다.
        }
    },
    beforeUnmount() { // 화면을 나올 때 동작하는 함수 (dom 객체 삭제 시)
        if (this.stompClient?.connected) {
            this.stompClient.disconnect();
        }
    },
    methods: {
        connectWebsocket() {
            if (this.stompClient && this.stompClient.connected) return; // 이미 연결되어있다면 return 
            const baseUrl = process.env.VUE_APP_API_BASE_URL || "http://localhost:8080";
            const sockJs = new SockJS(`${baseUrl}/connect`);
            this.stompClient = Stomp.over(sockJs); //connect 맺기
            this.token = localStorage.getItem("token");
            this.stompClient.connect(
                {
                    Authorization : `Bearer ${this.token}`
                },
                () => {
                    // 메시지를 받아 화면에 렌더링
                    this.stompClient.subscribe(`/topic/1`, (message) => {
                        const parseMessage = JSON.parse(message.body);
                        this.messages.push(parseMessage);
                        this.scrollToBottom();
                    })
                }
            );
        },
        sendMessage() {
            if(this.newMessage.trim() === "") return; // 유효성 검증
            const message = {
                senderEmail : this.senderEmail,
                message : this.newMessage
            }
            this.stompClient.send(`/publish/1`, JSON.stringify(message)); // 메시지를 JSON으로 서버에 전달
            this.newMessage = ""; // 세 메시지를 "" 처리 (하지 않을 경우 전송 후 입력란에 글자가 그대로 보여진다.)
        },
        scrollToBottom() {
            // 채팅방에서 채팅 입력 시 스크롤 다운이 자동으로 되도록 하는 함수
            this.$nextTick(() => { 
                const chatBox = this.$el.querySelector(".chat-box");
                chatBox.scrollTop = chatBox.scrollHeight;
            })
        },
        disconnectWebSocket() {
            if (this.stompClient && this.stompClient.connected) {
                this.stompClient.disconnect(`/topic/1`);
            }
        }
    }
}

</script>
<style>
.chat-box{
    height: 300px;
    overflow-y: auto;
    border: 1px solid #ddd;
    margin-bottom: 10px;
}
.chat-message{
    margin-bottom: 10px;
}
.sent{
    text-align: right;
}
.received{
    text-align: left;
}
</style>