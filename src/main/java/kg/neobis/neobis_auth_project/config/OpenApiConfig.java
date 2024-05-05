package kg.neobis.neobis_auth_project.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Diyas",
                        email = "imashevdt@gmail.com"
                ),
                description = "Open API documentation for Auth Project",
                title = "Auth Project"
        )
)
public class OpenApiConfig {}
