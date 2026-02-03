// 1. 배열 생성

let arrA = new Array(); // new Array()로 배열 선언 가능
let arrB = []; // 배열 리터럴 방식 (자주 사용)

let arrC = [1, 2, 3, true, "hello,", undefined, 
() => {}, {}, []]; 
// 배열에는 여러 데이터 타입들이 들어올 수 있다.

console.log(arrC);  // 주솟값이 아니라 데이터들이 출력

// 2. 배열 요소 접근
let item1 = arrC[0]; // 0번 인덱스 접근
console.log(item1);

let item2 = arrC[4]; 
console.log(item2);

let item3 = arrC[6];
console.log(item3);

arrC[0] = "kim"; // 배열 데이터 수정 가능