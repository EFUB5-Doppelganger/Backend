#!/usr/bin/env bash

PROJECT_ROOT="/home/ubuntu/Backend"
JAR_FILE="$PROJECT_ROOT/build/libs/doppelganger-0.0.1-SNAPSHOT.jar"

APP_LOG="$PROJECT_ROOT/application.log"
ERROR_LOG="$PROJECT_ROOT/error.log"
DEPLOY_LOG="$PROJECT_ROOT/deploy.log"

TIME_NOW=$(date +%c)

CURRENT_PID=$(pgrep -f $JAR_PATH)

if [ -n "$CURRENT_PID" ]; then
    echo "$TIME_NOW > 실행 중인 애플리케이션을 종료합니다. (PID: $CURRENT_PID)" >> $DEPLOY_LOG
    kill -9 $CURRENT_PID
    sleep 2
fi

echo "$TIME_NOW > Starting application" >> $DEPLOY_LOG

nohup java -jar \
  -Dspring.config.location=$PROJECT_ROOT/src/main/resources/application.yml \
  $JAR_FILE > $APP_LOG 2> $ERROR_LOG &

NEW_PID=$(pgrep -f $JAR_FILE)
echo "$TIME_NOW > Application started with PID: $NEW_PID" >> $DEPLOY_LOG