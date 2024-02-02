<h2>1. 프로젝트명</h2>
<h2>2. 프로젝트 설명</h2>
<h2>3. 환경 설정</h2>
<ul>
  <li>Dependencies</li>
  
    dependencies {
      implementation 'org.springframework.boot:spring-boot-starter-web'
      implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
      implementation 'org.springframework.boot:spring-boot-starter-security'
    
      implementation group: 'com.h2database', name: 'h2', version: '2.2.220'
      implementation group: 'org.jsoup', name: 'jsoup', version: '1.15.3'
    
      compileOnly 'org.projectlombok:lombok'
      runtimeOnly 'com.h2database:h2'
      annotationProcessor 'org.projectlombok:lombok'
      testImplementation 'org.springframework.boot:spring-boot-starter-test'
    }
</ul>
