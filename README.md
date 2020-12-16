# Kontomatik Challenge App

Bank account information scraper made for the sake of recruitment into Kontomatik company. Currently working only with
PKO Bank Polski accounts, which do not use IKO mobile 2FA.

### Notes

* Project uses Java 11 and gets built with gradle
* Code is formatted using google-java-format

## Requirements

* CLI (I recommend using Git Bash)
* Java 11
* Lombok plugin

## Configuration

### Lombok plugin

To setup Lombok plugin in IntelliJ, follow this guide:  
https://projectlombok.org/setup/intellij

## Usage

### Tests

If you plan to run all the tests, or Integration tests only, you need to provide bank credentials first. To do it,
rename the **credentials.properties.sample** file by deleting the **.sample** extension, and fill it with PKO bank
credentials you want to test the scraper with. Git will not track this file if you make any commits later, but you
should not keep your plaintext password in the source code too long anyway.

#### Running all tests

To run all the tests, use

```bash
./gradlew test
```

#### Running unit tests

To run only the unit tests, use

```bash
./gradlew unitTest
```

#### Running integration tests

To run only the integration tests, use

```bash
./gradlew integrationTest
```

### Build & Run

To run the project using one command, use the below command from the project root directory(.../kontomatik-challenge
given you didn't change the directory name), don't forget the quotes

```bash
./gradlew jar && java -jar build/libs/kontomatik-challenge.jar "$yourPkoLogin" "$yourPkoPassword"
```

#### Building

To only build the project to jar file use the command below

```bash
./gradlew jar
```

#### Running

To only run the built jar, use the command below. Make sure to specify correct path to the built jar (by default, it
should be preceded by ./build/libs/)

```bash
$ java -jar kontomatik-challenge.jar "$yourPkoLogin" "$yourPkoPassword"
```

### Example Output

```
Account: PKO KONTO DLA MŁODYCH, balance: x PLN
```
