/*
var http = require("http");
var server = http.createServer(function(request, response) {
  response.writeHead(200, {"Content-Type": "text/html"});
  response.write("<!DOCTYPE \"html\">");
  response.write("<html>");
  response.write("<head>");
  response.write("<title>Hello World Page</title>");
  response.write("</head>");
  response.write("<body>");
  response.write("Hello World!");
  response.write("</body>");
  response.write("</html>");
  response.end();
});

server.listen(8080);
console.log("Server is listening");
*/


var http = require('http'),
    fs = require('fs');

http.createServer(function (req, res) {
    if(req.url.indexOf('.html') != -1){ //req.url has the pathname, check if it conatins '.html'

      fs.readFile('./index.html', function (err, data) {
        if (err) console.log(err);
        res.writeHead(200, {'Content-Type': 'text/html'});
        res.write(data);
        res.end();
      });

    }

    if(req.url == "/socket.io.js"){ //req.url has the pathname, check if it conatins '.js'

      fs.readFile('./socket.io.js', function (err, data) {
        if (err) console.log(err);
        res.writeHead(200, {'Content-Type': 'text/javascript'});
        res.write(data);
        res.end();
      });

    }
	
    if(req.url == "/jquery-3.2.0.min.js"){ //req.url has the pathname, check if it conatins '.js'

      fs.readFile('./jquery-3.2.0.min.js', function (err, data) {
        if (err) console.log(err);
        res.writeHead(200, {'Content-Type': 'text/javascript'});
        res.write(data);
        res.end();
      });

    }
	
    if(req.url.indexOf('.css') != -1){ //req.url has the pathname, check if it conatins '.css'

      fs.readFile('./style.css', function (err, data) {
        if (err) console.log(err);
        res.writeHead(200, {'Content-Type': 'text/css'});
        res.write(data);
        res.end();
      });

    }
}).listen(8080, '192.168.0.102');
//}).listen(8080, 'localhost');
console.log('Server running at http://localhost:8080/');