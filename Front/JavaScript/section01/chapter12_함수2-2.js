// 함수 표현식 : 함수를 변수에 담아 표현하는 방법, 호이스팅이 되지 않는다.

function funcA() {
    console.log("funcA");
}

let varA1 = funcA(); // funcA() : 함수를 호출 (동작된다)
let varA2 = funcA; // funcA 라는 함수를 var2에 담기만 한다.

console.log(varA2); 
varA2(); // 변수를 함수처럼 사용 => 내부에 담긴 함수가 실행된다.
// varA2를 출력하면 funcA 함수의 전체 코드가 콘솔에 그대로 출력
// 함수도 하나의 값으로 취급 가능

let varB1 = function funcB() {
    console.log("funcB");
}

varB1(); // varB를 함수처럼 사용 => 내부에 담긴 함수가 실행
// funcB() : 변수에 담긴 함수이기에 사용 불가

// 익명 함수 : 함수 이름만 생략
let varB2 = function () {
    console.log("funcB");
}

// 화살표 함수 : function 선언까지 삭제, () => {}
// 한 줄이 넘어가면 {} 표시 필수
// 한 줄이 넘어가지 않는다면 {} 생략 가능
let varC1 = () => {
    console.log("varC1");
    return 1;
}

let varC2 = () => console.log("varC2");
varC2();

let varC3 = (a, b) => console.log(a + " " + b); 
varC3(3,2);

let varC4 = (value) => {
    console.log(value);
    return value + 10;
}
varC4(4);

console.log("중요한 것은 ()가 사용되었는지 확인하는 것이다.")