# supreme-broccoli


## Jar 파일 다운로드 경로
https://drive.google.com/file/d/1JlRMslogGomuojD4mW2GccJcsRvGofeo/view?usp=sharing

---

## Jar 파일 실행 명령어
<u>**jasypt으로 키를 암호화하여, 아래 명령어로 VM 옵션을 추가하지 않으면 어플리케이션이 실행되지 않습니다.**</u>

<u>**아래 xxx로 표기된 키 값은 요청하신 페이지 첨부문서에 명시하였습니다.**</u>

java -jar -Djasypt.encryptor.password=xxx api-0.0.1-SNAPSHOT.jar

(어플리케이션 기본 포트는 8080입니다.)

---

## 추가기능

1. Jasypt을 활용한 키 encrypt 
- git repo에 public으로 소스를 공개해야하는데, OPEN API 키 정보가 그대로 노출되는 문제가 있어 jasypt을 사용하여 키를 암호화 하였습니다. 
- 때문에 jar 파일을 정상적으로 실행하려면 VM 옵션으로 encrypt 키 정보를 제공해야 합니다.
- repo를 clone하고, IntelliJ 등 IDE에서 어플리케이션 구동 및 테스트 시에는 api 모듈 yml에 jasypt config를 명시 후 진행하시는 것이 수월합니다.

2. Spring Rest Doc
- API 스펙 문서를 제공하기 위해 Spring Rest Doc을 사용하였습니다.
- jar를 실행시킨 뒤 아래 URL로 각 API의 스펙 확인하실 수 있습니다.
- 블로그 검색 API : http://localhost:8080/blog.html 
- 인기검색어 TOP 10 API : http://localhost:8080/keyword.html 

---

## 왜 이딴식으로 짠거야?!!
1. 블로그 검색 서비스에 집중하였습니다.
- 블로그 검색 서비스를 안정적으로 제공하는 것에 집중하였습니다.
- 싱글 인스턴스 실행을 가정했을 때, 하나의 스레드로 OPEN API 호출 및 검색어 DB 저장을 순차적으로 진행하는 것은 장애 발생 및 대규모 트래픽 발생시 안정적인 서비스 제공을 어렵게한다고 판단, Spring webflux를 활용하여 비동기로 적은 스레드로 많은 트래픽을 처리 가능하게 구현하였으며, 검색어를 저장하는 기능은 Spring event를 비동기로 처리하도록 하여 블로그 검색과 검색어 저장 기능의 결합도를 낮췄습니다.  
2. OPEN API가 추가될 것을 고려하여, 확장성 있게 구현하였습니다.
- 변하는 것과 변하지 않는 것을 구분하라 디자인 패턴 원칙에 의거하여 Bean 주입으로 OPEN API를 추가할 수 있도록 구현하였습니다.
3. 인기검색어가 추후 연도별, 월별, 일별, 시간별로 제공을 요청할 수 있기 때문에 검색어 History를 저장하고 통계성 쿼리로 제공하는 방식을 선택하였습니다.
