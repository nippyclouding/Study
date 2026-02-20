// script.js 맨 위에 추가해서 로드 확인
console.log("script.js 파일이 정상적으로 로드되었습니다.");

function verifyPassword(button) {
    console.log("함수 호출됨! 버튼 데이터:", button.dataset.id);

    const boardId = button.dataset.id;
    const passwordElement = document.getElementById('password');

    if (!passwordElement) {
        alert("비밀번호 입력창을 찾을 수 없습니다.");
        return;
    }

    const userTyped = passwordElement.value;

    fetch(`/verify/${boardId}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ password: userTyped })
    })
    .then(response => {
        if (!response.ok) throw new Error("서버 응답 에러");
        return response.json();
    })
    .then(data => {
        if (data.match) {
            location.href = `/update/${boardId}`;
        } else {
            alert("비밀번호가 틀렸습니다.");
        }
    })
    .catch(error => console.error("에러 발생:", error));
}