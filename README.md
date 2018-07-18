### 1. Web Service 시작 
ConferenceScheduleApplication.java 실행

### 2. Mail Service
application.properties 파일 수정 : 사용자 username, password 작성 후 실행
<pre><code>#spring mail
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=example@gmail.com
spring.mail.password=example_pw</code></pre>

### 3. 라이브러리
lombok, handlebars, bootstrap, jquery, font-awesome
<pre><code>compile 'org.projectlombok:lombok:1.16.18'           
compile('pl.allegro.tech.boot:handlebars-spring-boot-starter:0.3.0')
compile('org.webjars:jquery:3.3.1-1',
        'org.webjars:jquery-ui:1.12.1',
        'org.webjars:bootstrap:4.1.1',
        'org.webjars.bower:font-awesome:4.7.0')
</code></pre>

### 4. 설계
일별로 회의실과 예약시간, 빈 회의 일정을 일정 주기로 스케줄러를 통해 미리 생성해 두는 것으로 가정.
빈 회의 일정에 하나의 먼저 선점한 사용자가 회의 예약을 진행