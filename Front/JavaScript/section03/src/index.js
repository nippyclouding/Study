console.log("hello node js");
// node ~.js 로 js 실행
// 현재 경로 아래를 기준으로 찾아서 실행한다.
// index.js가 src 폴더 밑에 있을 경우 node index.js => 오류, node src/index.js 로 입력해야 실행된다.



// package.json에서 단축 명령어 지정 가능
// scripts 내부에 작성 - "start" : "node src/index.js" 
// 터미널에서 npm run 명령어 입력 시 동작

const moduleData = require("./math");
console.log(moduleData);

console.log(moduleData.add(1, 2));
console.log(moduleData.sub(2, 1));