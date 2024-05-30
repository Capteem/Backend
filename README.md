# Plog - 나만의 사진촬영 패키지를 만들어 보자

## 역할 분담
|주명하|김태헌|
|:---:|:---:|
|DB설계|DB설계|
|API 명세서 작성 및 구현|API 명세서 작성 및 구현|
|리드미 작성|인프라 구축|

## 기술 스택
<div>
  <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">
  <img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"/> 
  <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"/>
</div>
</br>
<img src="https://img.shields.io/badge/aws-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white"/>
<img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"/>


## build.gradle
```gradle
plugins {
	id 'java'
	id 'org.springframework.boot' version '3.2.4'
	id 'io.spring.dependency-management' version '1.1.4'
}

group = 'com.plog'
version = '0.0.1-SNAPSHOT'

java {
	sourceCompatibility = '17'
}

configurations {
	compileOnly {
		extendsFrom annotationProcessor
	}
}

repositories {
	mavenCentral()
}

dependencies {

	implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0'

	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
	implementation 'org.springframework.boot:spring-boot-starter-web'
	compileOnly 'org.projectlombok:lombok'
	runtimeOnly 'com.mysql:mysql-connector-j'
	annotationProcessor 'org.projectlombok:lombok'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'


	implementation 'org.projectlombok:lombok'
	implementation 'org.springframework.boot:spring-boot-starter-webflux'

	//authentication
	implementation 'javax.mail:mail:1.4.7'
	implementation 'org.springframework:spring-context-support:5.3.9'


	//db
	implementation 'org.springframework.boot:spring-boot-starter-jdbc'
	implementation 'com.h2database:h2'
	implementation 'org.springframework.security:spring-security-core:6.2.3'

	//jwt
	implementation 'io.jsonwebtoken:jjwt:0.9.1'
	implementation 'javax.xml.bind:jaxb-api:2.3.1'


}

tasks.named('test') {
	useJUnitPlatform()
}
```

## ERD
![db_erd_최종](https://github.com/Capteem/Backend/assets/80399640/1bbe47a4-d0bd-4b88-9b63-d9aadad65b07)

## 서버 아키텍처
![image](https://github.com/Capteem/Backend/assets/80399640/a06b1162-f6cf-4e7f-b4d1-8585a692acb5)


## 프로젝트 폴더 구조
```
├─common
│  └─file
├─config
│  └─interceptor
├─controller
│  ├─admin
│  ├─complaint
│  ├─confirm
│  ├─payment
│  ├─portfolio
│  ├─provider
│  ├─reservation
│  ├─review
│  ├─sign
│  └─user
├─dto
│  ├─admin
│  ├─complaint
│  ├─confirm
│  ├─file
│  ├─payment
│  ├─portfolio
│  ├─Provider
│  ├─reservation
│  ├─review
│  │  └─comment
│  ├─sign
│  ├─user
│  └─workdate
├─exception
├─model
├─repository
├─schedule
└─service
    ├─admin
    ├─complaint
    ├─confirm
    ├─payment
    ├─portfolio
    ├─Provider
    ├─reservation
    ├─review
    ├─sign
    └─user
```
