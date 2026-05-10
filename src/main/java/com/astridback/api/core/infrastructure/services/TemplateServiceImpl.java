package com.astridback.api.core.infrastructure.services;

import com.astridback.api.core.application.services.TemplateService;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.StringWriter;
import java.util.Map;

public class TemplateServiceImpl implements TemplateService {
    private final Configuration freemarker;

    public TemplateServiceImpl(Configuration freemarker) {
        this.freemarker = freemarker;
    }

    public String render(String templateName, Map<String, Object> model) {
        try {
            final Template template = freemarker.getTemplate(templateName);

            final StringWriter writer = new StringWriter();
            template.process(model, writer);

            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException("Template rendering failed", e);
        }
    }
}