// 단락 평가

let varA = false;
let varB = true;

console.log(varA && varB); // varA를 확인 후 바로 false를 출력 => 단락 평가 : varB에 접근하지 않고 varA를 출력

console.log(varB || varA); // varB를 확인 후 바로 true를 출력 => 단락 평가 : varAd에 접근하지 않고 varB를 출력


function returnFalse() {
    console.log("False 함수");
    return false;
}

function returnTrue() {
    console.log("True 함수");
    return true;
}

console.log(returnFalse() && returnTrue()); // false만 출력, true console은 출력하지 않는다.

function returnFalsy() {
    console.log("returnFalsy 함수");
    return undefined;
}

function returnTruthy() {
    console.log("returnTruthy 함수");
    return 10;
}
// Truthy, Falsy 사용 시 true, false가 출력되는 것이 아니라 결과값이 출력된다.
console.log(returnTruthy() || returnFalsy()); // returnTruthy 함수, 10 출력




console.log(returnFalse && returnTrue); 
// 출력 : 
// ƒ returnTrue() {
//     console.log("True 함수");
//     return true;
// }
// returnTrue 함수 자체가 출력
// returnFalse : 함수 '객체' 자체가 평가된다, JS에서 객체가 논리 연산 수행 시 Truthy 반환
// true && true =>  && and 연산자에서 truthy라면 뒤의 객체를 반환 => returnTrue 함수 자체 반환


// Truethy || Truethy : 앞의 Truethy 값이 출력된다.
// Truethy && Truethy : 뒤의 Truethy 값이 출력된다.

// 단락 평가 활용
function printName1(person) {
    // 단락 평가를 활용하지 않았을 경우
    if (!person) {
        console.log("person에 값이 비어있음");
        return;
    }

    console.log(person.name);
}

printName1();
printName1({name : "kim"});


function printName2(person) {
    // 단락 평가를 활용할 경우
    const name = person && person.name; 
    // person이 truthy면 person.name이 들어간다
    // person이 falsy면 person이 들어간다
    console.log(person || person.name);
    //왼쪽이 truthy면 왼쪽 반환
    //왼쪽이 falsy면 오른쪽 반환
}