FROM gradle:7.2.0-jdk11
WORKDIR /app

COPY ./build.gradle ./settings.gradle /app/

RUN gradle clean build --no-daemon > /dev/null 2>&1 || true
COPY . /app/

RUN gradle clean build --no-daemon
CMD gradle :serviceC:bootRun
