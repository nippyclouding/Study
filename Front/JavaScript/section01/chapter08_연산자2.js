// 1. null 병합 연산자 : 존재하는 값을 추려내는 기능

// null, undefined가 아닌 값을 찾아내는 연산자

let var1;
let var2 = 10;
let var3 = 20;

let var4 = var1 ?? var2;
console.log(var4);
// ?? : null이나 undefined가 아닌 값을 찾아낸다 
// => var1은 undefined이기에 var4에 var2인 10이 들어간다.

let var5 = var2 ?? var3;
console.log(var5); // 둘 다 null이나 undefined가 아닐 경우 첫 번째 수 var2가 출력

let userName = "kim"; // 백엔드에서 보통 데이터를 받아온다.
let userNickName = "helloKim";

let displayName = userName ?? userNickName;
// userName 값이 없다면 userNickName을 사용
// userName 값이 있다면 그대로 사용
 


// 2. typeof 연산자 : 값의 타입을 문자열로 반환

let var6 = 1;
var6 = "hello"; // 동적으로 타입이 변한다.

let t1 = typeof var6;
console.log(t1); // String 출력

// 3. 삼항 연산자 : 항을 3개 사용하는 연산자
let var7 = 10;
let res = var7 % 2 === 0 ? "짝수" : "홀수"; // var7 % 2가 0일 경우 짝수, 0이 아닐 경우 홀수