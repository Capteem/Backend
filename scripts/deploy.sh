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
# Use full path for nohup.out
LOG_FILE=$REPOSITORY/nohup.out
nohup java -jar $JAR_PATH > $LOG_FILE 2>&1 &

# Confirm the log file is created and provide feedback
if [ -f $LOG_FILE ]; then
  echo "> Logs are being written to $LOG_FILE"
else
  echo "> Failed to create log file at $LOG_FILE"
fi