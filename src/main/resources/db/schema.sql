CREATE TABLE IF NOT EXISTS member(
   member_id bigint auto_increment primary key comment '회원 ID',
   member_name varchar(50) not null comment '회원 이름',
   point bigint not null comment '포인트 잔액',
   created_at datetime not null default CURRENT_TIMESTAMP comment '생성 날짜',
   updated_at datetime not null default CURRENT_TIMESTAMP comment '수정 날짜',
    version     bigint                             null,
   INDEX idx_member_point (point), -- 포인트 컬럼에 인덱스 추가
   INDEX idx_member_updated_at (updated_at) -- 업데이트 시각 컬럼에 인덱스 추가
);

CREATE TABLE IF NOT EXISTS concert(
    concert_id bigint auto_increment primary key comment '콘서트 ID',
    concert_name varchar(50) not null comment '콘서트 이름',
    created_at datetime not null default CURRENT_TIMESTAMP comment '생성 날짜',
    updated_at datetime not null default CURRENT_TIMESTAMP comment '수정 날짜'
);

CREATE TABLE IF NOT EXISTS schedule(
    schedule_id bigint auto_increment primary key comment '스케줄 ID',
    concert_id bigint not null comment '콘서트 ID',
    schedule_at datetime not null default CURRENT_TIMESTAMP comment '스케줄 날짜',
    created_at datetime not null default CURRENT_TIMESTAMP comment '생성 날짜',
    updated_at datetime not null default CURRENT_TIMESTAMP comment '수정 날짜',
    INDEX idx_schedule_concert_id (concert_id), -- 콘서트 ID로 조회할 때 유용
    INDEX idx_schedule_schedule_at (schedule_at) -- 스케줄 날짜로 조회할 때 유용
);

CREATE TABLE IF NOT EXISTS seat(
    seat_id bigint auto_increment primary key comment '좌석 ID',
    schedule_id bigint not null comment '스케줄 ID',
    seat_number bigint not null comment '좌석 번호(1~50)',
    seat_price bigint not null comment '좌석 가격',
    seat_status boolean not null comment '좌석 상태 true : 가능, false : 안 됨',
    created_at datetime not null default CURRENT_TIMESTAMP comment '생성 날짜',
    updated_at datetime not null default CURRENT_TIMESTAMP comment '수정 날짜',
    version     bigint                             null,
    INDEX idx_seat_schedule_id (schedule_id), -- 스케줄별 좌석 조회 시 유용
    INDEX idx_seat_number (seat_number), -- 좌석 번호별 조회 시 유용
    INDEX idx_seat_seat_status (seat_status) -- 좌석 상태별 조회 시 유용
);

CREATE TABLE IF NOT EXISTS queue(
    queue_id bigint auto_increment primary key comment '대기열 ID',
    member_id bigint not null comment '회원 ID',
    token varchar(250) not null comment '회원 토큰',
    queue_status varchar(50) not null comment '대기열 상태 (WAIT/PLAY/EXPIRE/CANCEL)',
    created_at datetime not null default CURRENT_TIMESTAMP comment '생성 날짜',
    updated_at datetime not null default CURRENT_TIMESTAMP comment '수정 날짜',
    expired_at datetime comment '대기열 만료 날짜',
    INDEX idx_queue_member_id (member_id), -- 회원 ID로 대기열 조회 시 유용
    INDEX idx_queue_status (queue_status), -- 대기열 상태별 조회 시 유용
    INDEX idx_queue_expired_at (expired_at) -- 대기열 만료 날짜로 조회 시 유용
);

CREATE TABLE IF NOT EXISTS reservation(
    reservation_id bigint auto_increment primary key comment '예약 ID',
    member_id bigint not null comment '회원 ID',
    seat_id bigint not null comment '좌석 ID',
    reservation_status varchar(50) not null comment '예약 상태 (EMPTY/TEMP/RESERVED/CANCELLED)',
    created_at datetime not null default CURRENT_TIMESTAMP comment '생성 날짜',
    updated_at datetime not null default CURRENT_TIMESTAMP comment '수정 날짜',
    expired_at datetime comment '예약 만료 날짜',
    INDEX idx_reservation_member_id (member_id), -- 회원 ID로 예약 내역 조회 시 유용
    INDEX idx_reservation_seat_id (seat_id), -- 좌석 ID로 예약 내역 조회 시 유용
    INDEX idx_reservation_status (reservation_status), -- 예약 상태로 조회 시 유용
    INDEX idx_reservation_expired_at (expired_at) -- 예약 만료 날짜로 조회 시 유용
);

CREATE TABLE IF NOT EXISTS payment(
  payment_id bigint auto_increment primary key comment '결제 ID',
  reservation_id bigint not null comment '예약 ID',
  member_id bigint not null comment '회원 ID',
  payment_price bigint not null comment '결제 가격',
  created_at datetime not null default CURRENT_TIMESTAMP comment '생성 날짜',
  updated_at datetime not null default CURRENT_TIMESTAMP comment '수정 날짜',
  INDEX idx_payment_member_id (member_id), -- 회원 ID로 결제 내역 조회 시 유용
  INDEX idx_payment_reservation_id (reservation_id), -- 예약 ID로 결제 내역 조회 시 유용
  INDEX idx_payment_created_at (created_at) -- 결제 생성 날짜로 조회 시 유용
);
