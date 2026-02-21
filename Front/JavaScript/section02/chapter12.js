function task() {
    // 비동기 작업
    setTimeout(() => {
        console.log("hello");
    }, 3000);
}

task(); // 3초 뒤에 "hello 출력"


function add(a, b) {
    // 비동기 작업
    setTimeout(() => {
        const sum = a + b;
        console.log(sum);
    }, 3000);
}

add(1, 6);

add(1, 2, () => {}); 
// add 함수의 결과를 이용하고 싶을 경우 
// 비동기 처리의 결과값을 사용하고자 하는 콜백 함수를 파라미터로 넣는다.


function add(a, b, callback) {
    // 비동기 작업
    setTimeout(() => {
        const sum = a + b;
        callback(sum); // 콜백 함수를 통해 결과 리턴
    }, 3000); 
}

add(1, 2, (value) => {
    console.log(value);
}); 


// 음식 주문 함수
function orderFood(callback) {
    setTimeout(() => {
        const food = "떡볶이";
        callback(food);
    }, 3000);
}

// 음식을 식히는 함수
function coolDownFood(food, callback) {
    setTimeout(() => {
        const cooldownedfood = `식은 ${food}`;
        callback(cooldownedfood);
    }, 2000);
}

// 음식을 얼리는 함수
function freezeFood(food, callback) {
    setTimeout(() => {
        const freezedFood = `냉동된 ${food}`;
        callback(freezedFood);
    }, 1500);
}

// 비동기 작업의 결과를 또 다른 비동기 작업 함수로 전달
orderFood((food) => {
    console.log(food); // 3초 뒤에 실행

    coolDownFood(food, (cooldownedfood) => {
        console.log(cooldownedfood); // 이후 2초 뒤에 실행

        freezeFood(cooldownedfood, (freezedFood) => {
            console.log(freezedFood); // 이후 1.5초 뒤에 실행
        })
    }); 
});

// 문제 : 콜백 함수에 의해 코드가 > 형태가 된다.
// 해결 : Promise