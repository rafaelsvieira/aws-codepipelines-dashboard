### Build stage
FROM maven:3.5.2-jdk-8 as builder
WORKDIR /tmp/build-dir
COPY . .
RUN cd /tmp/build-dir && mvn package
### Production stage
FROM java:8-jre
RUN groupadd -r app && useradd --no-log-init -r -g app app
WORKDIR /home/app
USER app
COPY --from=builder /tmp/build-dir/target/app.jar /app/app.jar
CMD java -jar /app/app.jar
EXPOSE 8080
