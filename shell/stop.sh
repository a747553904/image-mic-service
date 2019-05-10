#stop image-system
APP_NAME=image-system.jar ;
ps -ef | grep $APP_NAME | awk '{print $2}' | xargs kill -9