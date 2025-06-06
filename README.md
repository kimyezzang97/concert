## 콘서트 예약 서비스

- **대기열 시스템**을 구축하고, 예약 서비스는 대기열에서 작업이 가능한 유저만 수행합니다.
- 사용자는 좌석 예약시 미리 충전한 잔액을 이용합니다. 
- 좌석 예약 요청시에, 결제가 이루어지지 않더라도 일정 시간동안 다른 유저가 해당좌석에 접근 할 수 없습니다.
___
### 기능적 요구사항
1. 유저 대기열 토큰 기능
- 서비스를 이용할 토큰을 발급받는 API를 구현합니다.
- 토큰은 유저의 UUID와 해당 유저의 대기열을 관리할 수 있는 정보 (예약 날짜, 좌석번호, 대기 순서 or 잔여시간 등)를 포함합니다.
- 이후 모든 API는 위 토큰을 이용해 대기열 검증을 통과 합니다.
- [기본적 폴링 방식 가정]
    - 대기열 토큰 발급 API
    - 대기번호 조회 API

2. 예약 가능 날짜 / 좌석 조회 API
- 예약가능한 날짜와 해당 날짜의 좌석을 조회하는 API 를 **각각** 구현합니다.
- 예약 가능한 날짜 목록을 조회합니다.
- 날짜 정보를 입력받아 예약가능한 좌석정보를 조회합니다.
  (좌석 정보는 1 ~ 50 까지의 좌석번호로 관리합니다.)

3. 좌석 예약 요청 API
- 날짜와 좌석 정보를 입력받아 좌석을 예약 처리하는 API 를 구현합니다.
- 좌석 예약과 동시에 해당 좌석은 그 유저에게 약 5분간 임시 배정됩니다. (시간은 변경 가능)
- 만약 배정 시간 내에 결제가 완료되지 않는다면 좌석에 대한 임시 배정은 해제됩니다.
- 누군가에게 점유된 동안에는 해당 좌석은 다른 사용자가 예약할 수 없습니다.

4. 잔액 충전 / 조회 API
- 결제에 사용될 금액을 API 를 통해 충전하는 API 를 구현합니다.
- 사용자 식별자 및 충전할 금액을 받아 잔액을 충전합니다.
- 사용자 식별자를 통해 해당 사용자의 잔액을 조회합니다.

5. 결제 API
- 결제 처리하고 결제 내역을 생성하는 API 를 구현합니다.
- 결제가 완료되면 해당 좌석의 소유권을 유저에게 배정하고 대기열 토큰을 만료시킵니다.

___
## Docs
- [📝 마일스톤](https://github.com/users/kimyezzang97/projects/2/views/1?groupedBy%5BcolumnId%5D=Milestone)
- [📌 ERD](https://github.com/kimyezzang97/hhp-concert/blob/WEEK04_REAL/docs/erd/ERD.md)
- [▶ 시퀀스](https://github.com/kimyezzang97/hhp-concert/tree/STEP03/docs/sequence)
- [📈 동시성 이슈 보고서](https://github.com/kimyezzang97/concert/blob/WEEK05/docs/report/concurrency_report.md)

---

## Getting Started

### Prerequisites

#### Running Docker Containers

`local` profile 로 실행하기 위하여 인프라가 설정되어 있는 Docker 컨테이너를 실행해주셔야 합니다.

```bash
docker-compose up -d
```
```bash
docker compose stop concert-service
```
테이블 생성 및 더미데이터 insert를 할 경우 application.yml의 
spring - sql - init - mode 를 always로 바꿔주세요

---
### 테스트 도커 컨테이너 실행
1. Gradle - clean
2. Gradle - bootJar
3. 프로젝트 최상단 명령어 실행 및 도커 컴포즈 실행
4. test 컨테이너 cpu 및 메모리 제한 (aws 프리티어 절반 성능)
5. test 컨테이너 제한된 CPU 확인 500000000 : 0.5 CPU
6. test 컨테이너 제한된 mem 확인
```bash
docker build --no cache -t concert-service:latest .
```
```bash
docker-compose up -d
```
```bash
docker-compose -f docker-compose.k6.yml -p test up -d
```
```bash
docker update --memory=1024m --memory-swap=1024m --cpus=1 concert-service
```
```bash
docker inspect concert-service --format '{{.HostConfig.NanoCpus}}'
```
```bash
docker stats concert-service
```

