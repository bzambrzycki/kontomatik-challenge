package pl.zambrzyckib;

import java.io.IOException;

public interface BankConnection {
  ResponseDTO send(RequestDTO requestDTO) throws IOException;
}
