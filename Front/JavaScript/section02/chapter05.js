// JS에서 자료형 : 원시 타입, 객체 타입으로 구분된다 => 값이 저장, 복사되는 과정이 서로 다르다.

// 원시 타입 : 값 자체로 변수에 저장, 복사, 불변값 (메모리 값의 원본 데이터는 변경되지 않는다.) call by value
// 객체 타입 : 주소값(참조값)으로 변수에 저장, 복사 (가변값) call by reference

// 객체 타입 주의 사항 : 참조에 의한 복사 시 의도치 않게 값이 수정될 수 있다.

let o1 = {name : "kim"};
let o2 = o1; // 얕은 복사 : 객체 참조값 복사

o1.name = "lee";
console.log(o2.name); // lee로 변경되어있다.

// 메모리를 공유하지 않고 새로운 객체를 생성하면 side effect가 x
let o3 = {...o1}; // 깊은 복사 : 새로운 객체를 복사하며 값(프로퍼티)만 복사 (엄밀히 말하면 중첩 객체 상황에서는 얕은 복사)
// 새로운 객체 생성, spread 연산자로 o1 객체 값 복사
console.log("before change " + o3.name);

o1.name = "choi";
console.log("after change : " + o3.name); // 변화 x
o1.name = "lee"; // 원래대로 변경

// 객체 타입 주의 사항 : 객체 간 비교는 기본적으로 '참조값'을 기준
isSame1 = o1 === o2;
console.log(isSame1); // true 출력, 같은 참조값 공유

isSame2 = o1 === o3;
console.log(isSame2); // false 출력, 다른 참조값 공유

// JSON.stringify : 주솟값이 다른 객체의 프로퍼티들을 비교, true 출력
console.log (JSON.stringify(o1) === JSON.stringify(o3)); 

// 배열, 함수도 객체로 동작 => 일반 객체에 존재하는 프로퍼티, 메서드를 가질 수 있따.
