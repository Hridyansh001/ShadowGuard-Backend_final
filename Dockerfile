FROM maven:3.9-eclipse-temurin-21-alpine AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY . .

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN apk add --no-cache \
    tesseract-ocr \
    tesseract-ocr-data-eng

ENV TESSDATA_PREFIX=/usr/share/tessdata

COPY --from=build /app/target/ShadowGuard-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","--enable-native-access=ALL-UNNAMED","-jar","app.jar"]
#FROM maven:3.9-eclipse-temurin-21-alpine AS build
#
#WORKDIR /app
#
#COPY pom.xml .
#RUN mvn dependency:go-offline
#
#COPY src ./src
#
#RUN mvn clean package -DskipTests
#
#
#FROM eclipse-temurin:21-jre-alpine
#
#WORKDIR /app
#
#RUN apk add --no-cache \
#    tesseract-ocr \
#    tesseract-ocr-data-eng
#
#ENV TESSDATA_PREFIX=/usr/share/tessdata
#
#COPY --from=build /app/target/ShadowGuard-0.0.1-SNAPSHOT.jar app.jar
#
#EXPOSE 8080
#
#ENTRYPOINT ["java","--enable-native-access=ALL-UNNAMED","-jar","app.jar"]