#!/bin/sh
export API_TODO="http://localhost:9100/api/todos/v2.0"

mvn clean spring-boot:run -P h2 -Drun.profiles=h2