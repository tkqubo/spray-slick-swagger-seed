# Spray-Slick-Swagger seed

Scala seed project with:

- [Spray](http://spray.io/) for Web framework
- [Swagger](http://swagger.io/) for API documentation
- [Slick](http://slick.typesafe.com/) for Database library
- [Flyway](https://flywaydb.org/) for Database migration

## Quick start

```sh
# clone repo from GitHub
git clone https://github.com/tkqubo/spray-slick-swagger-seed.git

# create database for local environment (assuming MySQL is already installed on your machine)
mysql -e 'CREATE DATABASE sample;'

# setup database
sbt dbClean dbMigrate

# run the server
sbt run
```

## SBT tasks

### Test

```sh
sbt test
```

### Migrate database

```sh
sbt dbMigrate
```

### Clean database

```sh
sbt dbClean
```

### Generate database migration file

```sh
# xxx comes any description for the migration
sbt "generateMigration xxx"
```

### Create executable

```sh
sbt universal:stage
# execute as follows
# ./target/universal/stage/bin/spray-slick-swagger-seed -Denvironment=localhost
```

