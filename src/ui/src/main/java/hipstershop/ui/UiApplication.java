package hipstershop.ui;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;





@SpringBootApplication
@RestController
public class UiApplication {


  @RequestMapping(value = "/**", method = {RequestMethod.GET, RequestMethod.POST})
  public String home(HttpServletRequest request) {
    String method = request.getMethod();

    String host = System.getenv("FRONTEND_ADDR") != null ? System.getenv("FRONTEND_ADDR") : "localhost:8081";

    String uri = "http://" + host + request.getRequestURI();

    Map<String, String[]> params =  request.getParameterMap();

    System.out.println((method.equals("POST")) + " " + method + ": " + uri);


    RequestHeadersUriSpec<?> rhus = (method.equals("POST")) ? (RequestHeadersUriSpec<?>) WebClient.create().post().body(Mono.just(params), Map.class) : WebClient.create().get();

    rhus.uri(uri).exchangeToFlux(response -> extracted(response)).blockFirst();
    return "Hello Docker World";
  }

  private Flux<Object> extracted(ClientResponse response) {
    System.out.println(response.statusCode());
    return Flux.empty();
  }

  public static void main(String[] args) {
    SpringApplication.run(UiApplication.class, args);
  }

}
