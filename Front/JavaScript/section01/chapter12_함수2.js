// 1. 함수 표현식

function funcA() {
    console.log("This is funcA");
}


// 함수의 결과값을 varA에 담는 것이 아닌 함수 자체를 varA에 담는다.
let varA = funcA; 
console.log(varA); // 함수 funcA() 자체의 전체 코드가 콘솔에 출력
// JS에서는 함수도 하나의 값으로 취급한다.

// let varA에 funcA()로 ()를 쓸 경우 함수 내부가 동작
// let varA에 funcA로 ()를 생략할 경우 함수 전체가 콘솔에 출력
// => funcA로 사용할 경우 하나의 값으로 사용, funcA()일 경우 함수로 사용 


// funcB()는 값으로 함수가 생성되었다
let varB = function funcB() {
    console.log("funcB");
}

// funcB(); => 값으로 함수가 생성되었기에 사용 불가
varB(); // funcB() 함수가 동작한다, varB만 사용 시 동작 x, varB()로 () 필요 

// 함수를 변수에 담기 때문에 함수의 이름을 생략 가능 = 익명 함수 
let varC = function() { 
    console.log("funcC"); 
}
varC(); // 값으로 선언된 함수를 사용 = 함수 표현식

// varD(); 함수 표현식으로 선언된 함수는 호이스팅의 대상이 되지 않는다.
let varD = function () {
    console.log("This is funcD");
}


// 2. 화살표 함수
// 함수를 더 빠르고 간결하게 생성할 수 있도록 돕는 문법

let var1 = () => {
    return 1;
}
var1(); // 1 리턴
console.log(var1()); // 리턴된 1을 출력

let var2 = () => 2; // 함수 내부 식이 하나일 경우 { return } 생략 가능  
console.log(var2());

let var3 = (value) => value + 1; // 매개변수 전달
console.log(var3(10)); // 11 출력

// 메서드가 한 줄이 아니라면 아래처럼 {return 추가 가능}
// let var3 = (value) => {
//     console.log(3);
//     return value + 3;
// }