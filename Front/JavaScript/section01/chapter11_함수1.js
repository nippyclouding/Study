
// 1. 기본적인 함수 
function hello () {
    console.log("hello");
}

hello();


// 2. 매개 변수를 사용한 함수
function getArea (width, height) {
    let area = width * height;
    // console.log(area);
    return area;
}

console.log(getArea(30, 20));  

// 3. JS에서는 함수 오버로딩이 없다.
// function getArea () {
//     let width = 20;
//     let height = 10;
//     let area = width * height;
//     console.log(area);
// }

// 4. 호이스팅 : '끌어올리다', 함수 선언 시 아래에 있는 함수를 끌어올려 실행
let result = anotherTest(4, 5);
console.log(result); 


// 5. 중첩 함수
function anotherTest (a, b) {
    console.log("anotherTest");
    // JS에서는 함수 속에 중첩으로 함수 생성 가능 
    
    function add() {
        console.log("another");
    }

    add(); // 함수 속에 선언된 중첩 함수 사용
    let area = a * b;
    return area;   
}







