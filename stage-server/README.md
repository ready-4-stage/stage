# Stage Server

## Overview

In this module you'll find the main REST service for *Stage*, including its
business logic. It uses `stage-common` and `stage-database`.

## Usage

### Running

```bash
# In root directory of the project
mvn clean install
java -jar stage-server/target/stage-server-VERSION.jar -Dspring.profiles.active=dev -Dstage.db.user=USER -Dstage.db.pwd=PASSWORD
```

For the latest version see [Tags](https://github.com/ready-4-stage/stage/tags).

### Authentication

Call `/authenticate` with the following headers:

- `username=USERNAME`
- `password=PASSWORD`

Submit the retrieved token as a *Bearer Token* to all requests.
(In Postman you'll find this option underneath *Authorization*).
