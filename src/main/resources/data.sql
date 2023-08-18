/*
    CLOSETALK 개발용 초기 데이터

======================================================================
   구분  | 작성자 | 작성내용                                 |  작성일
----------------------------------------------------------------------
 최초작성 | 김민정 | 사용 방법 및 USER 테이블 초기 데이터 작성    | 23.08.18


* 사용방법
    : SQL을 복사하여 직접 DB에 삽입하거나 YAML 설정을 마친 후 프로젝트 실행

* YAML 설정하기
    1. YAML에 아래 설정을 추가한다.
----------------------------------
spring:
  jpa:
    defer-datasource-initialization: true
  sql:
    init:
      mode: always
----------------------------------
    2. YAML에서 DDL-AUTO 설정을 'CREATE' 또는 'CREATE-DROP'으로
    3. data.sql에서 로컬에 ENTITY가 존재하지 않는 테이블의 INSERT문은 주석처리 한다.
    4. 로컬에서 프로젝트 실행을 하고 기존에 사용하던 DDL-AUTO설정이 'UPDATE'였다면 다시 'UPDATE'로 변경하고
        위에 추가했던 설정은 주석처리한다.


*/


-- INSERT USERS( login_id = 암호화 되지 않은 password 입니다.)
INSERT INTO users (id, login_id, password,  nickname, email, profile_image_url, created_at)
VALUES (1,'user01', '$2a$10$683FGWMTZkLKSfjRAstivenYNj9Jrz5N7UTLxLDLfRQ54a1bILHuG', 'userNo.1', 'user01@closetalk.com', '/src/main/resources/static/images/profile/default_profile.png', '2023-08-17 17:54:33');
INSERT INTO users (id, login_id, password,  nickname, email, profile_image_url, created_at)
VALUES (2,'user02', '$2a$10$fogJdUbb478vgoFMw6SiheyB.tILLROmEBCE5qYo7KvTfigix56dq', 'NextUser', 'user02@closetalk.com', '/src/main/resources/static/images/profile/default_profile.png', '2023-08-17 17:13:44');
INSERT INTO users (id, login_id, password,  nickname, email, profile_image_url, created_at)
VALUES (3,'user03', '$2a$10$vJIDksp34bWZ7KzCbKp73esekbDK0LjUPWnrdMDCSXSEyd29.ffBK', '블랙맘바를삼켜', 'user03@closetalk.com', '/src/main/resources/static/images/profile/default_profile.png', '2023-08-17 22:10:06');
INSERT INTO users (id, login_id, password,  nickname, email, profile_image_url, created_at)
VALUES (4,'user04', '$2a$10$gg3hLcEPfC8aWWLnzbrWb.3k0dURlZDsJ8ogj.B0oU8RVXTzDPeRG', '힘내어떡해이겨내', 'user04@closetalk.com', '/src/main/resources/static/images/profile/default_profile.png', '2023-08-18 00:11:03');
INSERT INTO users (id, login_id, password,  nickname, email, profile_image_url, created_at)
VALUES (5,'user05', '$2a$10$vyqR/N0.Ph.0A.Jvn5Kqv.3HcRbjHamlKL4KRRxdlBhazPZ/2I6Q6', '우주최강햄스터', 'user05@closetalk.com', '/src/main/resources/static/images/profile/default_profile.png', '2023-08-18 03:11:22');


-- INSERT CLOSET
INSERT INTO closet (closet_name, is_hidden) VALUES ('My closet 1', false);
INSERT INTO closet (closet_name, is_hidden) VALUES ('아우터 모음', true);

-- INSERT CLOSET_ITEM
INSERT INTO closet_item (brand, category, item_image_url, item_name, price, description, created_at, closet_id)
VALUES ('나이키', 'TOP', '/nike1.png', '나이키 반팔', 29800, '운동용', NOW(), (SELECT id FROM closet LIMIT 1 OFFSET 0));
INSERT INTO closet_item (brand, category, item_image_url, item_name, price, description, created_at, modified_at, closet_id)
VALUES ('아디다스', 'OUTER', '/adi_ter1.png', '아디다스 패딩', 324207, '생존용 롱패딩 구비', '2023-08-15 13:24:12', NOW(), (SELECT id FROM closet LIMIT 1 OFFSET 1));
INSERT INTO closet_item (brand, category, item_image_url, item_name, price, description, created_at, modified_at, closet_id)
VALUES ('보세', 'OUTER', '/dlalwl2_zz.png', '흑청자켓', 77000, '', '2023-08-16 11:23:32', NOW(), (SELECT id FROM closet LIMIT 1 OFFSET 1));

-- INSERT OOTD_ARTICLE
-- INSERT OOTD_COMMENT
-- INSERT OOTD_LIKE..?
-- INSERT COUMMUNITY_ARTICLE
-- INSERT COMMUNITY_COMMENT
-- INSERT COMMUNITY_LIKE
-- INSERT COMMUNITY_CLOSET_ITEM
-- INSERT ISSUE