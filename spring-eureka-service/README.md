# README: Spring Gateway를 통한 Actuator Health Check 설정 및 활용

## 소개

Spring Eureka는 Netflix OSS의 Eureka를 기반으로 한 서비스 등록 및 발견(Discovery) 서버입니다. 이를 통해 Microservices 간의 동적인 서비스 레지스트리를 제공하며, 클라이언트 애플리케이션이 다른 서비스의 위치를 알아내고 호출할 수 있습니다.

이 README는 Eureka Server와 클라이언트 설정 및 활용 방법을 안내합니다.

이 문서는 Spring Gateway를 통해 Microservice의 `/actuator/health`를 확인하는 방법에 대해 설명합니다. Gateway를 활용하면 여러 서비스의 상태를 한 곳에서 쉽게 확인할 수 있습니다.

---

## 주요기능

- 서비스 등록: 각 서비스는 Eureka 서버에 자신을 등록하여 다른 서비스가 이를 검색할 수 있게 합니다.
- 서비스 발견: 클라이언트 애플리케이션은 Eureka를 통해 동적으로 서비스 위치를 검색합니다.
- 헬스 체크: Eureka는 등록된 서비스의 상태를 주기적으로 확인하여 사용 가능 여부를 관리합니다.

---

## 실행 및 테스트

### 1. Eureka Server 실행
- Eureka 서버 애플리케이션을 실행한 후, 웹 브라우저에서 http://localhost:8761에 접속하여 대시보드를 확인합니다.

### 2. 클라이언트 애플리케이션 실행
- 클라이언트 서비스(Spring User Service 등)를 실행합니다. 실행 후 Eureka 대시보드에서 해당 서비스가 UP 상태로 표시되는지 확인합니다.

### 3. 서비스 호출
- Eureka 클라이언트를 사용하면 서비스의 호스트와 포트를 직접 알지 못해도, 서비스 이름을 기반으로 동적으로 호출할 수 있습니다.

--- 

## 요구 사항

- **Spring Boot**: 3.1.x 이상
- **Spring Cloud**: 2023.0.x (Dublin 릴리스)
- **Java**: 17 이상

---

## 설정

### 1. Gateway 설정

#### `build.gradle`

```gradle
ext {
    set('springCloudVersion', "2023.0.4")
}

dependencyManagement {
    imports {
        mavenBom "org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}"
    }
}

dependencies {
    implementation 'org.springframework.cloud:spring-cloud-starter-gateway'
    implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-client'
    implementation 'io.netty:netty-resolver-dns-native-macos:4.1.96.Final' // MacOS DNS 문제 해결
}
```

---

## gateway를 통한 클라이언트 서비스 설정
- gateway(8081)를 통한 각 클라이언트(admin, users)에 대한 서비스 설정 yml 파일

```gradle

spring:
  application:
    name: spring-gateway-service

  cloud:
    gateway:
      routes:
        # /user-service/health → spring-user-service의 /actuator/health
        - id: user-service-health-route
          uri: lb://spring-user-service
          predicates:
            - Path=/user-service/health
          filters:
            - RewritePath=/user-service/health,/actuator/health

        # /admin-service/health → spring-admin-service의 /actuator/health
        - id: admin-service-health-route
          uri: lb://spring-admin-service
          predicates:
            - Path=/admin-service/health
          filters:
            - RewritePath=/admin-service/health,/actuator/health

      default-filters:
        - AddRequestHeader=Gateway-Request, GatewayRequestHeaderValue

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/ # Eureka 서버 URL
    register-with-eureka: true
    fetch-registry: true

management:
  endpoints:
    web:
      exposure:
        include: "*" # 모든 Actuator 엔드포인트 활성화
  endpoint:
    health:
      show-details: always # Health 상태의 세부 정보 활성화

server:
  port: 8081

```
