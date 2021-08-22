#!/usr/bin/env bash

docker rm spring-hr

docker run \
  --name spring-hr \
  --network spring-hr_default \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:oracle:thin:@//oracle-database-xe-18400-slim:1521/ORCLPDB1 \
  spring-hr
