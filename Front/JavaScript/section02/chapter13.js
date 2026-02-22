// 문제 : 콜백 함수에 의해 코드가 > 형태가 된다.
// 해결 : Promise

// Promise : 비동기 작업을 효율적으로 처리할 수 있도록 비동기 작업을 감싸는 JS 내장 객체
// setTimeout 함수를 감싸는 객체
// 비동기 작업 실행, 관리, 결과 저장, 병렬 실행, 재실행 등 기능을 수행할 수 있는 객체

// Promise 객체는 비동기 작업을 3단계로 구분하여 관리한다.

// 1. 대기 Pending : 아직 작업이 완료되지 않은 상태

// 2. 성공 Fulfilled 

// 3. 실패 Rejected


// 대기 -> 성공 : resolve
// 대기 -> 실패 : reject 

// Yutube 예시 : 영상을 보기 위해 로딩을 기다리는 상태 : Pending
// 로딩 완료 후 시청 : 성공
// 로딩 실패 : 실패

const promise1 = new Promise(() => {}); // 생성자 파라미터에 비동기 작업을 담은 콜백 함수를 전달한다.
// 콜백 함수 : executor, 비동기 작업을 실행하는 함수

const promise2 = new Promise(() => {
    setTimeout(() => {
        console.log("hello");
    }, 2000); // 2초 뒤에 실행
})

console.log(promise2);
// promise state : pending
// promise result : undefined


const promise3 = new Promise((resolve, reject) => {
    // 콜백 함수 파라미터에 resolve, reject 추가
    setTimeout(() => {
        console.log("hello");
        resolve();
    }, 2000); // 2초 뒤에 실행
})

setTimeout(() => {
    console.log(promise3);
}, 3000); // 3초 뒤에 promise3 실행 => 2초 뒤에 비동기 함수 실행
// promise state : resolve



const promise4 = new Promise((resolve, reject) => {
    // 콜백 함수 파라미터에 resolve, reject 추가
    setTimeout(() => {
        console.log("hello");
        resolve("success"); // resolve 함수 안에 값을 넣으면 promise result에 출력
    }, 2000); // 2초 뒤에 실행
})

setTimeout(() => {
    console.log(promise4);
}, 3000); // 3초 뒤에 promise3 실행 => 2초 뒤에 비동기 함수 실행
// promise state : resolve
// promise result : success


const promise5 = new Promise((resolve, reject) => {
    // 콜백 함수 파라미터에 resolve, reject 추가
    setTimeout(() => {
        console.log("hello");
        reject("fail"); 
        // reject 함수 안에 값을 넣으면 reject 함수 실행 시 '실패'를 띄우며 promise result에 출력
    }, 2000); // 2초 뒤에 실행
})

setTimeout(() => {
    console.log(promise5);
}, 3000); // 3초 뒤에 promise3 실행 => 2초 뒤에 비동기 함수 실행
// promise state : reject
// promise result : fail




const promiseEx1 = new Promise((resolve, reject) => {
    setTimeout(() => {
        const num = 10;

        if (typeof num === "number") {
            resolve(num + 10);
        } else {
            reject("num은 숫자가 아닙니다.");
        }

    }, 2000); 
})

// then
promiseEx1.then((value) => {
    console.log(value);
})
// promise가 성공 시 '그 후'에 파라미터로 넣은 콜백 함수 실행, 성공 시에만 동작
// promise의 결과를 콜백 함수에서 파라미터로 받을 수 있다. (value)


// catch
promiseEx2.catch((error) => {
    console.log(error);
}) 
// promise가 실패 시 파라미터로 넣은 콜백 함수 실행, 실패 시에만 동작
// promise의 결과를 콜백 함수에서 파라미터로 받을 수 있다. (error)
// reject 내부 값을 출력한다. "num은 숫자가 아닙니다"


// 체이닝 : then, catch
promiseEx1.then((value) => {
    console.log(value);
}).catch((error) => {
    console.log(error);
}) 


function add10(num) {
    const promiseEx2 = new Promise((resolve, reject) => {
        setTimeout(() => {
            const num = 10;

            if (typeof num === "number") {
                resolve(num + 10);
            } else {
                reject("num은 숫자가 아닙니다.");
            }

        }, 2000); 
    })
    return promiseEx2;
}

const p = add10(5);
p.then((result) => {
    console.log(result);

    const newP = add10(result);
    newP.then((result) => {
        console.log(result);
    })

    // 콜백 함수를 연달아 사용하면 > 형태로 코드가 형성된다 => Promise 객체의 해결 : return newP
})

const p2 = add10(5);
p2.then((result) => {
    console.log(result);

    const newP = add10(result);

    return newP; // > 코드 형태 해결 - 새로운 P 반환 : then의 결과값이 newP가 된다.
}).then((result) => { // 리턴으로 newP가 되어 이어서 .then으로 작성 가능
    console.log(result);
})

// 간결화, 체이닝
add10(5).then((result) => {
    console.log(result);
    return add10(result);
}).then((result) => { 
    console.log(result);
}).then((result) => {
    return add10(reuslt);
}).catch((error) => {
    console.log(error);
})