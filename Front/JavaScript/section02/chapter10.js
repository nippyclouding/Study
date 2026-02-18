// 1. Date 객체 생성 방법

let date1 = new Date(); // new 키워드, 생성자 방식, 현재 날짜 시간 출력
console.log(date1); // Thu Feb 19 2026 08:41:18 GMT+0900 (한국 표준시)

let date2 = new Date("1999-05-21"); // Fri May 21 1999 09:00:00 GMT+0900 (한국 표준시)
console.log(date2);

let date3 = new Date("1999/05/21"); // '/'로 구분 가능
let date4 = new Date("1999.05.21"); // '.'으로 구분 가능
let date5 = new Date("1999, 1, 7, 23, 29, 59"); // ','로 구분 가능, 시 분 초 표기 가능

// 2. TimeStamp : 특정 시간이 1970.01.01 00:00:00초롭터 몇 ms가 지났는지를 의미하는 숫자값
// 1970.01.01 00:00:00 : UTC, 협정 세계시

let ts1 = date1.getTime(); // UTC와 date1의 차이
console.log(ts1); // 1771458321357 ms가 지남

let date6 = new Date(ts1); // date1의 time이 들어간다.
console.log(date6);

console.log(date1 === date6); // false : 값은 같지만 참조가 다르다 (저장된 메모리 위치가 다르기 때문)

// 3. 시간 요소를 추출하는 방법
let year = date1.getFullYear(); // 연도 
let month = date1.getMonth(); // 달
let date = date1.getDate(); // 일

let hour = date1.getHours(); // 시
let minute = date1.getMinutes(); // 분
let seconds = date1.getSeconds(); // 초 

console.log(year, month, date, hour, minute, seconds);
// 자바스크립트에서 month의 시작은 0부터이다 => 항상 month + 1 필요

month += 1;
console.log(year, month, date, hour, minute, seconds);


// 4. 시간 수정
date1.setFullYear(2023);
date1.setMonth(2); // 3월 설정 (0부터 시작)
date1.setDate(30);
date1.setHours(23);
date1.setMinutes(49);
date1.setSeconds(20);

console.log(date1); // Thu Mar 30 2023 23:49:20 GMT+0900 (한국 표준시)

// 5. 시간을 여러 포맷으로 출력
console.log(date1.toDateString());
console.log(date1.toLocaleString()); // 한국 시간 출력