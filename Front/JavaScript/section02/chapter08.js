// 배열의 순회, 탐색 

//1. for each
// arr.forEach(function() {}); 콜백 함수
let arr1 = [1, 2, 3];

arr1.forEach(function(item, idx, arr) {
    console.log(idx, item * 2);
}); 

// 현재 요소 값, 현재 반복 횟수, 전체 배열
// item : 1, 2, 3
// idx :  0, 1, 2
// arr : arr1 

let doubledArr = [];

arr1.forEach((item) => {
    doubledArr.push(item * 2);
})

console.log(doubledArr);


//2. includes : 배열에 특정 요소가 있는지 확인하는 메서드
let arr2 = [1, 2, 3];
let isInclude = arr2.includes(3); 
console.log(isInclude); // true

//3. indexOf : 특정 요소의 인덱스 (위치)를 찾아 반환하는 메서드
let arr3 = [1, 2, 3];
let index1 = arr3.indexOf(2); // 인덱스 위치인 1 반환
let index2 = arr3.indexOf(20); // 없는 값에 대해 찾을 시 -1 반환
console.log(index1); // 1 출력
console.log(index2); // -1 출력

//4. findIndex : 모든 요소를 순회하며 콜백 함수를 만족하면 해당 위치(인덱스) 반환
// 
let arr4 = [1, 2, 3];
// 콜백 함수를 만족하는 요소를 배열에서 찾아 인덱스를 반환
// 콜백 함수가 가장 처음으로 'true'를 반환하는 곳의 인덱스를 반환
// 콜백 함수의 조건에 맞는 인덱스가 없을 경우 -1 리턴
arr4.findIndex(() => {}); 

const findedIndex1 = 
arr4.findIndex((item) => {
    if (item %2 !== 0) {
        return true;
    }
}); 

console.log(findedIndex1); // 0번째 인덱스 리턴


const findedIndex2 = 
arr4.findIndex((item) => item %2 !== 0); // 줄이기 가능
console.log(findedIndex2); 


// indexOf 메서드는 배열 속에 기본형(원시형)이 있을 때 사용
// indexOf는 얕은 비교만 수행하기 때문에 객체 값을 상세하게 비교 불가
// findIndex 메서드는 배열 속에 객체형이 있을 때 사용

let objectArr = [
    {name: "kim"},
    {name : "lee"}
]

console.log(
    objectArr.indexOf({name:"kim"})
);

console.log(
    objectArr.findIndex(
        (item) => item.name === "kim"
    )
);

//5. find : findIndex와 유사하다.
// 모든 요소를 순회하며 콜백 함수를 만족하는 모든 요소들을 반환
// findIndex는 처음 만족하는 인덱스만 출력
// find는 조건에 만족하는 모든 '인덱스'가 아닌 '요소' 자체를 반환

let arr5 = [
    {name : "kim"},
    {name : "kim"}
];

const finded = arr5.find(
    (item) => item.name === "kim"
);

console.log(finded);