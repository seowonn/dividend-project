<h2>📌 프로젝트 명 : Dividend Information Inquiry Service</h2>
미국 주식 배당금 정보를 제공하는 API 서비스
<p></p>
<h2>📌 구현 초점</h2>
<ul>
  <li>웹 페이지를 분석하고 스크래핑 기법을 활용하여 필요한 데이터를 추출/저장한다.</li>
  <li>사용자별 데이터를 관리하고 예상 배당금 액수를 계산할 수 있다.</li>
  <li>서비스에서 캐시의 필요성을 이해하고 캐시 서버를 구성한다.</li>
</ul>
<p></p>

<style>
.badge {
    margin-right: 10px; /* 배지 사이의 간격 조절 */
}
</style>

<h2>📌 사용 기술 스택 및 환경 설정 (2024.02 기준)</h2>
<h3>기술 스택</h3>

<div class="badge-container">
    <img src="https://img.shields.io/badge/SpringBoot-green?style=flat-square" alt="SpringBoot" class="badge" height="25">
    <img src="https://img.shields.io/badge/java-orange?style=flat-square" alt="Java" class="badge" height="25">
    <img src="https://img.shields.io/badge/JPA-007396?style=flat-square" alt="JPA" class="badge" height="25">
    <img src="https://img.shields.io/badge/H2-blue?style=flat-square" alt="H2" class="badge" height="25">
    <img src="https://img.shields.io/badge/Redis-D82C20?style=flat-square" alt="Redis" class="badge" height="25">
    <img src="https://img.shields.io/badge/Jsoup-A83500?style=flat-square" alt="Jsoup" class="badge" height="25">
    <img src="https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=docker&logoColor=white" alt="Docker" class="badge" height="25">
</div>


<p></p>
<h3>환경 설정</h3>
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
<p></p><p></p>
<h2>📌 구현 항목</h2>
<h3>➡GET - finance/dividend/{companyname}</h3>

- [x]   회사 이름을 input으로 받아서 해당 회사의 메타 정보와 배당금 정보를 반환
- [x]   잘못된 회사명이 입력으로 들어온 경우 400ststus코드와 에러메시지를 반환
<p></p>

<h3>➡GET - company/autocomplete</h3>

- [x]   자동 완성 기능을 위한 API 구현
- [x]   검색하고자 하는 prefix를 입력으로 받고, 해당 prefix로 검색되는 회사명 리스트 총 10개를 반환

<h3>➡GET - company</h3>

- [x]   서비스에서 관리하고 있는 모든 회사 목록을 반환
- [x]   반환 결과는 Page 인터페이스 형태
<p></p>

<h3>➡POST - company</h3>

- [x]   새로운 회사 정보를 추가하는 기능
- [x]   추가하고자 하는 회사의 ticker를 입력으로 받아 해당 회사의 정보를 스크래핑하고 저장
- [x]   이미 보유하고 있는 회사의 정보일 경우 400status 코드와 적절한 에러 메시지를 반환
- [x]   존재하지 않는 회사 ticker일 경우 400status 코드와 적절한 에러 메시지를 반환
<p></p>

<h3>➡DELETE - company/{ticker}</h3>

- [x]   ticker에 해당하는 회사 정보 삭제 기능
- [x]   삭제 시 회사의 배당금 정보와 캐시도 모두 삭제됨
<p></p>

<h3>➡POST - auth/sighup</h3>

- [x]   회원 가입 API
- [x]   중복 ID는 허용하지 않음
- [x]   패스워드는 암호화된 형태로 저장됨
<p></p>

<h3>➡POST - auth/signin</h3>

- [x]   로그인 API
- [x]   회원가입이 되어 있고, 아이디/패스워드 정보가 옳은 경우 JWT를 발급해줌
<p></p>



