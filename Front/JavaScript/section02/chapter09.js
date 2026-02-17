// 5가지 배열 변형 메서드

// 1. filter : 실무에서 자주 사용
// 기존 배열에서 조건을 만족하는 요소들만 필터링하여 새로운 배열로 변환

let arr1 = [
    {name : "kim", hobby : "music"},
    {name : "lee", hobby : "running"},
    {name : "choi", hobby : "running"}
];

const runners = arr1.filter(
    (item) => {
        if (item.hobby === "running") {
            console.log(item.name);
        }
    }
)

console.log(runners); // filter 후 새로운 배열 

// 2. map : 실무에서 자주 사용
// 배열의 모든 요소를 순회하며 각각의 콜백 함수 실행, 그 결과값을 모아 새로운 배열로 반환

let arr2 = [1, 2, 3];

const mapReuslt1 = arr2.map(
    (item, idx, arr) => {
        console.log(idx, item);
        return item * 2; // 리턴이 있을 경우 새로운 배열로 반환
    }
)

let names = arr1.map(
    (item) => item.name // item의 name 값만 반환
);

// 3. sort : 정렬 
let arr3 = ["b", "a", "c"];
let arr4 = [10, 3, 5];

arr3.sort(); // 오름차 정렬 : a b c 
arr4.sort(); // 숫자 배열 : 정렬 실패, 사전 순으로만 정렬한다.

console.log(arr3); 
console.log(arr4);

// 오름차 정렬
arr4.sort(
    (a, b) => {
        if (a > b) {
            return 1; // sort 함수에서 양수 리턴 : 오름차 정렬
        } else if (a < b) {
            return -1; // sort 함수에서 음수 리턴 : 내림차 정렬
        } else return 0; // 값이 같을 경우 자리 변경 x
    }
)
console.log(arr4);

// 내림차 정렬
arr4.sort(
    (a, b) => {
        if (a > b) {
            return -1; // sort 함수에서 음수 리턴 : 내림차 정렬
        } else if (a < b) {
            return 1; // sort 함수에서 양수 리턴 : 오름차 정렬
        } else return 0; // 값이 같을 경우 자리 변경 x
    }
)

console.log(arr4);

// 4. toSorted : sort와 비슷하지만 원본 배열을 정렬하지 않고 정렬된 새로운 배열을 리턴
let arr5 = ["c", "a", "b"];
const sorted = arr5.toSorted();

console.log(arr5);   // 정렬 안된 원본 배열
console.log(sorted); // 정렬

// 5. join : 배열 모든 요소를 하나의 '문자열' 로 합쳐서 반환하는 메서드

let arr6 = ["hi", "my name", "is", "kim"];
const joinArr = arr6.join(' '); // 
console.log(joinArr);
