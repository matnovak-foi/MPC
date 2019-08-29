rm /var/lib/tomcat8/webapps/WebGUI_war.war
sleep 10
echo "copy war"
cp /home/matnovak/phd/WebGUI_war.war /var/lib/tomcat8/webapps/
sleep 10
echo "copy config"
cp /home/matnovak/phd/templateExclusionConfiguration.properties /var/lib/tomcat8/webapps/WebGUI_war/WEB-INF
cp /home/matnovak/phd/web.xml /var/lib/tomcat8/webapps/WebGUI_war/WEB-INF
chmod 755 /var/lib/tomcat8/webapps/WebGUI_war/WEB-INF/lib/tools/*
less /var/lib/tomcat8/logs/catalina.out 
