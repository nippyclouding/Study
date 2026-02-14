// 배열 메서드 1. 요소 조작

// 6가지 요소 조작 메서드 

//1. push : 배열의 맨 뒤에 새로운 요소를 추가하는 메서드
// 리턴 : 현재 배열의 길이
let arr1 = [1, 2, 3];
let length4 = arr1.push(4);
let length56 = arr1.push(5,6);

console.log(length4); // 현재 배열의 길이
console.log(length56); 

console.log("push 함수 :" + arr1);

//2. pop : 배열의 맨 뒤에 있는 요소를 제거, 반환

let arr2 = [1, 2, 3];
let pop = arr2.pop(); // 3 pop

console.log(pop);
console.log(arr2.pop()); // 2 pop

//3. shift : 배열의 맨 앞에 있는 요소 제거, 반환 (Java Q의 poll과 동일)
let arr3 = [1, 2, 3];
let shift = arr3.shift(); // 1 출력
console.log(shift);

//4. unshift : 배열의 맨 앞에 새로운 요소 추가 (Java arrayDeque의 addFirst와 동일)
let arr4 = [1, 2, 3];
let newLength = arr4.unshift(0); // 리턴 : 추가한 뒤 배열 길이
console.log(arr4); 


//5. slice : 배열을 자른 뒤 자른 배열을 리턴
let arr5 = [1, 2, 3, 4, 5];
let sliceArr = arr5.slice(2, 5); // 시작 인덱스, 끝 인덱스 + 1 (중요)
// 두 번째 파라미터 생략 시 배열의 끝까지 자른다.
let sliceArr2 = arr5.slice(-1); // 마지막 인덱스인 5만 자른다.
console.log(sliceArr);


//6. concat : 서로 다른 두 개의 배열을 이어서 새로운 배열 반환
let arr6 = [1, 2];
let arr7 = [3, 4];

let concatedArr = arr6.concat(arr7);
console.log(concatedArr); // 1, 2, 3, 4 반환