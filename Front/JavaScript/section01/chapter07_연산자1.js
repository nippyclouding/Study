// 연산자 (java와 같은 기능들이다)

// 1. 대입 연산자
let var1 = 1;

// 2. 산술 연산자
let num1 = 3 + 2;
let num2 = 3 - 2;
let num3 = 3 * 2;
let num4 = 3 / 2;
let num5 = 3 % 2;

let num6 = 1 + 2 * 10;
console.log(num6); // 21
console.log((1 + 2) * 10); // 30

// 3. 복합 대입 연산자 : 산술 + 대입
let num7 = 10;
num7 += 20; // num7 = num7 + 20;

// 4. 증감 연산자
let num8 = 10;
console.log(++num8); // 11
console.log(--num8); // 10
console.log(num8++); // 10 출력, num8 = 11

// 5. 논리 연산자
let or = true || false; // or 연산자
let and = true && false; // and 연산자
let not = !true; // not 연산자
console.log(or, and, not); // true false false

//6 6. 비교 연산자 (java와 조금 다르다)
let comp1 = 1 == '1'; // ==로 비교 시 자료형은 비교하지 않음, true 출력
let comp2 = 1 === '1';  // ===로 비교 시 자료형까지 비교, false 출력
let comp3 = 1 !== '1';  // ===로 비교 시 자료형까지 비교, true 출력
console.log(comp1, comp2, comp3); // true false true

let comp4 = 2 > 1;
letcomp4_1 = 2 >= 1;

let comp5 = 2 < 1;
let comp5_1 = 2 <= 1;
console.log(comp4, comp5);