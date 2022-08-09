package ru.skillbox.diplom.group25.microservice.post.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.skillbox.diplom.group25.microservice.post.resource.PostResource;

/**
 * PostFeignClient
 *
 * @author alex90bar
 */


//  для запуска в kubernetes сделать url=name
//  для запуска локально http://130.61.44.151
@FeignClient(name = "microservice-post", url =  "${rest.account-service-host}")
public interface PostFeignClient extends PostResource{

}
