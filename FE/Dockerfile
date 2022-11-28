FROM node:16.18.0 as build-stage
WORKDIR /var/jenkins_home/workspace/Front/frontend
COPY package*.json ./
RUN npm install
COPY ./ ./
RUN npm run build
EXPOSE 3000
CMD ["npm","run","start"]

