<<<<<<< HEAD
# Используем официальный образ Node.js в качестве базового
FROM node:20 AS build

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем package.json и package-lock.json
COPY package*.json ./

# Устанавливаем зависимости
RUN npm install

# Копируем остальные файлы проекта в рабочую директорию
COPY . .

# Собираем проект (опционально)
# RUN npm run build

# Открываем порт 3000 для доступа к приложению
EXPOSE 3000

# Запускаем React-приложение
CMD ["npm", "start"]
=======
#FROM eclipse-temurin:21-alpine as build
# Нам требуется образ, содержащий maven, при помощи
# ключевого слова as мы указываем псевдоним для контейнера сборки,
# чтобы при его помощи в дальнейшем обращаться к контейнеру
FROM openjdk:21-jdk

ARG JAR_FILE=target/SpringSecurity_1805-0.0.1-SNAPSHOT.jar

# Собирать проект будем в /build


# Теперь необходимо скопировать необходимые для сборки проекта файлы в конейнер
COPY ./target/SpringSecurity_1805-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
>>>>>>> 823b6f2590326050b632e84f50e474cb90fda94f
