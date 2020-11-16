# Kontomatik Challenge App

Bank account information scraper made for the sake of recruitment into Kontomatik company. Currently working only with PKO Bank Polski accounts, which do not use IKO mobile 2FA.

## Requirements

* Java 11
* Lombok plugin

## Usage

```bash
java -jar kontomatik-challenge.jar "$yourPkoLogin" "$yourPkoPassword"
```

## Example Output
```
Successfully fetched accounts info
Account: PKO KONTO DLA MŁODYCH, balance: x PLN
```
