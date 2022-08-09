package ru.skillbox.diplom.group25.microservice.post.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.skillbox.diplom.group25.microservice.account.model.AccountDto;
import ru.skillbox.diplom.group25.microservice.post.configuration.FeignClientConfiguration;

/**
 * AccountServiceFeignClient
 *
 * @author alex90bar
 */
//TODO Удали класс и попроси ребят из аккаунта или сделай коммит у них в аккаунте и используй из аккаунта
@FeignClient(name = "microservice-account", url =  "${rest.account-service-host}", configuration = FeignClientConfiguration.class)
public interface AccountServiceFeignClient{


  @GetMapping("/api/v1/account/{id}")
  AccountDto getAccountById(@PathVariable("id") Long id);


}
