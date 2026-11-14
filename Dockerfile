FROM sapmachine:26-jre-ubuntu-noble

WORKDIR /app

COPY target/sitesoft-testcase-0.0.1-jar-with-dependencies.jar .

VOLUME /var/db

ENTRYPOINT ["java", "-Ddb.path=/var/db/", "-jar", "/app/sitesoft-testcase-0.0.1-jar-with-dependencies.jar"]