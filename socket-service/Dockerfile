FROM node:12-alpine3.14

WORKDIR /usr/src/app

ENV NODE_ENV production

COPY package*.json ./

RUN npm install

COPY . .

CMD [ "node", "index.js" ]