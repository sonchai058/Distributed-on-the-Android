Server Nodejs(127.0.0.1) 's connected
Worker 1(127.0.0.1) 's connected
Worker 2(127.0.0.1) 's connected
Client 1(127.0.0.1) 's connected
Client 2(127.0.0.1) 's connected

Client1 Send request to server
Client1: SND_REQ:{img_name:"img1", process: {"GS", "BF", "GB"}}

Server Nodejs receive request from client
REC_REQ:{img_name:"img1", process: {"GS", "BF", "GB"}}

Server Nodejs Send work to worker
SND_WORK:{worker: "worker 1", img_name:"img1", process: {"GS"}}
SND_WORK:{worker: "worker 2", img_name:"img1", process: {"BF"}}

Worker receive work from server
REC_WORK:{worker: "worker 1", img_name:"img1", process: {"GS"}}
REC_WORK:{worker: "worker 2", img_name:"img1", process: {"BF"}}

Worker send work to server
SND_WORK:{worker: "worker 1", img_name:"img1", process: {"GS"}}
SND_WORK:{worker: "worker 2", img_name:"img1", process: {"BF"}}

Server Nodejs receive/sent work via worker
REC_WORK:{worker: "worker 1", img_name:"img1", process: {"GS"}}
SND_WORK:{worker: "worker 1", img_name:"img1", process: {"GB"}}
REC_WORK:{worker: "worker 2", img_name:"img1", process: {"BF"}}

Worker receive work from server
REC_WORK:{worker: "worker 1", img_name:"img1", process: {"GB"}}

Worker send work from server
SND_WORK:{worker: "worker 1", img_name:"img1", process: {"GB"}}

Server Nodejs receive work from worker
REC_WORK:{worker: "worker 1", img_name:"img1", process: {"GB"}}

Server Nodejs sent worked to client
SND_WORK:{client: "client 1", img_name:"img1", process: {"GS"}}
SND_WORK:{client: "client 1", img_name:"img1", process: {"BF"}}
SND_WORK:{client: "client 1", img_name:"img1", process: {"GB"}}

Client1 receive work from server
REC_WORK:{client: "client 1", img_name:"img1", process: {"GS"}}
REC_WORK:{client: "client 1", img_name:"img1", process: {"BF"}}
REC_WORK:{client: "client 1", img_name:"img1", process: {"GB"}}


Server Nodejs(127.0.0.1) 's connected
Worker 1(127.0.0.1) 's connected
Worker 2(127.0.0.1) 's connected
Client 1(127.0.0.1) 's connected

Client1
Client1: SND:{img_name:"img1", process: {"GS", "BF", "GB"}}

Server Nodejs:
REC:{img_name:"img1", process: {"GS", "BF", "GB"}}

Worker
SND_WORK:{worker: "worker 1", img_name:"img1", process: {"GS"}}
SND_WORK:{worker: "worker 2", img_name:"img1", process: {"BF"}}

Worker
REC_WORK:{worker: "worker 1", img_name:"img1", process: {"GS"}}
REC_WORK:{worker: "worker 2", img_name:"img1", process: {"BF"}}

Worker
REC_WORK:{worker: "worker 1", img_name:"img1", process: {"GS"}}
REC_WORK:{worker: "worker 2", img_name:"img1", process: {"BF"}}


RQ
สิ่งที่ระบบต้องมี
1.	โทรศัพท์ระบบปฏิบัติการแอนดรอยด์ Version 4.03 ขึ้นไปแต่ไม่เกิน 7.0
2.	แอพพลิเคชั่น Distributed
3.	Internet , 3G Wifi

การทำงานของระบบ
ขั้นตอนการเช็คสถานะ
1.	ทุกเครื่องทำการเปิดแอพพลิเคชั่น
2.	เครื่อง master จะทำการเช็คสถานการณ์เชื่อมต่อโดยการส่ง Message ไปไว้ที่ Message server 
3.	เมื่อ Message server ได้รับ message จากเครื่อง master แล้วจะทำการ Forward message ไปยังทุก ๆเครื่องที่ทำการเชื่อมต่ออยู่
4.	เมื่อเครื่องที่ทำการเชื่อมต่ออยู่ได้รับข้อความก็จะทำการตอบกลับไปยังเครื่อง master ผ่านทาง message server โดยข้อความที่ตอบกลับจะเป็น ชื่อของเครื่อง worker กับ สถานะความพร้อม
5.	เมื่อเครื่อง master ได้รับ message ตอบกลับมาจะจัดเก็บชื่อเครื่องกับสานะของเครื่อง worker ไว้
นำ message ที่ได้รับมาเช็ค ว่าได้รับมาจากชื่อเครื่องใดบ้าง แล้วทำการนับจำนวนเครื่องตามจำนวน ชื่อเครื่อง
6.	เครื่อง master จะทำการกำหนดลำดับเครื่อง worker ( เริ่มที่ 1 ) โดยส่ง ชื่อเครื่อง พร้อม กับ ลำดับ กลับไปให้กับ worker
7.	เครื่อง worker จะทำการจัดเก็บลำดับตัวเอง
8.	แสดงชื่อเครื่อง , ลำดับ , สถานะความพร้อม
ขั้นตอนการประมวลผล
1.	เลือกรูปภาพที่ต้องการประมวลผล ( เครื่อง master )
2.	เลือกฟังก์ชันในการประมวลผลรูปภาพ ( 1.Gray scale , 2.Blur image , 3.Black filter )
3.	อัพโหลดภาพขึ้นไปเก็บไว้ที่ image store 
4.	ส่ง url ของรูปภาพ พร้อมฟังก์ชันที่ต้องการประมวลผลไปที่ Message server พร้อมกำหนด สถานะของงาน (ว่าง) 
ถ้าต้องการเลือกรูปภาพอื่นกลับไปทำที่ข้อ 1 
ถ้าไม่มี ทำต่อไป
5.	Message server ทำการ Forward message ที่ได้รับมาจากเครื่อง master ไปยังเครื่อง worker ที่เชื่อมต่ออยู่
6.	เมื่อ worker ได้รับ message พร้อมสถานะของงาน 
ถ้างานว่าง ลำดับที่ 1 จะโหลดไฟล์มาประมวลผลพร้อมส่ง สถานะงาน (กำลังประมวล)ไปยัง ทุกเครื่องที่เชื่อมต่อ
พร้อมที่เครื่อง master แสดงสถานะการทำงานของระบบ
จากนั้นลำดับต่อมา จะเช็คว่ามีงานหรือไม่ ถ้ามี เช็คว่าเครื่องลำดับก่อนหน้า ได้รับงานแล้วหรือไม่ ถ้ายัง ก็จะไม่นำงานมาประมวลผล ถ้าลำดับก่อนหน้านั้นมีงานประมวลผลแล้ว ก็จะนำงานที่ว่างมาประมวลผลต่อพร้อมส่งสถานะออกไปยังทุกเครื่อง ทำซ้ำขั้นตอนที่ 6 จนกว่าจะไม่มีงาน
7.	เมื่อ worker ประมวลผลเสร็จจะทำการอัพโหลดภาพไปเก็บที่ image store แล้วทำการส่ง url , สถานะงาน (ประมวลผลเสร็จสิ้น) สถานะเครื่องตนเอง  กลับไปยังเครื่อง master 
ขั้นตอนการแสดงผล
1.	เมื่อเครื่อง master ได้รับ url , สถานะงาน , สนาถะเครื่อง worker ที่ประมวลผลเสร็จแล้ว จะทำการ Download ภาพตามที่อยู่ url มาจัดเก็บไว้ที่ตัว master 
2.	ทำการโชว์ ภาพ ก่อนการประมวลผล หลังการประมวลผล ประมวลผลจากเครื่องลำดับที่เท่าไหร่ 
3.	นับจำนวนงานที่แต่ละเครื่อง worker ได้ทำแล้วนำมาทำเป็น แผนภูมิ เพื่อแสดง จำนวนงานที่แต่ละเครื่องประมวลผลไป และคิดเป็นเปอร์เซ็นต์ที่แต่ละเครื่องประมวลผล 


