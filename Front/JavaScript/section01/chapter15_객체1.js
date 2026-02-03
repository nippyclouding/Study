// 1. 객체 생성 

let obj1 = new Object(); // 객체 생성자 내장 함수
let obj2 = {}; // 객체 리터럴 생성 방법, 자주 사용


// 2. 객체 프로퍼티 = 객체의 정보값, 속성
// { key : value }
let person = {
    name : "이정환",
    age : 27,
    hobby : "테니스",
    job : "FE Developer",

    func1 : function func1() {}, // 함수가 올 수도 있다.
    Node : {}, // 객체가 올 수도 있다. 

    10 : 20,
    "like cat" : true // key에 띄워쓰기가 있을 경우 "" 필요
}

console.log(person); // 객체 데이터 전체 확인

// 3. 객체 프로퍼티를 다루는 방법
// 3-1. 특정 프로퍼티에 접근하는 방법 : 점 표기법, 괄호 표기법

// 점 표기법 
let name = person.name; // 이정환
console.log(name); 
console.log(person.name2); // 없는 객체 데이터 접근 시 undifine

// 괄호 표기법 (동적으로 데이터를 가져올 때)
let age = person["age"]; // [] 속에 ""로 감싼 key를 넣는다. 반드시 "" 문자열 형태로 key를 넣기
let age2 = person[age]; // ""로 감싸지 않으면 age를 객체 프로퍼티가 아닌 '변수'로 취급한다. undefine

let property = "hobby";
let hobby = person[property]; // person["hobby"];
console.log(hobby); 

// 3-2. 새로운 프로퍼티를 추가하는 방법
// address, favoriteFood 데이터는 기존 객체에 없던 데이터
person.address = "서울시";
person["favoriteFood"] = "떡볶이";

console.log(person.job +" " + person.favoriteFood); 


// 3-3. 프로퍼티를 수정하는 방법
// 수정은 기존 데이터에서 값을 덮어쓰면 된다.
person.job="educator";
person["favoriteFood"] = "초콜릿";

console.log(person.job +" " + person.favoriteFood); 

// 3-4. 프로퍼티를 삭제하는 방법 : delete
delete person.job;
delete person["favoriteFood"];
console.log(person.job); // undefined
console.log(person.favoriteFood); // undefined

// 3-5. 프로퍼티의 존재 유무 확인 방법 : in 연산자
let result1 = "name" in person; // key in 객체
console.log(result1); // true

let result2 = "cat" in person;
console.log(result2); // false

let result3 = "age" in person; // int 데이터도 ""를 적용해야 한다.
console.log(result3); // true






 


