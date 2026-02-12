// 배열, 객체 순회

// for of : 배열의 for each
// for in : 객체의 for each



let arr = [1, 2, 3];

// 1-1. 배열 인덱스 순회
for (let i = 0; i < arr.length; i++) {
    console.log(arr[i]);
}


// 1-2. for of 반복문 : 배열을 순회하는 곳에만 사용되는 특수한 배열 반복문
// for each랑 비슷하다, Java의 for (.. : ..)에서 : 가 of로 바뀐다.
for (let item of arr) {
    console.log(item);
}


let person = {
    name: "kim",
    age : 27,
    hobby : "programming"
}

// 객체 순회
// 2-1. Object.keys(객체) => 리턴 타입 : 배열
let keys = Object.keys(person); // key값 배열
console.log(keys); // ['name', 'age', 'hobby'] 출력

for (let i = 0; i < keys.length; i++) {
    let key = keys[i];
    let value = person[key];

    console.log("key : " + key);
    console.log("value : " + person[key]);
}

for (let key of keys) {
    console.log("key : " + key);
    console.log("value : " + person[key]);
}

// 2-2. Object.values 
// 객체에서 value 값들만 새로운 배열로 반환
let values = Object.values(person); 
console.log(values); // ['kim', 27, 'programming'] 출력

for (let value of values) {
    console.log(value);
}

for (let i = 0 ; i < values.length; i++) {
    console.log(values[i]);
}

// 2-3. for in 반복문 : 객체를 순회하는 곳에만 사용되는 특수한 객체 반복문

for (let key in person) { // person 객체에서의 for each
    console.log(key);
    console.log(person[key]);
}


// 객체 순회 : Object.keys, Object.values