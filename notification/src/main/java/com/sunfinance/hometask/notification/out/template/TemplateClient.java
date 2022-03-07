package com.sunfinance.hometask.notification.out.template;

import com.sunfinance.hometask.api.rest.template.TemplateGetBodyOutRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(url = "${template.baseUrl}", name = "template-client")
public interface TemplateClient {

    @RequestMapping(method = RequestMethod.POST, value = "/render")
    String getBody(TemplateGetBodyOutRequest request);

}
