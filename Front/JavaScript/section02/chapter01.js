// truthy & falsy : 참 같은 값, 거짓 같은 값 

if (123) {
    console.log("123 is true"); // 출력
} else {
    console.log("123 is false");
}

if (undefined) {
    console.log("undefined is true");
} else {
    console.log("undefined is false"); // 출력
}

// JavaScript의 모든 값은 truthy 또는 falsy => 조건문을 간결하게 만들 수 있다.


// 1. falsy한 값 종류
let f1 = undefined;
let f2 = null;
let f3 = 0;
let f4 = -0;
let f5 = NaN;
let f6 = "";
let f7 = 0n; // big integer, 매우 큰 숫자에 이용

if(!f1) { // f1 ~ f7 모두 거짓 => !조건 : 참
    console.log("falsy");
}

// 2. trusy한 값 종류 
// 위 7가지 falsy한 값들을 제외한 나머지 모든 값

let t1 = "hello";
let t2 = 123;
let t3 = {}; 
let t4 = []; 
let t5 = () => {}; 

if (t5) { // t1 ~ t5 모두 참
    console.log("truthy");
}


// 3. 활용 사례

function printName(person) {
    if (person === undefined || person === null) { 
        // trusy falsy 기능이 없다면 or 연산자로 조건을 추가해야 한다.
        console.log("person 값이 비어있음");
        return;
    }

    if (!person) {
        // trusy falsy 기능 사용 시 간결한 조건문 사용
        console.log("person 값이 비어있음");
        return;
    }

    console.log(person.name);
}

let person1 = {name :  "이정환"};
printName(person1);

let person2;
printName(person2); 
// if문이 없다면
// Uncaught TypeError: Cannot read properties of undefined (reading 'name') at printName





