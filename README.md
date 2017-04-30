=========

## E9Pay WebService

### Build the Project
```
mvn clean install
```

### Package the Project
```
mvn package
```

### Run Project
```
mvn tomcat7:run
```

### Set up Oracle
```
oracle -u root -p
> CREATE USER 'tutorialuser'@'localhost' IDENTIFIED BY 'tutorialmy5ql';
> GRANT ALL PRIVILEGES ON *.* TO 'tutorialuser'@'localhost';
> FLUSH PRIVILEGES;
```


### Use the REST Service

```
curl http://localhost:8080/e9pay/users
```