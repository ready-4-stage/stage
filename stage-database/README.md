# Stage Database

## Overview

In this module you'll find all DAO interfaces, and their implementations as
Spring beans for *Stage*.

- `stage.database` All interfaces.
- `stage.database.sqlite` The implementation of the interfaces by using SQLite.

## Usage

```xml
<dependency>
    <groupID>stage</groupID>
    <artifactId>stage-database</artifactId>
    <version>VERSION</version>
</dependency>
```

For the latest version see [Tags](https://github.com/ready-4-stage/stage/tags).

## Bean loading

Each implementation bean gets only loaded if a specific parameter is set. For
SQLite this would be e.g. `--database=sqlite`.
