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

# Estágio 2: Runtime (Apenas o JRE mínimo sobre Alpine)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Cria um usuário não-root por segurança (boa prática para produção)
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copia o JAR do estágio de build
COPY --from=build /app/build/libs/*SNAPSHOT.jar app.jar

# Expõe a porta
EXPOSE 8080

# Flags otimizadas para containers pequenos e correção de rede
# -XX:+UseSerialGC: Economiza CPU/RAM em instâncias com < 1GB
# -Xmx384m: Define o limite de memória abaixo do limite do Render para evitar crash
ENTRYPOINT ["java", \
            "-Djava.net.preferIPv4Stack=true", \
            "-XX:+UseSerialGC", \
            "-Xss256k", \
            "-Xmx384m", \
            "-jar", "app.jar"]