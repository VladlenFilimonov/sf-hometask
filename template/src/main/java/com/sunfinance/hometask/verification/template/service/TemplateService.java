package com.sunfinance.hometask.verification.template.service;

import com.samskivert.mustache.Mustache;
import com.sunfinance.hometask.api.rest.template.SlugConstants;
import com.sunfinance.hometask.api.rest.template.TemplateGetBodyOutRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TemplateService {

    public String render(TemplateGetBodyOutRequest request) {
        var slug = SlugConstants.getSlug(request.getSlug());
        switch (slug) {
            case EMAIL:
                return Mustache.compiler()
                               .compile(prepareMailTemplate())
                               .execute(request.getVariables());
            case MOBILE:
                return Mustache.compiler()
                               .compile(prepareMobileTemplate())
                               .execute(request.getVariables());
        }
        throw new RuntimeException();
    }

    //TODO move templates to DB
    private String prepareMailTemplate() {
        return "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<head>\n"
                + "    <title>Email verification</title>\n"
                + "    <style>\n"
                + "        .content {\n"
                + "            margin: auto;\n"
                + "            width: 600px;\n"
                + "        }\n"
                + "    </style>\n"
                + "</head>\n"
                + "<body>\n"
                + "    <div class=\"content\">\n"
                + "        <p>Your verification code is {{ code }}.</p>\n"
                + "    </div>\n"
                + "</body>\n"
                + "</html>";
    }

    private String prepareMobileTemplate() {
        return "Your verification code is {{ code }}.";
    }
}
