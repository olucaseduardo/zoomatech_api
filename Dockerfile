# Estágio 1: Build (JDK completo para compilação)
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copia apenas o necessário para baixar as dependências primeiro (cache de camada)
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

# Baixa as dependências sem compilar o código ainda
RUN ./gradlew dependencies --no-daemon

# Agora copia o código fonte e compila
COPY src src
RUN ./gradlew build -x test --no-daemon

# Estágio 2: Runtime (IBM Semeru OpenJ9 - 50% menos consumo de RAM)
FROM ibm-semeru-runtimes:open-21-jre-jammy
WORKDIR /app

# Cria um usuário não-root por segurança
RUN groupadd -r spring && useradd -r -g spring spring
USER spring:spring

# Copia o JAR do estágio de build
COPY --from=build /app/build/libs/*SNAPSHOT.jar app.jar

# Expõe a porta 8081
EXPOSE 8081

# Flags do OpenJ9 otimizadas para baixo consumo de memória em containers
ENTRYPOINT ["java", \
            "-Xtune:virtualized", \
            "-Xquickstart", \
            "-Xshareclasses:name=app_share,cacheDir=/tmp", \
            "-Xscmx60m", \
            "-Xms32m", \
            "-Xmx160m", \
            "-Djava.net.preferIPv4Stack=true", \
            "-jar", "app.jar"]