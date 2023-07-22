package cn.wenhe9.pinda.core.swagger;

import cn.wenhe9.pinda.core.swagger.value.DocketInfo;
import cn.wenhe9.pinda.core.swagger.value.GlobalOperationParameter;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.github.xiaoymin.knife4j.spring.configuration.Knife4jHttpBasic;
import com.github.xiaoymin.knife4j.spring.configuration.Knife4jProperties;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @description: swagger2配置类
 * @author: DuJinliang
 * @create: 2023/7/4
 */
@EnableKnife4j
@Configuration
@EnableConfigurationProperties(SwaggerProperties.class)
public class Swagger2Configuration  {
    private static final String AUTH_KEY = "token";

    private static final String BEARER_AUTH = "bearerAuth";

    private final SwaggerProperties swaggerProperties;

    public Swagger2Configuration(SwaggerProperties swaggerProperties) {
        this.swaggerProperties = swaggerProperties;
    }


    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "pinda.swagger.enabled", havingValue = "true", matchIfMissing = true)
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(
                    new Info()
                        .title(swaggerProperties.getTitle())
                        .description(swaggerProperties.getDescription())
                        .version(swaggerProperties.getVersion())
                        .license(
                                new License()
                                        .name(swaggerProperties.getLicense())
                                        .url(swaggerProperties.getLicenseUrl())
                        )
                        .contact(
                                new Contact()
                                        .name(swaggerProperties.getContact().getName())
                                        .url(swaggerProperties.getContact().getUrl())
                                        .email(swaggerProperties.getContact().getEmail())
                        )
                        .termsOfService(swaggerProperties.getTermsOfServiceUrl())
                )
                .components(
                        new Components()
                            .securitySchemes(securityScheme())
                )
                .addSecurityItem(securityRequirement());
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "pinda.swagger.enabled", havingValue = "true", matchIfMissing = true)
    public Knife4jProperties knife4jProperties() {
        Knife4jProperties knife4jProperties = new Knife4jProperties();
        Knife4jHttpBasic knife4jHttpBasic = new Knife4jHttpBasic();

        knife4jHttpBasic.setEnable(swaggerProperties.getBasic().getEnable());
        knife4jHttpBasic.setUsername(swaggerProperties.getBasic().getUsername());
        knife4jHttpBasic.setPassword(swaggerProperties.getBasic().getPassword());

        knife4jProperties.setBasic(knife4jHttpBasic);

        return knife4jProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "pinda.swagger.enabled", havingValue = "true", matchIfMissing = true)
    public List<GroupedOpenApi> createRestApi() {
        return Optional.ofNullable(needGroupedOpenApi(swaggerProperties))
                        .map(docketInfoMap -> {
                            List<GroupedOpenApi> groupedOpenApiList = new ArrayList<>();
                            docketInfoMap.forEach((groupName, docketInfo) -> {
                                // base-path处理
                                // 当没有配置任何path的时候，解析/**
                                List<String> basePath = Optional.ofNullable(getActualBasePath(docketInfo.getBasePath()))
                                        .orElse(List.of("/**"));

                                // exclude-path处理
                                List<String> excludePath = Optional.ofNullable(getActualExcludePath(docketInfo.getExcludePath()))
                                        .orElse(Collections.emptyList());

                                // 合并全局参数
                                List<GlobalOperationParameter> assemblyGlobalOperationParameters = assemblyGlobalOperationParameters(
                                        swaggerProperties.getGlobalOperationParameters(),
                                        docketInfo.getGlobalOperationParameters()
                                );

                                GroupedOpenApi groupedOpenApi = GroupedOpenApi
                                        .builder()
                                        .packagesToScan(docketInfo.getBasePackage())
                                        .pathsToMatch(basePath.toArray(new String[0]))
                                        .pathsToExclude(excludePath.toArray(new String[0]))
                                        .group(docketInfo.getGroup())
                                        .addOpenApiCustomizer(openApi -> {
                                            buildGlobalOperationParametersFromSwaggerProperties(
                                                    assemblyGlobalOperationParameters
                                            );
                                        })
                                        .build();

                                groupedOpenApiList.add(groupedOpenApi);
                            });

                            return groupedOpenApiList;
                        })
                        .orElseGet(() -> {
                            // base-path处理 当不存在basePath时，匹配所有
                            List<String> basePath = Optional.ofNullable(getActualBasePath(swaggerProperties.getBasePath()))
                                    .orElse(List.of("/**"));

                            // exclude-path处理
                            List<String> excludePath = Optional.ofNullable(getActualExcludePath(swaggerProperties.getExcludePath()))
                                    .orElse(Collections.emptyList());

                            return Collections.singletonList(
                                    GroupedOpenApi
                                        .builder()
                                        .packagesToScan(swaggerProperties.getBasePackage())
                                        .pathsToMatch(basePath.toArray(new String[0]))
                                        .pathsToExclude(excludePath.toArray(new String[0]))
                                        .group(swaggerProperties.getGroup())
                                        .addOpenApiCustomizer(openApi -> {
                                            buildGlobalOperationParametersFromSwaggerProperties(
                                                    swaggerProperties.getGlobalOperationParameters()
                                            );
                                        })
                                        .build()
                            );
                        });
    }

    private Map<String, DocketInfo> needGroupedOpenApi(SwaggerProperties swaggerProperties) {
        return !CollectionUtils.isEmpty(swaggerProperties.getDocket()) ? swaggerProperties.getDocket() : null;
    }

    private List<String> getActualBasePath(List<String> basePath) {
        return !CollectionUtils.isEmpty(basePath) ? basePath : null;
    }

    private List<String> getActualExcludePath(List<String> excludePath) {
        return !CollectionUtils.isEmpty(excludePath) ? excludePath : null;
    }

    private SecurityRequirement securityRequirement() {
        SecurityRequirement securityRequirement = new SecurityRequirement();
        securityRequirement.addList("bearerAuth", List.of("global"));
        return securityRequirement;
    }

    private Map<String, SecurityScheme> securityScheme() {
        Map<String, SecurityScheme> securitySchemes = new HashMap<>();

        // 添加安全方案，例如 Basic Auth 或 Bearer Token
        SecurityScheme securityScheme = new SecurityScheme()
                .name(AUTH_KEY)
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER)
                .scheme("bearer")
                .bearerFormat("JWT");
        securitySchemes.put("bearerAuth", securityScheme);

        return securitySchemes;
    }

    private List<Parameter> buildGlobalOperationParametersFromSwaggerProperties(
            List<GlobalOperationParameter> globalOperationParameters
    ) {
        return globalOperationParameters.stream()
                .map(operationParameter -> {
                    return new Parameter()
                            .name(operationParameter.getName())
                            .description(operationParameter.getDescription())
                            .in(operationParameter.getParameterType())
                            .required(operationParameter.getRequired())
                            .example(operationParameter.getDefaultValue())
                            .allowEmptyValue(operationParameter.getAllowEmptyValue());
                })
                .collect(Collectors.toList());
    }

    /**
     * 局部参数按照name覆盖局部参数
     */
    private List<GlobalOperationParameter> assemblyGlobalOperationParameters(
            List<GlobalOperationParameter> globalOperationParameters,
            List<GlobalOperationParameter> docketOperationParameters) {

        if (CollectionUtils.isEmpty(docketOperationParameters)) {
            return globalOperationParameters;
        }

        Set<String> docketNames = docketOperationParameters.stream()
                .map(GlobalOperationParameter::getName)
                .collect(Collectors.toSet());

        return Stream.concat(
                        globalOperationParameters.stream()
                                .filter(parameter -> !docketNames.contains(parameter.getName())),
                        docketOperationParameters.stream()
                )
                .collect(Collectors.toList());
    }

}
