
console.log("script.js 파일이 정상적으로 로드되었습니다.");

function verifyPassword(button, action) {
    const boardId = button.dataset.id;

    const passwordElement = document.getElementById('commonPassword');
    const userTyped = passwordElement.value;

    if (!userTyped) {
        alert("비밀번호를 입력해주세요.");
        return;
    }

    if (action === 'delete' && !confirm("정말 삭제하시겠습니까?")) {
        return;
    }

    fetch(`/verify/${boardId}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ password: userTyped })
    })
    .then(response => response.json())
    .then(data => {
        if (data.match) {

            if (action === 'update') {
                location.href = `/update/${boardId}`;
            } else if (action === 'delete') {
                const form = document.createElement('form');
                    form.method = 'POST';
                    form.action = `/delete/${boardId}`;
                    document.body.appendChild(form);
                    form.submit();
            }
        } else {
            alert("비밀번호가 일치하지 않습니다.");
        }
    })
    .catch(error => console.error("에러 발생:", error));
}