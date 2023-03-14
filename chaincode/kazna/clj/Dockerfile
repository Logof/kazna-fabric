# the first stage 
FROM docker.io/library/clojure:temurin-17-alpine AS builder

# copy the code to the container
RUN mkdir -p /build
COPY . /build/

# Build and package code
WORKDIR /build
RUN clojure -T:build uber


# the second stage of our build just needs the compiled files
FROM docker.io/library/eclipse-temurin:17-jre
ARG CC_SERVER_PORT=9999

# Setup tini to work better handle signals
ENV TINI_VERSION v0.19.0
ADD https://github.com/krallin/tini/releases/download/${TINI_VERSION}/tini /tini
RUN chmod +x /tini

RUN addgroup --system javauser && useradd -g javauser javauser

# copy only the artifacts we need from the first stage and discard the rest
COPY --chown=javauser:javauser --from=builder /build/target/chaincode.jar /chaincode.jar
COPY --chown=javauser:javauser docker/docker-entrypoint.sh /docker-entrypoint.sh 

ENV PORT $CC_SERVER_PORT
EXPOSE $CC_SERVER_PORT

USER javauser
ENTRYPOINT [ "/tini", "--", "/docker-entrypoint.sh" ]
