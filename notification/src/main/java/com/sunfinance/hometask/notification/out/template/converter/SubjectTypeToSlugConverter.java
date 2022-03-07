package com.sunfinance.hometask.notification.out.template.converter;

import com.sunfinance.hometask.api.SubjectType;
import com.sunfinance.hometask.api.rest.template.SlugConstants;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SubjectTypeToSlugConverter implements Converter<SubjectType, String> {

    @Override
    public String convert(SubjectType source) {
        switch (source) {
            case MOBILE_CONFIRMATION:
                return SlugConstants.MOBILE;
            case EMAIL_CONFIRMATION:
                return SlugConstants.EMAIL;
        }
        throw new RuntimeException("No supported Subject Type found");
    }
}
