FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD target/Contract.jar contract.jar
ADD target/config/api_portal.xml config/api_portal.xml
ADD target/config/apiegp.jks config/apiegp.jks
ADD target/config/db_pool.properties config/db_pool.properties
ADD target/config/hazelcast.properties config/hazelcast.properties
ADD target/config/log4j.properties config/log4j.properties
ADD target/config/mongo.properties config/mongo.properties
ADD target/config/rmq.properties config/rmq.properties
ADD target/logs/rmq.properties logs/contract.log
ENTRYPOINT exec java -jar contract.jar