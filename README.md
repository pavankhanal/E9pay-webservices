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
Connect Database as Sys or System (user having previlage to create user)

CREATE USER e9pay IDENTIFIED BY password;

GRANT CONNECT TO e9pay;

GRANT CREATE SESSION TO e9pay;

GRANT UNLIMITED TABLESPACE TO e9pay;

grant all privileges to e9pay identified by password;
```

### Confirm DB Connection Properties
```
Database server, url , username and password are configfured here.
db-connection.properties
```


### Use the REST Service

```
curl http://localhost:9090/e9pay/data/v1/users
```