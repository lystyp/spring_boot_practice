
var http = require("http");

http.createServer(function(request, response) {
  console.log("Server is connected.");
  response.writeHead(200, {"Content-Type": "text/plain"});
  response.write("Hello world");
  response.end();
}).listen(8080);
console.log("Server is created.");

