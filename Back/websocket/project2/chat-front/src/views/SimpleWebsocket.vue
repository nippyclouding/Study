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

export default {
    data() {
        return {
            ws: null, 
            messages: [],  // 현재 채팅방 전체 메시지 
            newMessage: "" // 서버에 전달할 새 메시지
        }
    },
    created() { // 훅 함수 (화면이 실행할 때)
        this.connectWebsocket(); // 화면이 실행되면 바로 웹소켓 연결 메서드 실행
    },
    beforeUnmount() { // 화면이 꺼질 때 동작
        this.connectWebsocket();
    },
    methods: {
        connectWebsocket() {
            this.ws = new WebSocket("ws://localhost:8080/connect"); // 연결 시도
            // 연결 성공 시
            this.ws.onopen = () => {
                console.log("successfully connected");
            }

            this.ws.onmessage = (message) => {
                this.messages.push(message.data); 
                // messages 배열에 파라미터로 들어온 메시지를 담는다.
                // .data 로 json 데이터 -> String 데이터 변환

                this.scrollToBottom(); // 메시지가 들어올 때마다 아래로 chatbox를 내린다. (스크롤 다운))
            }

            // 연결 실패 시
            this.ws.onclose = () => {
                console.log("disconnected");
            }
        },
        sendMessage() {
            if(this.newMessage.trim() === "") return; // 유효성 검증

            this.ws.send(this.newMessage); // 메시지를 서버에 전달
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
            if (this.ws) { // ws가 존재한다면
                this.ws.close(); // 서버의 afterConnectionClosed 메서드 실행

                console.log("disconnected");
                this.ws = null;
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
</style>