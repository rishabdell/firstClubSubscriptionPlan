package com.example.membership.plan.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenApiCustomizer removeAllExamples() {
        return openApi -> {
            if (openApi == null) {
                return;
            }

            Components components = openApi.getComponents();
            if (components != null) {
                if (components.getSchemas() != null) {
                    components.getSchemas().values().forEach(this::clearSchemaExamples);
                }
                if (components.getResponses() != null) {
                    components.getResponses().values().forEach(this::clearApiResponseExamples);
                }
                if (components.getRequestBodies() != null) {
                    components.getRequestBodies().values().forEach(this::clearRequestBodyExamples);
                }
            }

            if (openApi.getPaths() != null) {
                for (PathItem pathItem : openApi.getPaths().values()) {
                    if (pathItem == null) {
                        continue;
                    }
                    pathItem.readOperations().forEach(this::clearOperationExamples);
                }
            }
        };
    }

    private void clearOperationExamples(Operation operation) {
        if (operation == null) {
            return;
        }

        if (operation.getParameters() != null) {
            operation.getParameters().forEach(this::clearParameterExamples);
        }

        clearRequestBodyExamples(operation.getRequestBody());
        clearApiResponsesExamples(operation.getResponses());
    }

    private void clearParameterExamples(Parameter parameter) {
        if (parameter == null) {
            return;
        }
        parameter.setExample(null);
        parameter.setExamples(null);
        clearSchemaExamples(parameter.getSchema());
    }

    private void clearRequestBodyExamples(RequestBody requestBody) {
        if (requestBody == null || requestBody.getContent() == null) {
            return;
        }
        requestBody.getContent().values().forEach(this::clearRequestBodyMediaType);
    }

    private void clearRequestBodyMediaType(MediaType mediaType) {
        if (mediaType == null) {
            return;
        }
        mediaType.setExample(null);
        mediaType.setExamples(null);
        clearSchemaExamples(mediaType.getSchema());
    }

    private void clearApiResponsesExamples(ApiResponses responses) {
        if (responses == null) {
            return;
        }
        responses.values().forEach(this::clearApiResponseExamples);
    }

    private void clearApiResponseExamples(ApiResponse response) {
        if (response == null || response.getContent() == null) {
            return;
        }
        response.getContent().values().forEach(this::clearMediaTypeExamples);
        response.getContent().entrySet().removeIf(entry -> {
            MediaType mediaType = entry.getValue();
            return mediaType == null || mediaType.getSchema() == null;
        });
        if (response.getContent().isEmpty()) {
            response.setContent(null);
        }
    }

    private void clearMediaTypeExamples(MediaType mediaType) {
        if (mediaType == null) {
            return;
        }
        mediaType.setExample(null);
        mediaType.setExamples(null);
        clearSchemaExamples(mediaType.getSchema());
    }

    private void clearSchemaExamples(Schema<?> schema) {
        if (schema == null) {
            return;
        }

        schema.setExample(null);
        schema.setExamples(null);

        if (schema.getProperties() != null) {
            schema.getProperties().values().stream()
                    .filter(Schema.class::isInstance)
                    .map(Schema.class::cast)
                    .forEach(this::clearSchemaExamples);
        }
        clearSchemaExamples(schema.getItems());
        if (schema.getAdditionalProperties() instanceof Schema) {
            clearSchemaExamples((Schema<?>) schema.getAdditionalProperties());
        }
        if (schema.getNot() != null) {
            clearSchemaExamples(schema.getNot());
        }
        if (schema.getAllOf() != null) {
            schema.getAllOf().forEach(this::clearSchemaExamples);
        }
        if (schema.getOneOf() != null) {
            schema.getOneOf().forEach(this::clearSchemaExamples);
        }
        if (schema.getAnyOf() != null) {
            schema.getAnyOf().forEach(this::clearSchemaExamples);
        }
    }
}
