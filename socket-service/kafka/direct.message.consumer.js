const e = require('express');
const { Kafka } = require('kafkajs')
const io = global.io
const kafkaUrl = process.env.NODE_ENV == 'production' ? '34.162.185.163:9093': 'localhost:9093';
const kafka = new Kafka({
  clientId: 'my-app',
  brokers: [kafkaUrl, kafkaUrl]
})
global.kafka = kafka;
const consumer = kafka.consumer({ groupId: 'DIRECT_MESSAGE_GROUP_ID' })

exports.run = async () => {
  await consumer.stop()
  await consumer.connect()
  await consumer.subscribe({ topic: 'DIRECT_MESSAGE', fromBeginning: true })
  await consumer.run({
    eachMessage: async ({ message }) => {
      console.log("****************** Arrived in Consumer ******************")
      const obj = JSON.parse(message.value);
      if (obj.isNewRoom) {
        obj.memberEmails.filter(memberEmail => memberEmail != obj.sender.email).forEach(result => io.to(result).emit("send message", obj));

      } else {
        io.to("room " + obj.roomChatId).emit("send message", obj);
      }
    },
  })
}

// exports.start = async () => {
//   await consumer.connect()
//   await consumer.subscribe({ topic: 'DIRECT_MESSAGE', fromBeginning: true })
// }

// io.on("connection", function (socket) {



// socket.on("disconnect", function () {
//     console.log("A user with ID: " + socket.id + " disconnected");
// });

// // More Socket listening here.
// if (io.sockets.connected)
//     socket.emit("connections", Object.keys(io.sockets.connected).length);
// else socket.emit("connections", 0);
// });

// io.on("connection", (socket) => {
//   // notify existing users
//   console.log("A user with ID: " + socket.id + " connected");
//   socket.emit("user connected", {
//     userID: socket.id,
//     username: socket.username,
//   });
// });

//   await consumer.run({
//     eachMessage: async ({partition, message }) => {
//       console.log({
//         partition,
//         offset: message.offset,
//         value: message.value.toString(),
//       });
//     }
// })