# Imagen base de Java 21 (temurin es de Eclipse)
FROM eclipse-temurin:21-jre

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el JAR al contenedor
COPY target/studytrack-1.0.0.jar app.jar

# Expone el puerto por donde Spring Boot atenderá
EXPOSE 8080

# Comando que inicia la aplicación
ENTRYPOINT ["java","-jar","/app/app.jar"]
