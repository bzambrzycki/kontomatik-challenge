package pl.zambrzyckib;

import pl.zambrzyckib.dto.RequestDTO;
import pl.zambrzyckib.dto.ResponseDTO;

public interface BankConnection {
  ResponseDTO send(RequestDTO requestDTO);
}
