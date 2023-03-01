## :tada: 쿠팡이츠 클론 코딩 프로젝트 Server [라이징 캠프 13기 테스트]
### ※ 해당 프로젝트의 템플릿에 대한 저작권은 (주)소프트스퀘어드에 있습니다
- - -
## :bulb: 기술 스택
<img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat-square&logo=Spring Boot&logoColor=green"/> <img src="https://img.shields.io/badge/-SpringBoot-green"/> <img src="https://img.shields.io/badge/-nginx-yellowgreen"/>
<img src="https://img.shields.io/badge/-MySQL-red"/>
<img src="https://img.shields.io/badge/-AWS-orange"/>
<img src="https://img.shields.io/badge/-JWT-blue"/>
<img src="https://img.shields.io/badge/-OAuth-yellow"/>

- - -
## :books: [API DOCUMENT](https://docs.google.com/spreadsheets/d/19_krKrCAip0LuEZJsWMf815BNcbxDeia/edit?usp=sharing&ouid=114720192267022828790&rtpof=true&sd=true)
- - -
## :clipboard: [API Collections](https://documenter.getpostman.com/view/25231971/2s935smgSK)
- - -
## ERD ![사진](https://user-images.githubusercontent.com/106572156/222183190-bb51c7bf-23ed-4fcd-9010-fc4530222c9e.png)
- - -
## :iphone: 시연화면 (Android)
<img src="https://user-images.githubusercontent.com/106572156/222185270-1176a163-d796-4a4e-95c2-27164093b530.PNG"  width="250" height="400"/> <img src="https://user-images.githubusercontent.com/106572156/222187116-f947800f-4c87-41c8-b840-5c33167ee664.png"  width="250" height="400"/> <img src="https://user-images.githubusercontent.com/106572156/222185250-2dfb6675-c168-4dcd-aebf-e30deb61a01a.PNG"  width="250" height="400"/> <img src="https://user-images.githubusercontent.com/106572156/222185265-be403d09-4e5c-4857-9ac0-45d92831e4e0.PNG"  width="250" height="400"/> <img src="https://user-images.githubusercontent.com/106572156/222185277-2a182cbd-d285-4de2-8df3-3d8fad2d7428.PNG"  width="250" height="400"/> <img src="https://user-images.githubusercontent.com/106572156/222187943-9309646a-a331-424e-aaac-df0fff99fb30.png"  width="250" height="400"/>

## :memo: 개발일지

### 2023.01.28 1일차

- 쿠팡이츠 기획서 작성

- DB, API 설계 하기전 필요한 데이터 목록 나열하며 의견 조율
  - 로그인, 회원가입, 메인 페이지에 해당하는 데이터
  - 2번 과정에서 Json 안에 Json을 중첩하는 기술이 필요하다 판단되어 해당 Spring 라이브러리를 찾아보고 구현 가능한지 테스트할 예정

- Github 레포지토리 내 해피, 기인 각자의 Branch 생성

### 2023.01.29 2일차

#### (Client + Server)
- 우선순위 P1인 API를 설계 할때 필요한 데이터 나열하며 의견 조율
  - 가게, 가게 상세, 메뉴 상세 ...
  - API 별 필요한 Request, Response 값 정의

#### (Server)
- 나열했던 데이터 목록을 기반으로 ERD 초안 설계
  - 유저, 가게, 주문, 리뷰 등 우선순위가 높은 테이블 위주로 구성

#### (Solo)
- (홈 화면) 골라먹는 맛집 목록 불러오기 API 개발
  - 유저의 주소에 따른 필터링 기능 추가 예정
- (홈 화면) 가게 카테고리 목록 불러오기 API 개발

### 2023.01.30 3일차

#### (Server)
- ERD 마무리 작업
- RestAPI 리스트업 초안

#### (Solo)
- (My이츠) 결제관리 페이지 불러오기 API 개발
- (홈화면 등의 하트버튼) 즐겨찾기 추가/해제 API 개발

### 2023.01.31 4일차

#### (Client + Server)
- 설계한 ERD를 바탕으로 더 필요한 데이터가 있을지 토의
- API Request, Response 값 조율

#### (Solo)
- (홈 화면) 골라먹는 맛집 목록 불러오기 API 기능 추가
  - 유저가 즐겨찾기 한 가게 인지 아닌지 (오른쪽 상단 하트) 명시
  - 유저와 가게의 위도, 경도를 기준으로 필터링 기능 추가 진행중
- 1차 피드백 전 작업 내역 검토

### 2023.02.01 5일차 (1차 피드백 D-day)

#### (Client + Server)
- 1차 피드백 내용, API 개발 진행사항 공유

#### (Server)
- 1차 피드백 내용 검토 및 반영

  - MySql 데이터 타입 관련
    - INT 형을 사용한 데이터들 중 UNSIGNED INT 를 사용할 수 있다면 변경 (PK, FK는 필수)
    - 유저 결제 정보 Table의 계좌, 카드 번호는 암호화해서 저장해야하므로 TEXT 타입으로 변경
    - VARCHAR 타입 컬럼 중 데이터를 담기 부족하다고 생각되는 것들 길이를 늘려주거나 TEXT 타입으로 변경
    - ENUM 타입은 권장되지 않으므로 VARCHAR로 변경(유저 결제 수단, 쿠폰 종류)

  - API 명세서 관련
    - API 별로 담당자를 명시
    - 현재 작업중인 API 목록 명시

  - REST API 관련
    - 유저의 정보를 필요로하는 API URI에 users/:userId 삭제
      - jwt 토큰의 payload 값으로 넘어오는 정보면 충분함

#### (Solo)
- (홈 화면) 골라먹는 맛집 목록 불러오기 API 기능 추가
  - 유저와 가게의 위도, 경도를 바탕으로 거리를 계산하여 일정 거리 이내 가게만 보이도록
  - 가게별 리뷰들의 별점 평균을 계산하여 평점 나타내기
  - 가게별로 현재 기준 사용가능한 쿠폰중 가장 할인가격이 높은 쿠폰 정보 하나 보여주기

- (홈 화면) 이츠 오리지널 가게 목록 불러오기 쿼리 작성

- (홈, 유저주소 화면) 유저의 주소 목록 불러오기 쿼리 작성

### 2023.02.02 6일차

#### (Solo)
- 1차 피드백 이후 ERD, REST API 리스트업 마무리 -> API 개발 본격 시작

- (홈 화면)에 필요한 API 관련 쿼리 작성 마무리 (SQL 쿼리 작성까지, API 반영예정)

  - 골라먹는 맛집 목록 불러오기 API
  - 이츠오리지널 목록 불러오기 API
  - 자주 주문한 맛집 목록 불러오기 API
  - 우리동네 인기맛집 목록 불러오기 API

  - 이벤트 배너 목록 불러오기 API
  - 가게 카테고리 목록 불러오기 API
  - 유저의 주소 목록 불러오기 API

- 현재까지 홈 화면 맛집 불러오기 API에 반영된 기능
  1. (공통) 유저와 가게의 위도, 경도를 바탕으로 거리를 계산하여 일정 거리 이내 가게만 불러오기
  2. (공통) 가게별 리뷰 수, 별점 평균을 계산하여 평점 나타내기
  3. (공통) 가게별 쿠폰중 가장 할인가격이 높은 쿠폰 정보 하나 보여주기 (사용기간이 만료되지 않은 쿠폰)
  4. (공통) 영업시간을 기준으로 현재 영업중인지 아닌지 나타내기
  5. (공통) 유저가 해당 가게를 즐겨찾기 했는지 아닌지 나타내기
  6. (자주 주문) 유저의 주문 수로 정렬 후 상위 n개만 불러오기
  7. (우리 동네) 유저의 동네(몇 km 이내)에서 가장 주문이 많은 상위 n개만 불러오기

### 2023.02.03 7일차

#### (Solo)
- 02.04 Merge 전 dev 환경 DB 데이터 추가 작업

- API 반영 전 쿼리 점검 및 수정

- 홈 화면 관련 API(6일차 API 목록 참고) 구현 완료
  - 골라먹는 맛집 목록... 의 API 호출 시 addressId를 넘겨주는 것이 필요하다고 판단 -> 유저의 주소 목록 불러오기 API addressId 반환하도록 수정

### 2023.02.04 8일차

#### (Server)
- API 개발 진행 상황 공유, 남은 API 역할 분담

- 현재까지 개발한 코드 Merge
  - ★ git 환경에 익숙하지 않아 어려움을 겪음
  - happy branch가 main으로 부터 시작된 것이 아니라 그런지 merge 불가
    - 1. 수작업으로 소스코드 병합 후 main branch 에 push
    - 2. main 으로부터 happy2 branch 분기 하여 새로운 작업 공간 마련

### 2023.02.05 9일차

#### (Solo)
- 가게별 리뷰 목록 불러오기 API 개발
  - Reviews 테이블에서 해당 가게의 리뷰 수, 평점을 계산
  - 리뷰는 주문에 대해 작성하도록 기획 -> 리뷰에 해당하는 주문이 어떤 메뉴들을 시켰는지 OrderLists 테이블에서 불러옴

- 오늘까지 개발한 모든 API 최종 점검 후 dev 서버에 반영하여 클라이언트 측에서 테스트 할 수 있도록 배포

### 2023.02.06 10일차

#### (Client + Server)
- 위클리 스크럼

#### (Solo)
- 비로그인(저장된 유저의 주소가 없는 경우)시 골라먹는 맛집 목록, 검색기능이 필요하여 추가 개발
  - 유저의 주소정보가 없으므로 현재 위도, 경도를 직접 넘겨서 가게들을 불러오도록

- 결제수단 관련 API 개발(추가, 조회)
  - 결제수단 추가 API는 계좌, 카드의 구분이 있고 번호를 입력해야 하므로 Validation에 신경씀
  - 존재하는 은행정보인지, 이미 등록된 결제정보 인지, 카드/계좌 번호 길이, 형식제한(정규표현식 활용) 등등

- 카카오 소셜 로그인 기능 구현
  - 카카오 로그인 했을 시 DB에 저장된 유저라면 로그인, 아니라면 신규 회원가입을 하도록 구현

### 2023.02.07 11일차

#### (Client + Server)
- 현재까지 진행상황 공유 및 API 개발 마무리, 배포 일정 논의

#### (Server)
- Prod용 DB 구축 관련 논의

#### (Solo)
- 즐겨찾기 추가/해제 (하트버튼) API 개발
  - 버튼을 누르면 즐겨찾기에 추가, 다시 누르면 해제하도록 현재 상태를 확인하여 그에 맞게 동작하도록 구현

- 내 즐겨찾기 목록 조회 API 개발
  - 즐겨찾기 한 가게들만 보여주고, 유저가 해당 가게에서 몇번 주문했는지 계산하여 나타냄
  - 자주 주문한 순으로 정렬

- 즐겨찾기 해제 API 개발
  - 해제하려는 가게의 id들을 List로 넘겨 한번에 여러가게를 즐겨찾기 해제 할 수 있도록 구현

- 유저 주소 추가, 삭제 API 개발

- MyEats 내 정보 조회 API 개발
  - 유저의 전화번호는 가운데 네자리 * 표시
  - 내 리뷰 수, 내 리뷰의 반응 수, 내 즐겨찾기 수 계산하여 나타냄

- 계획했던 모든 API 개발 완료(21개), 서버 반영 후 최종 점검

### 2023.02.08 12일차 (2차 피드백 D-day)

#### (Server)
- 2차 피드백 내용 공유 및 반영
  - REST API URI에 복수형이 아닌 것들 복수형으로 변경
  - Prod DB 구축완료 한 뒤 시간이 남는다면 챌린지 과제 도전

#### (Solo)
- Prod DB 구축
  - 필요한 사진들(가게, 메뉴 등등..) 캡쳐 후 S3를 이용하여 URL화
  - 가게, 유저 위치는 판교 주변으로 통일
  - 판교주변 가게 위도, 경도 파악하여 DB 구축
  - 가게, 메뉴, 주문 각각의 자원들간의 연관관계를 꼼꼼히 확인하며 데이터 구축

- API 코드 작성 최종점검
  - Validation 처리
  - 논리적 오류 검증

### 2023.02.09 13일차 (최종 마감 D-1)
- (Client + Server)
  - Prod 서버로 최종 테스트
  - API 보완할 점 공유 및 수정 반영
  - API 명세서 최종 점검

#### (Server)
- 최종 코드 Merge 작업
- Prod 서버 API 동작 테스트
- API 명세서 URL 수정 및 Sample Request 값 점검

#### (Solo)
- 제출용 Collection 작업, 영상 촬영

### 2023.02.10 14일차 (최종 마감 D-day)
- (Solo)
  - Postman Collection 작업 및 발표 영상 촬영
  - 발표영상, API 명세서, ERD, 노션, Postman collection 링크 최종 정리


















