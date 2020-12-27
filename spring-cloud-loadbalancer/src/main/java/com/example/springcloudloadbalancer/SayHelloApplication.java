package com.example.springcloudloadbalancer;

import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@RestController
public class SayHelloApplication {

	private final WebClient.Builder loadBalancedWebClientBuilder;
	
	private final ReactorLoadBalancerExchangeFilterFunction lbFunction;

	public SayHelloApplication(WebClient.Builder loadBalancedWebClientBuilder, 
			ReactorLoadBalancerExchangeFilterFunction lbFunction) {
		this.loadBalancedWebClientBuilder = loadBalancedWebClientBuilder;
		this.lbFunction = lbFunction;
	}

	 @RequestMapping("/hi")
	  public Mono<String> hi(@RequestParam(value = "name", defaultValue = "Mary") String name) {
	    return loadBalancedWebClientBuilder.build().get().uri("http://say-hello/message")
	        .retrieve().bodyToMono(String.class)
	        .map(greeting -> String.format("%s, %s!", greeting, name));
	  }
	
}
