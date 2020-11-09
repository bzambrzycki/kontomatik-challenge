# Kontomatik Challenge App

Bank account information scrapper made for the sake of recruitment into Kontomatik company. Currently working only with PKO Bank Polski accounts, that are not using IKO mobile 2FA.

## Used libraries

* Jsoup - to make calls to the bank's server
* Gson - for parsing responses body
* Lombok - to reduce some of the boilerplate code
* Vavr - to get access to Collections with more methods available to be called straight away, e.g. map on a List
## Usage

```bash
java -jar kontomatik-challenge.jar "$yourPkoLogin" "$yourPkoPassword"
```

## Example Output
```
Login sent
Password sent
Logged in successfully
Fetched accounts data
Account: PKO KONTO DLA MŁODYCH, balance: xxx PLN
```
