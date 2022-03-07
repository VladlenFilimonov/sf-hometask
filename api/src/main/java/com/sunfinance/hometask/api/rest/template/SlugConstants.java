package com.sunfinance.hometask.api.rest.template;

import java.util.Map;

public class SlugConstants {

    public static final String EMAIL = "email-verification";
    public static final String MOBILE = "mobile-verification";

    private static final Map<String, Slug> slugs = Map.of(
            EMAIL, Slug.EMAIL,
            MOBILE, Slug.MOBILE);

    public static Slug getSlug(String slug) {
        return slugs.get(slug);
    }

}
