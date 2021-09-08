var serv = require("socket.io"); // เรียกใช้ socket.io
var http = require('http');

//var io = serv.listen(9999,"localhost");  // เปิดเซิฟเวอร์ที่ port 9999

var s1 = http.createServer();
//s1.listen(9999, 'localhost');
s1.listen(9999, '192.168.0.102');
var io = serv.listen(s1);

console.log("Server NodeJS Socket.io Started.."); // log ที่ command ว่า server start ปล. ให้ผลเหมือน System.out.println();


var name = {}; // เตรียมตัวแปร name สำหรับเก็บชื่อผู้เข้าห้องแชต

var nameWorker = {}; // เตรียมตัวแปร Worker สำหรับเก็บชื่อผู้เข้าห้องแชต
var numWorker = 0; 

var nameClient = {}; // เตรียมตัวแปร Client สำหรับเก็บชื่อผู้เข้าห้องแชต
var numClient = 0; 

var nameWork = {}; //เตรียมตัวแปรสำหรับเก็บงาน

io.sockets.on('connection', function(socket) {  // เมื่อมีการ connect เข้ามา

    var ipv4 = socket.request.socket.remoteAddress; // เก็บ ip address ของผู้เข้าห้องแชต
    
    socket.on('SND_CNT', function(data) {// Transfer Connection Status to Device
        if (data.name === "" || data.name === null) {//ตรวจสอบข้อมูลว่าง
            socket.disconnect(); // ถ้าว่างให้ disconnect
        } else {
			
			var tmpName = data.name.trim();
			
			if(tmpName.localeCompare("Worker")==0) {
				numWorker = Object.keys(nameWorker).length;
				tmpName = data.name.trim()+(numWorker+1);
				nameWorker[socket.id]= {name: tmpName, status: 0};
			}else if(tmpName.localeCompare("Client")==0) {
				numClient = Object.keys(nameClient).length;
				tmpName = data.name.trim()+(numClient+1);
				nameClient[socket.id]= tmpName;
			}
			
            //console.log("Socket id : " + socket.id + " : " + tmpName + "(" + ipv4 + ") 's connected");// ถ้าไม่ว่าง Log ชื่อกับ IP
			console.log(tmpName + "(" + ipv4 + ") 's connected");// ถ้าไม่ว่าง Log ชื่อกับ IP
			
            name[socket.id] = tmpName; // นำชื่อมาเก็บไว้ใน name โดยมี คีย์ประจำตัวเป็น socket id
			//console.log(name);
			
			//console.log({id: socket.id, name: tmpName, ip: ipv4, numClient: Object.keys(nameClient).length, numWorker: Object.keys(nameWorker).length, uniqueId: data.uniqueId});
            io.sockets.emit('REC_CNT', {id: socket.id, name: tmpName, ip: ipv4, numClient: Object.keys(nameClient).length, numWorker: Object.keys(nameWorker).length, uniqueId: data.uniqueId});
			// emit ข้อมูลผ่านท่อ 'sendName' โดยมีพารามีเตอร์เป็น array มีname เป็น key มีค่าเป็น ชื่อและ ip
		
			if(Object.keys(nameWork).length>0)doWork();	
        }
    });
	
	function doWork() {
						
		if(Object.keys(nameWorker).length>0){ //Check Worker Connect
		
			for (var sid in nameWork) {
				if (nameWork.hasOwnProperty(sid)) {
				//	
					var chk = 0;
					
					//Check Status and Send Task to Worker
					if(nameWork[sid].process_gs == "1") { 
						for (var prop in nameWorker) {
							if (nameWorker.hasOwnProperty(prop)) {
								//console.log(nameWorker[prop].status);
								if(nameWorker[prop].status==0) {
									nameWork[sid].process_gs = "2";
									chk=1;
									nameWorker[prop].status = 1;
									io.sockets.emit('REC_TASK', {sort_id: nameWork[sid].id, worker_id: prop, name: nameWork[sid].name, img_data: nameWork[sid].img_data,  img_name: nameWork[sid].img_name, task: "gs"});	
									console.log("Sent task to "+nameWorker[prop].name+", sort_id: "+nameWork[sid].id+", worker_id: "+prop+", img_name:"+nameWork[sid].img_name+", task: "+"gs");
									break;
								}
							}
						}	
					}

					if(nameWork[sid].process_gb == "1") {
						for (var prop in nameWorker) {
							if (nameWorker.hasOwnProperty(prop)) {
								//console.log(nameWorker[prop].status);
								if(nameWorker[prop].status==0) {
									nameWork[sid].process_gb = "2";
									chk=1;
									nameWorker[prop].status = 1;
									io.sockets.emit('REC_TASK', {sort_id: nameWork[sid].id, worker_id: prop, name: nameWork[sid].name, img_data: nameWork[sid].img_data,  img_name: nameWork[sid].img_name, task: "gb"});	
									console.log("Sent task to "+nameWorker[prop].name+", sort_id: "+nameWork[sid].id+", worker_id: "+prop+", img_name:"+nameWork[sid].img_name+", task: "+"gb");
									break;
								}
							}
						}	
					}
					
					if(nameWork[sid].process_bf == "1") {
						for (var prop in nameWorker) {
							if (nameWorker.hasOwnProperty(prop)) {
								//console.log(nameWorker[prop].status);
								if(nameWorker[prop].status==0) {
									nameWork[sid].process_bf = "2";
									chk=1;
									nameWorker[prop].status = 1;
									io.sockets.emit('REC_TASK', {sort_id: nameWork[sid].id, worker_id: prop, name: nameWork[sid].name, img_data: nameWork[sid].img_data,  img_name: nameWork[sid].img_name, task: "bf"});	
									console.log("Sent task to "+nameWorker[prop].name+", sort_id: "+nameWork[sid].id+", worker_id: "+prop+", img_name:"+nameWork[sid].img_name+", task: "+"bf");
									break;
								}
							}
						}	
					}
					
					if(chk==0) {
						delete nameWork[sid];
					}
			
				//	
				}
			}
			
		}else {
			console.log("No Worker Connection");
			io.sockets.emit('NO_WRK',{remark: "No Worker Connection"}); // Setn "No Worker Connectio Message
		}
		
	}
	
    socket.on('SND_REQ', function(data) { //Transfer Request work to Worker
	
	    console.log("Received work from "+data.name+", id: "+data.socket_id+", img_name: "+data.img_name+", process_gs: "+data.process_gs+", process_bf: "+data.process_bf+", process_gb: "+data.process_gb);
		
		nameWork[data.socket_id] = {"name": data.name, "id": data.socket_id, "img_name": data.img_name, "img_data": data.img_data, "process_gs": data.process_gs, "process_bf": data.process_bf, "process_gb": data.process_gb };
		
		doWork();
    });
	
	socket.on('SND_TASK_RS', function(data) { //Transfer Task to Client/Worker
	
		console.log("Received task  from "+data.worker_name+", sort_id:"+data.sort_id+", worker_id: "+data.worker_id+", img_name:"+data.img_name+", task: "+data.task);
		
		nameWorker[data.worker_id].status = 0;
		
		//console.log(nameWorker[data.worker_id]);
		
		io.sockets.emit('REC_WORK_RS', data);
		console.log("Sent result work to "+data.name+", id:"+data.sort_id+", worker_name:"+data.worker_name+", worker_id: "+data.worker_id+", img_name:"+data.img_name+", task: "+data.task);
		 
		if(data.task=='gs') {
			if(nameWork.hasOwnProperty(data.sort_id)){nameWork[data.sort_id].process_gs = data.img_data; }
		}else if(data.task=='bf') {
			if(nameWork.hasOwnProperty(data.sort_id)){nameWork[data.sort_id].process_bf = data.img_data; }
		}else if(data.task=='gb') {
			if(nameWork.hasOwnProperty(data.sort_id)){nameWork[data.sort_id].process_gb = data.img_data; }
		}
		 
		doWork();
		 
	});
	
    socket.on('disconnect', function() {// ถ้ามีการ Disconnect
        console.log(name[socket.id] + " was disconnected"); // Log ชื่อที่ทำการ disconnect
        io.sockets.emit('disconnected', {name: name[socket.id]}); // ส่งข้อมูลชื่อ disconnect ไปยัง client ทุกตัว
		delete name[socket.id];
		delete nameWorker[socket.id];
		delete nameClient[socket.id];
		io.sockets.emit('REC_CNT', {id: "", name: "", ip: "", numClient: Object.keys(nameClient).length, numWorker: Object.keys(nameWorker).length, uniqueId: ""});
		//console.log(name);
    });
    
	/* Set Display for test */
    socket.on('sendMsg', function(data) { // เมื่อมีการ emit มาที 'sendMsg' 
        if (data.message === '' || data.message === null) { // ถ้า message เป็นค่าว่าง หรือไม่มีค่า
            return; // หยุดฟังชั่น
        }else if(data.message.indexOf('<')>=0){ // ถ้ามีการส่ง < มาด้วย (กรณีกัน การใส่ code เข้ามาในห้องแชต)
            socket.disconnect(); // บังคับผู้ส่งให้ออกจากห้อง
            return; // หยุดฟังชั่น
        }
        if (name[socket.id] !== null) { // ถ้าชื่อผู้ใช้ของ socket คนนี้ไม่เป็นค่าว่าง
            var msg = name[socket.id] + "(" + ipv4 + ") : " + data.message.trim(); // ให้ส่งข้อมูลกลับไปในรูปแบบ
// ชื่อ (ไอพี) : ข้อความ
            console.log(msg); // log โชว์ข้อความ           
            io.sockets.emit('sendMsg', {message: msg}); // emit ไปหา client ทุกคนผ่านท่อ 'sendMsg' 
        }
    });
	/* End Set Display for test */
    
});