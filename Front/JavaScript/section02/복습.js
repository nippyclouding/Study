// 구조 분해 할당 복습

let arr = [10, "kim", 0.42];

let [num, name, str] = arr;
console.log(num, name, str, varNum = 10);


let person = {
    name : "lee",
    age : 28,
    mail : "hello@naver.com"
};
let varFunc = ({name, age, mail}) => {
    if (name === "lee") {
        console.log("lee income");
    }

    console.log(age, mail);
}

varFunc(person);