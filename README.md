### 1. Web Service 시작 
BookSearchApplication.java 실행

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

