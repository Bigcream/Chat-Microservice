// import Eureka from 'eureka-js-client';
var express = require('express')
var app = express()
const http = require('http').createServer(app);
const io = require('socket.io')(http, {
  // cors: {
  //   origin: "http://localhost:5173",
  //   methods: ["GET", "POST"],
  //   allowedHeaders: ["*"],
  //   credentials: false
  // },
  path: "/socket/ws"
});

global.io = io;

const axios = require('axios');
const redis = require("redis");
let redisClient;
const redisUrl = process.env.NODE_ENV == 'production' ? '34.162.185.163': 'localhost';
(async () => {
  redisClient = redis.createClient(redisUrl, 6379);

  redisClient.on("error", (error) => console.error(`Error : ${error}`));

  await redisClient.connect();
})();



const gatewayurl = process.env.NODE_ENV == 'production' ? 'https://gateway.toppica360.online/api/v1': 'http://localhost:8080/api/v1';

const eurekaHelper = require('./helper/eureka-helper');
const consumer = require('./kafka/direct.message.consumer');
const notiConsumer = require('./kafka/notification.consumer');
// consumer.start();
// example configuration
consumer.run().catch("errooor", console.error);
notiConsumer.run().catch("errooor", console.error);

eurekaHelper.registerWithEureka('socket-service', 8082);

io.use((socket, next) => {
  const username = socket.handshake.auth.username;
  const access_token = socket.handshake.auth.token;
  if (!username) {
    return next(new Error("invalid username"));
  }
  socket.username = username;
  socket.access_token = access_token
  next();
});

io.on("connection", (socket) => {
  socket.join(socket.username);
  socket.broadcast.emit("user connected", {
    username: socket.username,
  });
  redisClient.set('ONLINE_' + socket.username, JSON.stringify(socket.username));
  const token = "Bearer " + socket.access_token.access_token;
  axios.get(gatewayurl + '/chat/list-room-socket/' + socket.username, {
    headers: {
      'Authorization': token
    }
  })
    .then(function (response) {
      console.log(response.data);
      socket.join(response.data);
    })
    .catch(function (error) {
      console.log(error);
    });
  // // notify existing users
  // socket.emit("user connected", {
  //   userID: socket.id,
  //   username: socket.username,
  // });
  socket.on("disconnect", (reason) => {
    redisClient.del('ONLINE_' + socket.username);
    socket.broadcast.emit("user disconnected", {
      username: socket.username,
    });
  });
});

app.get('/info', function (req, res) {
  res.send('Hello World!')
})
app.get('/order', function (req, res) {
  res.send('Hello World order!')
})
http.listen(8082, function () {
  console.log('Listening on port 8082...', gatewayurl)
})

