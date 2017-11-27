package works.weave.socks.broker.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.servicecomb.provider.rest.common.RestSchema;

@RestSchema(schemaId = "broker")
@RequestMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
public class BrokerController {
  private static RestTemplate restTemplate;

  @ResponseStatus(HttpStatus.OK)
  @RequestMapping(value = "auth", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
  public long get() {
    return new Random().nextLong() % 10000;
  }

  @ResponseStatus(HttpStatus.CREATED)
  @RequestMapping(value = "specificCurrency/{name}", consumes = MediaType.APPLICATION_JSON_VALUE,
      method = RequestMethod.POST)
  public @ResponseBody String getCurrency(@RequestBody String name) {
    String converter = System.getenv("CONVERTER_NAME");
    String prefix = "cse://" + converter;
    String response = restTemplate.getForObject(prefix + "/currency", String.class);

    Map<String, String> map = new HashMap<String, String>();
    ObjectMapper mapper = new ObjectMapper();
    // convert JSON string to Map
    try {
      map = mapper.readValue(response, new TypeReference<Map<String, String>>() {
      });
    } catch (IOException e) {
      e.printStackTrace();
    }

    String result = map.get(name);
    return result;
  }
}
