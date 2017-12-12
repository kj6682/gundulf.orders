#!/bin/sh
export API_PRODUCTS="http://localhost:9000/api/products"

mvn clean spring-boot:run -P h2 -Drun.profiles=h2