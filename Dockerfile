FROM openjdk:11
EXPOSE 8080:8080
RUN mkdir /app
COPY ./build/libs/*-all.jar /app/Chiassebin-0.0.1.jar
ENTRYPOINT ["java","-jar","/app/Chiassebin-0.0.1.jar"]