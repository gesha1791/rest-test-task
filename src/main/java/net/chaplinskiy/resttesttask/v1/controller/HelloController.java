package net.chaplinskiy.resttesttask.v1.controller;

import net.chaplinskiy.resttesttask.payload.Request;
import net.chaplinskiy.resttesttask.payload.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.time.Duration;

@RestController("helloControllerV1")
@RequestMapping(value = "/v1")
public class HelloController {

    @Value("${resttemplate.url}")
    private String url;

    @Value("${resttemplate.timeout}")
    private Duration timeout;

    private RestTemplate restTemplate;

    @Autowired
    public HelloController(RestTemplateBuilder builder) {
        this.restTemplate = builder
                .setReadTimeout(timeout)
                .build();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/home")
    public ResponseEntity<?> getMessage(@Valid @RequestBody Request request) {
        ResponseEntity<Void> exchange = restTemplate.exchange(url + request.getName(), HttpMethod.GET, null, Void.class);
        Response answer = getAnswer(exchange.getStatusCode(), request.getName());

        return new ResponseEntity<>(answer, HttpStatus.OK);
    }

    private Response getAnswer(HttpStatus statusCode, String name) {
        Response response = new Response();
        if(statusCode == HttpStatus.OK){
            String format = String.format("%d, сервис приветствует тебя", name);
            response.setReply(format);
        } else if (statusCode == HttpStatus.NOT_FOUND){
            String format = String.format("Извините, %d, но я вас не знаю", name);
            response.setReply(format);
        } else if (statusCode == HttpStatus.GATEWAY_TIMEOUT){
            response.setReply("Никого нет дома");
        }
        return response;
    }
}
