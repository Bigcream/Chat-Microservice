# build stage
FROM node:lts-alpine as build-stage
WORKDIR /app
RUN npm install -g npm@6.14.18
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# production stage
# FROM nginx:stable-alpine as production-stage
FROM nginx as production-stage
RUN mkdir /app
COPY --from=build-stage /app/dist /app
COPY nginx.conf /etc/nginx/nginx.conf
# COPY nginx.conf /etc/nginx/nginx.conf
# COPY --from=build-stage /app/dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]