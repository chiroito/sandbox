# weblogic-jee-arquillian sample
weblogic-jee-arquillian is sample to run arquillian on Oracle WebLogic Server.
This sample based on [https://github.com/chiroito/weblogic-jee-quickstart].

Before running arquillian, you need to modify arquillian.xml for your environment.
This sample has two arquillian.xml as following.
- ejb/src/test/resources/arquillian.xml
- web/src/test/resources/arquillian.xml

Please refer to the following URL for each property.   
https://github.com/arquillian/arquillian-container-wls/

Test run managed mode by default.
```
mvn test
```
If you have WebLogic Server still running, you can run test on remote WebLogic Server adding `-Parquillian-weblogic-remote` as paramerter. 
```
mvn test -Parquillian-weblogic-remote
```
