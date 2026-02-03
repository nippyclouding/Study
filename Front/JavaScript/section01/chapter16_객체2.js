// 1. 상수 객체

const animal = {
    type : "고양이",
    name : "나비",
    color : "black"
}

//animal = {
//    a : 1
//}
// const로 선언된 상수 객체는 새로 할당 불가

//상수 객체에 새로운 프로퍼티(값)를 추가하거나 삭제, 수정하는 것은 가능하다
animal.age = 2;  // 프로퍼티 추가
animal.name = "공주"; // 프로퍼티 수정
delete animal.color; // 프로퍼티 삭제

console.log(animal); // color은 삭제되어 출력 x

// 상수 : 새로운 값을 할당하지 못한다.
// 저장되어있는 프로퍼티의 수정, 삭제, 추가는 자유롭다.


// 2. 메서드 : 값이 함수인 프로퍼티, 객체의 동작을 표현

const person = {
    name : "이정환",

    sayHi1 : function () { // 익명 함수
        console.log("hello1");
    },

    sayHi2 : () => { // 화살표 함수
        console.log("hello2");
    },

    sayHi3 () { // 메서드 선언
        console.log("hello3");
    }

}

person.sayHi1(); // 익명 함수 사용
person.sayHi2(); // 화살표 함수 사용
person.sayHi3(); // 메서드 사용
