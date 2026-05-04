// window : 브라우저가 제공하는 최상위 객체 (브라우저 전체 창), 창 크기 조절, 새 창 띄우기, alert 등의 동작 주체
// location : window 객체의 하위 객체, 현재 페이지의 URL(주소)을 가지고 있다. location.href 등도 가능
// search : 쿼리 스트링 조회, 요청 뒤 ? 뒷부분 데이터를 가져온다. http://localhost:8080/getBoard?boardId=1 등
const urlParams = new URLSearchParams(window.location.search);
const boardId = urlParams.get('boardId');

// 비동기 : 작업이 끝날 때까지 기다리지 않고 다음 작업
/* 
async, await : 비동기 코드를 동기 코드처럼 가독성 좋게 볼 수 있도록 하는 키워드

async : 해당 함수 속에 비동기 작업이 있다는 것을 선언, 함수 리턴이 promise 객체가 된다.
await : 비동기 함수 앞에 주로 붙인다. (fetch), 데이터가 반환될 때까지 코드가 다음 작업을 수행하지 않는다.
=> 브라우저 전체가 멈추는 것이 아니라 다른 작업들은 수행한 채 fetch 함수 부분만 다음 코드로 넘어가지 않는다.
*/

async function getBoardDetail() {
    try {
        const response = await fetch(`http://localhost:8080/board/${boardId}`);
        // fetch() : 다른 서버에 데이터 get 요청

        if (!response.ok) throw new Error("서버 응답에서 문제 발생");
        
        const data = await response.json(); // 응답 데이터 추출

        renderBoardDetail(data); // 렌더링
    } catch (error) {
        console.error("데이터를 가져오는 중 오류 발생", error);
        alert("게시글을 불러올 수 없습니다.");
    }
}

function renderBoardDetail(data) {
    document.getElementById("writer").innerText = data.writer;
    document.getElementById("created_at").innerText = data.createdAt;
    document.getElementById("board_content").innerText = data.content;
    /* 
    document : html 총괄 관리 객체, html의 모든 태그 정보가 document에 저장된다.
    DOM : document object model, 현재 HTML 파일의 각 태그들을 나무 모양의 구조로 브라우저가 생성한다.
    document : DOM의 최상위 객체
    .innerText : 태그 속 텍스트
    */
}

getBoardDetail();