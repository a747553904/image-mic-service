#start image-system
APP_NAME=image-system.jar ;
cd ../jar ;
nohup java -jar $APP_NAME > nohup.out & 
echo 'Start Success!'
