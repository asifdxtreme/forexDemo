package works.weave.socks.broker.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class BrokerController {

  @ResponseStatus(HttpStatus.OK)
  @RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
  public long get() {
    return new Random().nextLong() % 1000;
  }

  @ResponseStatus(HttpStatus.CREATED)
  @RequestMapping(value = "/specificCurrency/{name}", consumes = MediaType.APPLICATION_JSON_VALUE,
      method = RequestMethod.POST)
  public long getCurrency(@PathVariable String name) {
    String response = null;
    try {
      String addr = System.getenv("CONVERTER_ADDR");
      URL url = new URL(addr + "currency");
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      conn.setRequestProperty("Accept", "application/json");

      if (conn.getResponseCode() != 200) {
        throw new RuntimeException("Failed : HTTP error code : "
            + conn.getResponseCode());
      }

      BufferedReader br = new BufferedReader(new InputStreamReader(
          (conn.getInputStream())));

      System.out.println("Output from Server .... \n");
      while ((response = br.readLine()) != null) {

      }

      conn.disconnect();

    } catch (MalformedURLException e) {

      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    Map<String, Object> map = new HashMap<String, Object>();
    ObjectMapper mapper = new ObjectMapper();
    // convert JSON string to Map
    try {
      map = mapper.readValue(response, new TypeReference<Map<String, Long>>() {
      });
    } catch (IOException e) {
      e.printStackTrace();
    }
    long result = (long) map.get(name);
    System.out.println(map);
    return result;
  }
}
