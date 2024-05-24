#!/usr/bin/env bash

REPOSITORY=/home/ubuntu/plog
cd $REPOSITORY

APP_NAME=demo
JAR_NAME=$(ls $REPOSITORY/build/libs/ | grep 'SNAPSHOT.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/build/libs/$JAR_NAME

CURRENT_PID=$(pgrep -f $APP_NAME)

if [ -z $CURRENT_PID ]; then
  echo "> No application to terminate."
else
  echo "> Terminating process $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5

  # Ensure the process is terminated
  if ps -p $CURRENT_PID > /dev/null; then
    echo "> Process did not terminate, killing with -9"
    kill -9 $CURRENT_PID
    sleep 5
  fi
fi

echo "> Deploying $JAR_PATH"
nohup java -jar $JAR_PATH > $REPOSITORY/nohup.out 2>&1 &
