package com.example.user_management_ms.feign;

import com.example.user_management_ms.entities.Address;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url= "https://viacep.com.br/ws/" , name = "viacep")
public interface AddressFeign {

    @GetMapping("{cep}/json")
    Address findAddressByCep(@PathVariable("cep") String cep);
}
