package br.com.edu.ifce.maracanau.carekobooks.common.customizer;

import jakarta.servlet.http.HttpServletRequest;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiOAuthProperties;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springdoc.webmvc.ui.SwaggerIndexPageTransformer;
import org.springdoc.webmvc.ui.SwaggerWelcomeCommon;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.resource.ResourceTransformerChain;
import org.springframework.web.servlet.resource.TransformedResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Component
public class SwaggerCustomizer extends SwaggerIndexPageTransformer {

    @Value("${springdoc.swagger-ui.custom-css-path}")
    private String path;

    public SwaggerCustomizer(
            final SwaggerUiConfigProperties swaggerUiConfig,
            final SwaggerUiOAuthProperties swaggerUiOAuthProperties,
            final SwaggerWelcomeCommon swaggerWelcomeCommon,
            final ObjectMapperProvider objectMapperProvider
    ) {
        super(swaggerUiConfig, swaggerUiOAuthProperties, swaggerWelcomeCommon, objectMapperProvider);
    }

    @Override
    public @NonNull Resource transform(
            @NonNull HttpServletRequest request,
            @NonNull Resource resource,
            @NonNull ResourceTransformerChain transformer
    ) throws IOException {
        if ("index.html".equals(resource.getFilename())) {
            try (var in = resource.getInputStream(); var reader = new BufferedReader(new InputStreamReader(in))) {
                var html = reader.lines().collect(Collectors.joining(System.lineSeparator()));
                var transformedHtml = getTransformedHtml(html);
                return new TransformedResource(resource, transformedHtml.getBytes());
            }
        }

        return super.transform(request, resource, transformer);
    }

    private String getTransformedHtml(String html) {
        var linkTag = """
            <link rel="stylesheet" type="text/css" href="%s"/>
            """.formatted(path);

        return html.replace("</head>", linkTag + "</head>");
    }

}
