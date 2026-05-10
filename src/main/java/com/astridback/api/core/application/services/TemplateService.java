package com.astridback.api.core.application.services;

import java.util.Map;

public interface TemplateService {
    String render(String templateName, Map<String, Object> model);
}
