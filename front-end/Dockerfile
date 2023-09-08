# Utiliser une image Node pour construire l'application
FROM node:14 AS build
WORKDIR /usr/src/app
COPY package.json ./
RUN npm install
COPY . .
RUN npm run build

# Utiliser une image Nginx pour servir l'application
FROM nginx:alpine
COPY --from=build /usr/src/app/dist/front-end /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
