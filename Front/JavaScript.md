JavaScript
- 객체 기반의 스크립트 언어
- AJAX의 출현으로 웹 개발에 필수적인 프로그래밍 언어가 되었다.

Client 요청 -> Server의 HTML, CSS, JS 응답 -> 브라우저는 Client에게 화면을 띄워준다.
웹 브라우저는 JS 언어를 Client에게 띄워주기 위해 JavaScript 인터프리터를 내장하고 있다.

JS에서는 ; 를 사용하지 않아도 된다.

------------------------------------------------------------------------------------------------------------------

JS의 자료형
- primitive : Number, String, Boolean, undefined(자료형이 정해지지 않음), null .. 
- object : Date, Array, Object ..
자료형이 크게 중요하지 않다, 변수 선언 시 var로 선언해야 하기 때문

리터럴 : 값을 직접 명시해서 선언, 할당

JS에서 변수의 선언 : var

var x;
var x = 40;
var x = 'hello';

JS에서 비교 연산자 
- == : 값이 동일하다.
- != : 값이 다르다.
- === : 값과 자료형이 동일하다.

JS에서 입출력
- prompt("입력이 들어갑니다.");
- alert("출력입니다.");

입출력 예시
<script>
    prompt("write your word:");
    alert("And this is output");

    var x = prompt("write your word:");
    alert("Your word is "+ x);
</script>

응용
<script>
    var x = 10;
    var input = prompt("수를 맞추어 주세요.");
    console.log(input);

    if(input>x) alert("더 작은 값 입니다.");
    else if(input<x) alert("더 큰 값 입니다.");
    else alert("정답입니다.");

    var gender = prompt("성별을 입력해주세요.")
    switch (gender){
        case "여자" : alert('Female'); break;
        case "남자" : alert('Male'); break;
        case "기타" : alert('Other'); break;
        default : alert("");
    }
</script>

------------------------------------------------------------------------------------------------------------------
JS의 반복문 - 피보나치 수열

실행 후 chrome - 도구 더보기 - 개발자 도구 - Console에서 결과 확인

for문 반복
<script>
    var n_1 = 1;
    var n_2 = 1;
    console.log(n_1);
    console.log(n_2);

    for(var i=0; i<100; i++){
        var n = n_1 + n_2;
        console.log(n);
        n_2 = n_1;
        n_1 = n;
    }
</script>

while 반복
<script>
    var n_1 = 1;
    var n_2 = 1;

    console.log(n_1);
    console.log(n_2);

    var i=0;
    while(i<100){
        var n = n_1 + n_2;
        console.log(n);
        n_2 = n_1;
        n_1 = n;
        i++
    }


 //배열
    var arr1 = [4, 0, 3];
    var a = arr1.length;
    arr1.push(5);//배열 맨 끝에 데이터 추가
    arr1.unshift(1);//배열 맨 처음에 데이터 삽입
    var removeFirst = arr1.shift();//배열 맨 처음 데이터 삭제, 리턴
    var removeData = arr1.pop();//배열 맨 끝 데이터 삭제, 리턴

    //객체
    var obj1 = {username:"이상원", age:20}
    obj1.username = "김때둥";
    obj1.online = true; //변수 추가, 할당


</script>



-------------------------------------------------------
실습
<!doctype html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
</head>
<body>
<script>
    var list = [
        {question: "가장 좋아하는 과일을 선택해주세요.",
            option : ["사과", "복숭아", "포도"]
        },
        {question: "가장 좋아하는 반려 동물을 선택해주세요.",
            option : ["강아지","고양이", "곰"]
        }
    ];

    console.log("초기 list 상태 :", list);

    for(var i = 0; i<list.length; i++){
        var questionMessage = list[i].question +'\n';

        for(var j = 0; j<list[i].option.length; j++){
            questionMessage+=(j+1) +'. ' + list[i].option[j] + '\n';
        }
        list[i].answer = prompt(questionMessage);
    }

    for(var i = 0; i<list.length; i++){
        var index = parseInt(list[i].answer);
        var message = list[i].question + '\n당신의 선택 : ' + list[i].option[index-1];
        alert(message);
    }



</script>
</body>
</html>



------------------------------------------------------------------------------------------------------------------
JS에서의 함수

function 함수명(매개변수) {
    실행 코드
}

function converDollarToWon{
    return (dollar*1201.2);
}

function printMe(){
    alert("안녕하세요");
}    


<console에서 사용>
function converDollarToWon{
    return (dollar*1201.2);
}
var input = promt('달러화를 입력해주세요');
input
input = parseInt(input);
convertDollarToWon(input)

 scope
- var로 변수 선언 : 함수 블록 안에서 선언 시 지역 변수, 밖에서 선언 시 전역 변수
함수가 아닌 if, for문 안에서 선언을 해도 전역 변수 성질을 가진다.

- let으로 변수 선언 : 블록 내에서만 효과가 있는 지역 변수 (for, if문 안에서 선언 시 동일하게 지역 변수 효과)

- const로 변수 선언 : let과 같이 지역 변수 성질 + 수정 불가(final과 유사)






<JS 작성 방식>
- 인라인 스타일 : html 태그에 이벤트 속성으로 직접 작성
<button onclick="alert('버튼이 클릭되었습니다!');">클릭하세요</button>

- 내부 스크립트 : <script> 태그 내부에 작성
<head>
    <script>
        console.log("안녕하세요! 내부 스크립트입니다.");
        function sayHello() {
            alert("웹 페이지에 오신 것을 환영합니다!");
        }
        // 페이지 로드 시 바로 실행하고 싶다면 sayHello(); 헤드에 입력하면 바로 실행된다.
    </script>
</head>
<body>
    <button onclick="sayHello()">인사하기</button>
</body>

- 외부 스크립트 : js 파일 작성 후 <script> 태그로 import (가장 권장되는 방식)

main.js 파일
console.log("안녕하세요! 외부 스크립트입니다.");

function showMessage() {
    alert("외부 파일에서 불러온 메시지입니다!");
}

html 파일
<body>
    <!-- ... html 코드 -->
    <script src="main.js"></script>
    <script type="text/javascript" src = "js 파일 위치"></script>
<body>


------------------------------------------------------------------------------------------------------------------
DOM : Document Object Model
HTML 문서를 JS가 조작할 수 있게 만든 객체 트리 구조
document : HTML의 최상위 root 객체
element : HTML document 하위의 화면 요소(html tag로 그려지는 요소)



<!DOCTYPE html>
<html>
<head>
    <title>제목</title>
</head>
<body>
    <div id="container">
        <p>안녕하세요</p>
    </div>
</body>
</html>


DOM에서의 트리구조
document
└── html
    ├── head
    │   └── title
    └── body
        └── div#container
            └── p

DOM 접근 함수
- document.getElementById(elementId) : 요소의 id값을 조회
- document.getElementByTagName(name) : 요소의 태그 종류로 요소들을 가져온다, 배열 list 형태(nodelist)
- document.getElementsByClassName(className) : 요소의 class 값으로 요소들을 가져온다, 배열 list 형태(nodelist)
- document.querySelector(selector) : 셀렉터 문법으로 문서 내 요소들을 가져온다.

1. ID로 선택
const element = document.getElementById('container');
2. 클래스로 선택 (하나의 요소)
const element = document.querySelector('.my-class');
3. 클래스로 선택 (모든 요소)
const elements = document. querySelectorAll('.my-class');
4. 태그로 선택
const paragraphs = documents.getElementsByTagName('p');


google.com 접속, 개발자 도구 -> Console(inspector on) 
var googleLogo = document.getElementById('hplogo');
googleLogo => 정보들이 출력된다.
-----------------------------------------------------------------------------------------------------------------


