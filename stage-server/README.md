# Stage Server

## Overview

In this module you'll find the main REST service for *Stage*, including its
business logic. It uses `stage-common` and `stage-database`.

## Usage

```bash
# In root directory of the project
mvn clean install
java -jar stage-server/target/stage-server-VERSION.jar --database=sqlite
```

For the latest version see [Tags](https://github.com/ready-4-stage/stage/tags).
