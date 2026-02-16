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
                            >
                                {{ msg }}
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
            stompClient: null
        }
    },
    created() { // 훅 함수 (화면이 실행할 때)
        this.connectWebsocket(); // 화면이 실행되면 바로 웹소켓 연결 메서드 실행
    },
    beforeUnmount() {
        if (this.stompClient?.connected) {
            this.stompClient.disconnect();
        }
    },
    methods: {
        connectWebsocket() {
            const baseUrl = process.env.VUE_APP_API_BASE_URL || "http://localhost:8080";
            const sockJs = new SockJS(`${baseUrl}/connect`);
            this.stompClient = Stomp.over(sockJs); //connect 맺기
            
            this.stompClient.connect(
                {},
                () => {
                    // 메시지를 받아 화면에 렌더링
                    this.stompClient.subscribe(`/topic/1`, (message) => {
                        this.messages.push(message.body);
                        this.scrollToBottom();
                    })
                }
            );
        },
        sendMessage() {
            if(this.newMessage.trim() === "") return; // 유효성 검증
            this.stompClient.send(`/publish/1`, this.newMessage); // 메시지를 서버에 전달
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
            // if (this.ws) { // ws가 존재한다면
            //     this.ws.close(); // 서버의 afterConnectionClosed 메서드 실행

            //     console.log("disconnected");
            //     this.ws = null;
            // }
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
</style>