// async, await : 비동기 작업 처리 

// 1. async : 함수 앞에 붙이는 키워드
// 함수를 비동기 함수로 만든다.
// 함수가 Promise 객체를 반환하도록 한다.

async function getData1() {
    return {
        name : "kim",
        id : "hello"
    };
}

console.log(getData1()); // Promise 객체 출력, Promise 결과값으로 내부 객체 출력


 // return new Promise(...) : async가 붙은 함수가 원래 비동기였다면 그대로 실행 
async function getData2() {
    return new Promise((resole, reject) => {
        setTimeout(() => {
            resolve({
                name : "kim",
                id : "hello"
            });
        }, 1500);
    })
}

console.log(getData2());

// await : async 함수 내부에서만 사용이 가능한 키워드
// 비동기 함수가 모두 처리되기를 기다리는 역할 

async function printData1() {
    await getData2().then((result) => {
        // getData2() 함수가 반환하는 Promise 객체가 '종료'되길 기다린다.
        // Promise 비동기 작업이 완료되면 다음을 진행한다.
        console.log(result);
    })
}

printData1();

async function printData2() {
    const data = await getData2();
    console.log(data);
}

printData2();

// async, await 이용 시 비동기 작업을 동기 작업을 처리하듯이 간결하게 구현 가능
// 실제로는 비동기로 동작하지만 코드를 읽는 방식이 동기처럼 보여진다. 
