const { Kafka } = require('kafkajs')
const io = global.io
const kafka = global.kafka;
const consumer = kafka.consumer({ groupId: 'NOTIFICATION_GROUP_ID' })

exports.run = async () => {
  await consumer.stop()
  await consumer.connect()
  await consumer.subscribe({ topic: 'NOTIFICATION', fromBeginning: true })
  await consumer.run({
    eachMessage: async ({ message }) => {
      const obj = JSON.parse(message.value);
      if(obj.type === 'FRIEND_NOTIFICATION'){
        io.to(obj.receiver).emit("friend request", obj);
        console.log("FRIEND_NOTIFICATION", obj);
      }
    },
  })
}