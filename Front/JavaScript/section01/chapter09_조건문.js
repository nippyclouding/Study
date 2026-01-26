// 1. if 조건문

let num = 10;

if (num >= 10) {
    console.log("10 이상입니다.");
} else if (num >= 5) {
    console.log("5 이상, 10 미만 입니다.");
} else {
    console.log("그 외의 값입니다.");
}


// 2. switch 조건문

let animal = "cat";
switch (animal) {
    case "dog" :
        console.log("this is dog");
        break;
    case "bird" :
        console.log("this is bird");
        break;
    case "bear" :
        console.log("this is bear");
        break;
    case "cat" :
        console.log("this is cat");
        break;
}