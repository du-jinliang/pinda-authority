plugins {
    id("java-library")
    id("org.springframework.boot")
}

description = "核心模块"

dependencies {
    api("org.springframework.boot:spring-boot-starter-web") {
        exclude("org.springframework.boot", "spring-boot-starter-tomcat")
    }
    api("org.aspectj:aspectjrt")
    api("org.springframework.boot:spring-boot-starter-undertow")
    api("org.springframework.cloud:spring-cloud-starter-bootstrap")
    api("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-discovery")
    api("com.alibaba.cloud:spring-cloud-starter-alibaba-nacos-config")
    api("com.github.xiaoymin:knife4j-openapi3-jakarta-spring-boot-starter")
    annotationProcessor ("org.springframework.boot:spring-boot-configuration-processor")
    api("cn.hutool:hutool-all")
    api("com.alibaba:fastjson")
    api("org.slf4j:slf4j-api:2.0.7")
}
