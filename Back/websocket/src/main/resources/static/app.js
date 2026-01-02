
// 웹소켓 전용 스톰프 클라이언트
const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/endPointTest'
});
// 웹소켓 전용 팩토리에서 webSocket이 아닌 SockJS를 이용하는 스톰프
// 웹소켓 연결 실패 시 대안으로 long polling, HTTP Streaming 등 다른 기술 사용
stompClient.webSocketFactory = function () {
    return new SockJS("http://localhost:8080/endPointTest")
  };

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);

};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#messages").html("");
}

function setSubscribed1(subscribed) {
    $("#subscribe1").prop("disabled", subscribed);
    $("#unsubscribe1").prop("disabled", !subscribed);
}

function setSubscribed2(subscribed) {
    $("#subscribe2").prop("disabled", subscribed);
    $("#unsubscribe2").prop("disabled", !subscribed);
}

function connect() {
    stompClient.activate();
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

function subscribe1() {
    setSubscribed1(true);

    // index.html 에서 id가 subscribedDestination1인 값(val)을 조회
    // subscribe 을 수행할 destination 을 조회한다.
    let destination = $("#subscribedDestination1").val()

    console.log('subscribe1. destination: ' + destination)
    stompClient.subscribe(destination, (response) => {
        console.log(destination + '-> here' + '. received response: ' + response.body)
        showMessage(response.body);
    });
}

function unsubscribe1() {
    setSubscribed1(false);
    console.log("unsubscribe1");
}

function subscribe2() {
    setSubscribed2(true);
    let destination = $("#subscribedDestination2").val()
    console.log('subscribe2. destination: ' + destination)
    stompClient.subscribe(destination, (response) => {
        console.log(destination + '-> here' + '. received response: ' + response.body)
        showMessage(response.body);
    });
}

function unsubscribe2() {
    setSubscribed2(false);
    console.log("unsubscribe2");
}

function sendMessage() {
    // index.html 에서 id가 targetDestination 인 값(val)을 조회
    const targetDestination = $("#targetDestination").val();
    // index.html 에서 message 조회
    const message = $("#message").val();

    console.log("send message. targetDestination: " + targetDestination + " ,message: " + message)

    // 발행
    stompClient.publish({
        destination: targetDestination,
        body: JSON.stringify({'message': message})  // { "message": xxx }
    });
}

function showMessage(message) {
    $("#messages").append("<tr><td>" + message + "</td></tr>");
}

// index.html의 버튼들에 대한 핸들러
$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $( "#connect" ).click(() => connect());         // connect 버튼을 누르면 connect 호출
    $( "#disconnect" ).click(() => disconnect());   // disconnect 버튼을 누르면 disconnect 호출

    $( "#subscribe1" ).click(() => subscribe1());
    $( "#unsubscribe1" ).click(() => unsubscribe1());
    $( "#subscribe2" ).click(() => subscribe2());
    $( "#unsubscribe2" ).click(() => unsubscribe2());


    $( "#send" ).click(() => sendMessage());
});

