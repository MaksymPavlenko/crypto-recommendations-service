FROM azul/zulu-openjdk-alpine:17-latest

ADD build/libs/crypto-recommendations-service-*.jar app.jar

CMD ["java", "-jar", "app.jar"]

EXPOSE 8080
