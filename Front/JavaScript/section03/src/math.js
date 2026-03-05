function add(a, b) {
    return a + b;
}

function sub(a, b) {
    return a - b;
}

//CJS
// module이라는 내장 객체 속에 exports로 값을 담는다.
module.exports = {
    add : add,
    sub : sub

}
