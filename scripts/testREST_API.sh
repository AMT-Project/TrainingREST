#!/bin/bash
./runREST_API.sh

cd ../docker/testCucumber
rm -rf gamification-specs
cp -R ../../gamification-specs gamification-specs
cd gamification-specs
# https://stackoverflow.com/a/7456028
mvn clean package -Dmaven.test.skip=true # generate jar to copy
cd ../../..
echo "========================"
echo "=== Testing REST API ==="
echo "========================"
docker-compose up --build --force-recreate testcucumber
