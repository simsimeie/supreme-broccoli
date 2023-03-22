# supreme-broccoli

## java -version
JDK17

## Jar 파일 다운로드 경로

### [필독] Jar 파일 실행 명령어
<u>**jasypt으로 키를 암호화하여, 아래 명령어로 VM 옵션을 추가하지 않으면 어플리케이션이 실행되지 않습니다.**</u>

<u>**아래 xxx로 표기된 키 값은 요청하신 페이지 첨부문서에 명시하였습니다.**</u>

java -jar -Djasypt.encryptor.password=xxx api-0.0.1-SNAPSHOT.jar

(어플리케이션 기본 포트는 8080입니다.) 

https://drive.google.com/file/d/1sC4No__grmZBPeTiVzip8OGUxebCjq_s/view?usp=sharing

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
