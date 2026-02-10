// 1. Spread 연산자 (흩뿌리다, 펼치다)
// 객체, 배열에 저장된 여러 값을 개별로 흩뿌려주는 역할

let arr1 = [1, 2, 3];
let arr2 = [4, 5, 6];

// 4, 5, 6 사이에 1, 2, 3 데이터를 넣고 싶을 때
// 직접 인덱스로 추가하는 방법은 좋지 못한 방법이다.
let arr3 = [4, arr1[0], arr1[1], arr1[2], 5, 6];
console.log(arr3);

let arr4 = [4, ...arr1, 5, 6]; // ...배열 이름 : spread 연산
console.log(arr4); 

// 객체
let obj1 = {
    a:1,
    b:2
};
console.log(obj1);

// spread 연산 사용 x
let obj2 = {
    a: obj1.a,
    b: obj1.b
};
console.log(obj2);

// spread 연산 사용 o
let obj3 = {
    ...obj1 // spread 연산 사용 o
}
console.log(obj3);

// 함수 
function funcA(p1, p2, p3) {
    console.log(p1, p2, p3);
}

funcA(...arr1); // spread 연산 적용





// 2. Rest 매개변수 (나머지)
// rest가 아닌 다른 변수명도 가능하지만 일반적으로 rest 사용
// 데이터가 많이 들어와도 배열에 한 번에 저장 가능
function funcB(...rest) { // 여러 데이터가 들어올 때 배열로 담는다. 
    console.log(rest); 
}

funcB("kim", 4, true); 
funcB(...arr1);


// 첫 번째 파라미터를 구분하고 싶을 때는 rest 앞에 선언 가능
// rest 뒤에 다른 파라미터를 두면 오류가 발생한다.
function funcC(a, ...rest) {
    console.log("첫 번째 변수 : " + a);
    console.log(rest);
}
funcC("kim", 4, true); 
funcC(...arr1);
