## 🧑‍💻 팀 구성원, 개인 별 역할

---
PM : 김민지 - 챌린지 생성, 수정, 삭제 구현

CTO : 변흥섭 - 소셜로그인, 댓글 구현

인프라 : 곽철민 - 소셜로그인, 해시태그 구현

개발1 : 박정훈 - 메인페이지, 챌린지 전체 조회 UI 제작

개발2 : 이도현 - 좋아요 기능 구현

## 🤔 팀 내부 회의 진행 회차 및 일자

---

- 1회차(2022.01.24) 디스코드(박정훈님 불참)
    - develop 브랜치 merge
    - Jpa Auditing 적용
- 2회차(2022.01.25) 디스코드
    - 진행 상황 공유
- 3회차(2022.01.26) 디스코드
    - 소셜로그인, 챌린지 CRUD merge

## 현재까지 개발 과정 요약 (최소 500자 이상)

---

- 곽철민(인프라/개발)
    - Spring Security와 oauth2 client 라이브러리를 활용해 소셜 로그인을 구현했습니다. 로그인 이후에 인증/인가 처리를 위해 jwt를 활용하려고 했지만 구현하는데 어려움이 있어 세션으로 로그인 유지를 처리했습니다.
    - 해시태그 생성 및 조회를 구현하였습니다. 챌린지가 생성될 때, 입력된 해시태그 값들이 저장되도록 하였습니다. 이 때, 챌린지와 해시태그는 다대다 관계이기 때문에 이를, 일대다, 다대일 관계로 풀어주었습니다. 해시태그 수정 및 삭제는 주말을 활용해 구현 할 예정입니다.
- 김민지(PM/개발)
    - 챌린지 공개, 비공개 처리를 할 때 boolean 타입으로 받아오는데 db에는 null로 저장되는 것을 확인했습니다. 이 과정에서 boolean 타입은 @Getter 어노테이션을 사용할 때 perfix가 get이 아니라 is이기 때문에 바인딩이 되지 않습니다. 그래서 이 부분을 처리하기 위해 boolean타입은 getter 메서드를 직접 작성했습니다.
    - ControllerTest에서 `Consider defining a bean of type 'org.springframework.data.redis.connection.RedisConnectionFactory' in your configuration.` 에러가 발생했다. RedisConnectionFactory @Bean 어노테이션을 인식하지 못해서 발생하는 에러였고 @MockBean 어노테이션으로 DI 해주니까 해결되었다.
- 박정훈(개발) :
- 변흥섭(CTO):
    - Spring Security와 thymeleaf,jwt,oauth2, redis 연동하는 래퍼런스가 없고 springboot3로 버전이 달라져서 방법 또한 어려웠습니다. 리프레시 토큰과 어세스 토큰을저장하고 redis를 저장하는데 어려움 겪어 로그인 세션을 redis에 저장하고 관리 하려고 합니다. 아직 docker x redis 를 쓰는 것에 미숙한 것 같습니다 주말 반납해서 열심히 익히겠습니다.
    - 댓글은 생성, 조회를 구현하였고 앞으로는 수정과 삭제를 구현할 예정이고 더불어 대댓글 기능까지 구현하고 싶어서 공부할 것 같습니다. TestCode는 아직까지도 어려워서 시간이 오래걸릴 것 같습니다. 대댓글에 QueryDSL을 사용할 것 같은데 그것도 공부해야 할 것같습니다.
- 이도현(개발)
    - Spring Rest Docs 구현 방법을 공부했고 로컬에서 문서 작성 및 기본 Controller 테스트에 성공해 해당 과정을 팀원들에게 공유했습니다. Spring Rest Docs의 도커 - CI 배포는 좀 더 찾아봐야 할 것 같아 보류 중입니다.
    - 좋아요 기능 구현을 위해 첫째, 전체 팀의 코드를 분석해보는 시간을 갖고 있습니다. 예외 전역처리, ENUM, QueryDSL에 대해 알 수 있었으며 협업을 위해 팀원의 코드 로직을 이해하는 것이 중요하다는 것을 배울 수 있었습니다. 둘째, 좋아요 기능 구현을 위해 레퍼런스를 찾고 있습니다. ajax가 필요한 거 같은데 이 부분은 더 공부해야 할 것 같습니다.

## ❓ 개발 과정에서 나왔던 질문 (최소 200자 이상)

---

- 챌린지 시작날짜와 종료날짜를 Uesr_Challenge 테이블이 아니라 Challenge 테이블에서 관리하는게 더 좋지 않을까 싶습니다.
- 인증, 인가를 처리할 때 세션을 사용하는 경우도 jwt 사용할때 와 동일하게 Authentication을 이용하면 될까요?
    - authentication.getName : oauth_id
    - authentication.getAuthorities : role
    - authentication.getDetails : RemoteIpAddress, SessionId
- Spring Rest Docs를 현재 팀 프로젝트 개발 환경 설정과 비슷하게 배포한 레퍼런스가 있다면 알 수 있을까요? 어느 부분을 더 공부해야 할지 잘 모르겠습니다.
- QueryDSL의 필요성과 원리가 궁금합니다.

## 👏 개발 결과물 공유

---

[https://gitlab.com/minjikim99/devroutine](https://gitlab.com/minjikim99/devroutine)
