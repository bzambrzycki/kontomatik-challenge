# Kontomatik Challenge App

Bank account information scraper made for the sake of recruitment into Kontomatik company. Currently working only with PKO Bank Polski accounts, which do not use IKO mobile 2FA.
###Notes
* Project uses Java 11 and gets built with gradle
* Code is formatted using google-java-format
## Requirements

* CLI (I recommend using Git Bash)
* Java 11
* Lombok

### Configuration
####Java 11
Java JDK configuration using Git Bash:\
Run
```bash
$ java -version
```
If it tells you that the version is at least 11 you are good to go, otherwise you need to:
* Download JDK11 and unpack it
* Edit PATH environment variable, by adding a new variable with name JAVA_HOME, and value being the path to the previously downloaded JDK's bin folder
####Lombok
Lombok IntelliJ configuration:\
Download the plugin
``` 
File > Settings > Plugins > download Lombok > Restart IDE
```
Once restarted, enable annotation processing so that the IDE recognizes Lombok-generated methods
``` 
File > Settings > Build, Execution, Deployments > Compiler > Annotation Processors > check Enable Annotation Processing > Apply
```
## Usage

### Running tests
#### Unit tests
To run unit tests, use
```bash
./gradlew unitTest
```
#### Integration tests
Integration tests connect to the bank, and test whether the app reacts correctly to the received responses, so in order to run them, you need to provide a credentials.properties file in the test/.../resources directory. You can take the EDITME.properties template, fill it with correct credentials and rename it. Git will not track this file if you make any commits later, but you shouldn't keep your password in the source code too long anyway. Once the credentials file is there, run integration tests using
```bash
./gradlew integrationTest
```

### Building
Build the project to jar using the command below
```bash
./gradlew jar
```
### Running
Run using command below, make sure to specify correct path to the built jar (by default, it should be preceded by ./build/libs/jar/), and don't forget the quotes when providing credentials
```bash
java -jar kontomatik-challenge.jar "$yourPkoLogin" "$yourPkoPassword"
```

### Example Output
```
Successfully fetched accounts info
Account: PKO KONTO DLA MŁODYCH, balance: x PLN
```
