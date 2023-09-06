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
VALUES (1,'user01', '$2a$10$683FGWMTZkLKSfjRAstivenYNj9Jrz5N7UTLxLDLfRQ54a1bILHuG', 'userNo.1', 'user01@closetalk.com', '/static/images/profile/user01.jpg', '2023-08-17 17:54:33');
INSERT INTO users (id, login_id, password,  nickname, email, profile_image_url, created_at)
VALUES (2,'user02', '$2a$10$fogJdUbb478vgoFMw6SiheyB.tILLROmEBCE5qYo7KvTfigix56dq', 'NextUser', 'user02@closetalk.com', '/static/images/profile/user02.jpg', '2023-08-17 17:13:44');
INSERT INTO users (id, login_id, password,  nickname, email, profile_image_url, created_at)
VALUES (3,'user03', '$2a$10$vJIDksp34bWZ7KzCbKp73esekbDK0LjUPWnrdMDCSXSEyd29.ffBK', '블랙맘바를삼켜', 'user03@closetalk.com', '/static/images/profile/default_profile.png', '2023-08-17 22:10:06');
INSERT INTO users (id, login_id, password,  nickname, email, profile_image_url, created_at)
VALUES (4,'user04', '$2a$10$gg3hLcEPfC8aWWLnzbrWb.3k0dURlZDsJ8ogj.B0oU8RVXTzDPeRG', '힘내어떡해이겨내', 'user04@closetalk.com', '/static/images/profile/default_profile.png', '2023-08-18 00:11:03');
INSERT INTO users (id, login_id, password,  nickname, email, profile_image_url, created_at)
VALUES (5,'user05', '$2a$10$vyqR/N0.Ph.0A.Jvn5Kqv.3HcRbjHamlKL4KRRxdlBhazPZ/2I6Q6', '우주최강햄스터', 'user05@closetalk.com', '/static/images/profile/default_profile.png', '2023-08-18 03:11:22');

-- INSERT CLOSET
INSERT INTO closet (closet_name, is_hidden, users_id) VALUES ('My closet 1', false, (SELECT id FROM users LIMIT 1 OFFSET 0));
INSERT INTO closet (closet_name, is_hidden, users_id) VALUES ('아우터 모음', false, (SELECT id FROM users LIMIT 1 OFFSET 0));

-- INSERT CLOSET_ITEM
INSERT INTO closet_item (brand, category, item_image_url, item_name, price, description, created_at, closet_id)
VALUES ('나이키', 'TOP', '/nike1.png', '나이키 반팔', 29800, '운동용', NOW(), (SELECT id FROM closet LIMIT 1 OFFSET 0));
INSERT INTO closet_item (brand, category, item_image_url, item_name, price, description, created_at, modified_at, closet_id)
VALUES ('아디다스', 'OUTER', '/adi_ter1.png', '아디다스 패딩', 324207, '생존용 롱패딩 구비', '2023-08-15 13:24:12', NOW(), (SELECT id FROM closet LIMIT 1 OFFSET 1));
INSERT INTO closet_item (brand, category, item_image_url, item_name, price, description, created_at, modified_at, closet_id)
VALUES ('보세', 'OUTER', '/dlalwl2_zz.png', '흑청자켓', 77000, '', '2023-08-16 11:23:32', NOW(), (SELECT id FROM closet LIMIT 1 OFFSET 1));

# -- INSERT OOTD_ARTICLE
# insert into ootd_article (id, user_id, content, hashtag, created_at, thumbnail) VALUES (1, 1, '첫번째 글', '#ootd', '2023-08-16 11:23:32', '/static/images/ootd/1/1.jpg');
# insert into ootd_article (id, content, thumbnail, user_id) VALUES (2, '두번째 글', '/static/images/ootd/2/2.jpg', 1);
# insert into ootd_article (id, content, thumbnail, user_id) VALUES (3, '세번째 글', '/static/images/ootd/3/3.jpg', 1);
# insert into ootd_article (id, content, thumbnail, user_id) VALUES (4, '네번째 글', '/static/images/ootd/4/4.jpg', 2);
# insert into ootd_article (id, content, thumbnail, user_id) VALUES (5, '다섯번째 글', '/static/images/ootd/5/5.jpg', 5);
# insert into ootd_article (id, content, thumbnail, user_id) VALUES (6, '여섯번째 글', '/static/images/ootd/6/6.jpg', 4);
# insert into ootd_article (id, content, thumbnail, user_id) VALUES (7, '일곱번째 글', '/static/images/ootd/7/7.jpg', 4);
# insert into ootd_article (id, content, thumbnail, user_id) VALUES (8, '여덟번째 글', '/static/images/ootd/8/8.jpg', 5);
# insert into ootd_article (id, content, thumbnail, user_id) VALUES (9, '아홉번째 글', '/static/images/ootd/9/9.jpg', 3);
# insert into ootd_article (id, content, thumbnail, user_id) VALUES (10, '열번째 글', '/static/images/ootd/10/10.jpg', 2);
# insert into ootd_article (id, content, thumbnail, user_id) VALUES (11, '열한번째 글', '/static/images/ootd/11/11.jpg', 2);
# insert into ootd_article (id, content, thumbnail, user_id) VALUES (12, '열두번째 글', '/static/images/ootd/12/12.jpg', 4);
# insert into ootd_article (id, content, thumbnail, user_id) VALUES (13, 'test', '/static/images/ootd/13/13.jpg',  5);
# insert into ootd_article (id, content, thumbnail, user_id) VALUES (14, 'test', '/static/images/ootd/14/14.jpg', 5);
# insert into ootd_article (id, content, thumbnail, user_id) VALUES (15, 'test 글', '/static/images/ootd/15/15.jpg', 5);
# insert into ootd_article (id, content, thumbnail, user_id) VALUES (16, 'test 글', '/static/images/ootd/16/16.jpg', 5);
# insert into ootd_article (id, content, thumbnail, user_id) VALUES (17, 'test 글', '/static/images/ootd/17/17.jpg', 2);
# insert into ootd_article (id, content, thumbnail, user_id) VALUES (18, 'test 글', '/static/images/ootd/18/18.jpg', 2);
# insert into ootd_article (id, content, thumbnail, user_id) VALUES (19, 'test 글', '/static/images/ootd/19/19.jpg', 3);
# insert into ootd_article (id, content, thumbnail, user_id) VALUES (20, 'test 글', '/static/images/ootd/20/20.jpg', 3);
# insert into ootd_article (id, content, thumbnail, user_id) VALUES (21, 'test 글', '/static/images/ootd/21/21.jpg', 3);
# insert into ootd_article (id, content, thumbnail, user_id) VALUES (22, 'test 글', '/static/images/ootd/22/22.jpg', 1);
# insert into ootd_article (id, content, thumbnail, user_id) VALUES (23, 'test 글', '/static/images/ootd/23/23.jpg', 1);
# insert into ootd_article (id, content, thumbnail, user_id) VALUES (24, 'test 글', '/static/images/ootd/24/24.jpg', 4);
# insert into ootd_article (id, content, thumbnail, user_id) VALUES (25, 'test 글', '/static/images/ootd/25/25.jpg', 4);
# insert into ootd_article (id, content, thumbnail, user_id) VALUES (26, 'test 글', '/static/images/ootd/26/26.jpg', 5);
# insert into ootd_article (id, content, thumbnail, user_id) VALUES (27, 'test 글', '/static/images/ootd/27/27.jpg', 5);
# insert into ootd_article (id, content, thumbnail, user_id) VALUES (28, 'test 글', '/static/images/ootd/28/28.jpg', 5);
# insert into ootd_article (id, content, thumbnail, user_id) VALUES (29, 'test 글', '/static/images/ootd/29/29.jpg', 4);
# insert into ootd_article (id, content, thumbnail, user_id) VALUES (30, 'test 글', '/static/images/ootd/30/30.jpg', 2);
#
# -- INSERT OOTD_ARTICLE_IMAGES
# insert into ootd_article_images (id, ootd_article_id, image_url) values (1, 1, '/static/images/ootd/1/1.jpg');
# insert into ootd_article_images (id, ootd_article_id, image_url) values (2, 1, '/static/images/ootd/1/1-2.jpg');
# insert into ootd_article_images (id, ootd_article_id, image_url) values (3, 2, '/static/images/ootd/2/2.jpg');
# insert into ootd_article_images (id, ootd_article_id, image_url) values (4, 2, '/static/images/ootd/2/2-2.jpg');
# insert into ootd_article_images (id, ootd_article_id, image_url) values (5, 2, '/static/images/ootd/2/2-3.jpg');
# insert into ootd_article_images (id, ootd_article_id, image_url) values (6, 3, '/static/images/ootd/3/3.jpg');
# insert into ootd_article_images (id, ootd_article_id, image_url) values (7, 4, '/static/images/ootd/4/4.jpg');
# insert into ootd_article_images (id, ootd_article_id, image_url) values (8, 5, '/static/images/ootd/5/5.jpg');
# insert into ootd_article_images (id, ootd_article_id, image_url) values (9, 6, '/static/images/ootd/6/6.jpg');
# insert into ootd_article_images (id, ootd_article_id, image_url) values (10, 7, '/static/images/ootd/7/7.jpg');
# insert into ootd_article_images (id, ootd_article_id, image_url) values (11, 8, '/static/images/ootd/8/8.jpg');
# insert into ootd_article_images (id, ootd_article_id, image_url) values (12, 9, '/static/images/ootd/9/9.jpg');
# insert into ootd_article_images (id, ootd_article_id, image_url) values (13, 10, '/static/images/ootd/10/10.jpg');
# insert into ootd_article_images (id, ootd_article_id, image_url) values (14, 11, '/static/images/ootd/11/11.jpg');
# insert into ootd_article_images (id, ootd_article_id, image_url) values (15, 12, '/static/images/ootd/12/12.jpg');
# insert into ootd_article_images (id, ootd_article_id, image_url) values (16, 13, '/static/images/ootd/13/13.jpg');
# insert into ootd_article_images (id, ootd_article_id, image_url) values (17, 14, '/static/images/ootd/14/14.jpg');
# insert into ootd_article_images (id, ootd_article_id, image_url) values (18, 15, '/static/images/ootd/15/15.jpg');
# insert into ootd_article_images (id, ootd_article_id, image_url) values (19, 16, '/static/images/ootd/16/16.jpg');
# insert into ootd_article_images (id, ootd_article_id, image_url) values (20, 17, '/static/images/ootd/17/17.jpg');
# insert into ootd_article_images (id, ootd_article_id, image_url) values (21, 18, '/static/images/ootd/18/18.jpg');
# insert into ootd_article_images (id, ootd_article_id, image_url) values (22, 19, '/static/images/ootd/19/19.jpg');
# insert into ootd_article_images (id, ootd_article_id, image_url) values (23, 20, '/static/images/ootd/20/20.jpg');
# insert into ootd_article_images (id, ootd_article_id, image_url) values (24, 21, '/static/images/ootd/21/21.jpg');
# insert into ootd_article_images (id, ootd_article_id, image_url) values (25, 22, '/static/images/ootd/22/22.jpg');
# insert into ootd_article_images (id, ootd_article_id, image_url) values (26, 23, '/static/images/ootd/23/23.jpg');
# insert into ootd_article_images (id, ootd_article_id, image_url) values (27, 24, '/static/images/ootd/24/24.jpg');
# insert into ootd_article_images (id, ootd_article_id, image_url) values (28, 25, '/static/images/ootd/25/25.jpg');
# insert into ootd_article_images (id, ootd_article_id, image_url) values (29, 26, '/static/images/ootd/26/26.jpg');
# insert into ootd_article_images (id, ootd_article_id, image_url) values (30, 27, '/static/images/ootd/27/27.jpg');
# insert into ootd_article_images (id, ootd_article_id, image_url) values (31, 28, '/static/images/ootd/28/28.jpg');
# insert into ootd_article_images (id, ootd_article_id, image_url) values (32, 29, '/static/images/ootd/29/29.jpg');
# insert into ootd_article_images (id, ootd_article_id, image_url) values (33, 30, '/static/images/ootd/30/30.jpg');
#
# -- INSERT OOTD_COMMENT
# insert into ootd_comment (id, ootd_article_id, user_id, content, created_at) values (1, 1, 2, 'comment testing', '2023-08-30 11:23:32');
# insert into ootd_comment (id, ootd_article_id, user_id, content, created_at) values (2, 1, 2, 'comment testing', '2023-08-30 11:23:32');
#
# -- INSERT OOTD_COMMENT_REPLY
# insert into ootd_comment (id, ootd_article_id, user_id, ootd_comment_id, content, created_at) values (3, 1, 1, 1, 'reply testing', '2023-08-30 11:23:32');
# -- INSERT OOTD_LIKE..?

# -- INSERT ISSUE_ARTICLE
# insert into issue_article (id, content, thumbnail) VALUES (1, '첫번째 글', '/static/images/issue/1/1.jpg');
# insert into issue_article (id, content, thumbnail) VALUES (2, '두번째 글', '/static/images/issue/2/2.jpg');
# insert into issue_article (id, content, thumbnail) VALUES (3, '세번째 글', '/static/images/issue/3/3.jpg');
# insert into issue_article (id, content, thumbnail) VALUES (4, '네번째 글', '/static/images/issue/4/1.jpg');
# insert into issue_article (id, content, thumbnail) VALUES (5, '다섯번째 글', '/static/images/issue/5/2.jpg');
# insert into issue_article (id, content, thumbnail) VALUES (6, '여섯번째 글', '/static/images/issue/6/3.jpg');
#
# -- INSERT ISSUE_ARTICLE_IMAGES
# insert into issue_article_images (id, issue_article_id, image_url) values (1, 1, '/static/images/issue/1/1.jpg');
# insert into issue_article_images (id, issue_article_id, image_url) values (2, 2, '/static/images/issue/2/2.jpg');
# insert into issue_article_images (id, issue_article_id, image_url) values (3, 3, '/static/images/issue/3/3.jpg');
# insert into issue_article_images (id, issue_article_id, image_url) values (4, 4, '/static/images/issue/4/1.jpg');
# insert into issue_article_images (id, issue_article_id, image_url) values (5, 5, '/static/images/issue/5/2.jpg');
# insert into issue_article_images (id, issue_article_id, image_url) values (6, 6, '/static/images/issue/6/3.jpg');

-- INSERT COMMUNITY_ARTICLE
insert into community_article (id, category, title, content, thumbnail, created_at, user_id, hits, like_count) values (1, 'CLOSETALK', '예시 1번', 'test', '/static/images/community/1/1', NOW(), 1, 0, 0);
insert into community_article (id, category, title, content, thumbnail, created_at, user_id, hits, like_count) values (2, 'CLOSETALK', '예시 2번', 'test', '/static/images/community/2/4', '2023-08-24', 1, 0, 0);
insert into community_article (id, category, title, content, thumbnail, created_at, user_id, hits, like_count) values (3, 'CLOSETALK', '예시 3번', 'test', '/static/images/community/3/7', '2023-08-24', 1, 0, 0);
insert into community_article (id, category, title, content, thumbnail, created_at, user_id, hits, like_count) values (4, 'SMALLTALK', '예시 4번', 'test', '/static/images/community/4/10', '2023-08-24', 1, 0, 0);
insert into community_article (id, category, title, content, thumbnail, created_at, user_id, hits, like_count) values (5, 'SMALLTALK', '예시 5번', 'test', '/static/images/community/5/13', '2023-08-24', 1, 0, 0);
#
-- INSERT COMMUNITY_ARTICLE_IMAGES
insert into community_article_images (id, community_article_id, image_url) values (1, 1, '/static/images/community/1/1.jpg');
insert into community_article_images (id, community_article_id, image_url) values (2, 1, '/static/images/community/1/2.jpg');
insert into community_article_images (id, community_article_id, image_url) values (3, 1, '/static/images/community/1/3.jpg');
insert into community_article_images (id, community_article_id, image_url) values (4, 2, '/static/images/community/2/4.png');
insert into community_article_images (id, community_article_id, image_url) values (5, 2, '/static/images/community/2/5.png');
insert into community_article_images (id, community_article_id, image_url) values (6, 2, '/static/images/community/2/6.png');
insert into community_article_images (id, community_article_id, image_url) values (7, 3, '/static/images/community/3/7.jpg');
insert into community_article_images (id, community_article_id, image_url) values (8, 3, '/static/images/community/3/8.jpg');
insert into community_article_images (id, community_article_id, image_url) values (9, 3, '/static/images/community/3/9.jpg');
insert into community_article_images (id, community_article_id, image_url) values (10, 4, '/static/images/community/4/10.png');
insert into community_article_images (id, community_article_id, image_url) values (11, 4, '/static/images/community/4/11.png');
insert into community_article_images (id, community_article_id, image_url) values (12, 4, '/static/images/community/4/12.jpg');
insert into community_article_images (id, community_article_id, image_url) values (13, 5, '/static/images/community/5/13.png');
insert into community_article_images (id, community_article_id, image_url) values (14, 5, '/static/images/community/5/14.jpg');
insert into community_article_images (id, community_article_id, image_url) values (15, 5, '/static/images/community/5/15.png');

-- INSERT COMMUNITY_COMMENT
insert into community_comment (id, content, created_at, user_id, community_article_id)
values  (1, '첫번째 게시물의 첫번째 댓글', NOW(), 1, 1);
insert into community_comment (id, content, created_at, user_id, community_article_id)
values  (2, '첫번째 게시물의 두번째 댓글', NOW(), 2, 1);
insert into community_comment (id, content, created_at, user_id, community_article_id)
values  (3, '첫번째 게시물의 세번째 댓글', NOW(), 3, 1);
insert into community_comment (id, content, created_at, user_id, community_article_id, comment_id)
values  (4, '첫번째 댓글의 대댓글 1', NOW(), 3, 1, 1);
insert into community_comment (id, content, created_at, user_id, community_article_id, comment_id)
values  (5, '첫번째 댓글의 대댓글 2', NOW(), 1, 1, 1);
insert into community_comment (id, content, created_at, modified_at, user_id, community_article_id, comment_id)
values  (6, '두번째 댓글의 수정된 대댓글 1', NOW(), NOW(), 2, 1, 2);
insert into community_comment (id, content, created_at, deleted_at, user_id, community_article_id, comment_id)
values  (7, '두번째 댓글의 대댓글 2', NOW(), NOW(), 3, 1, 2);
insert into community_comment (id, content, created_at, user_id, community_article_id, comment_id)
values  (8, '두번째 댓글의 대댓글 3', NOW(), 2, 1, 2);
insert into community_comment (id, content, created_at, deleted_at, user_id, community_article_id)
values  (9, '첫번째 게시물의 네번째 댓글', NOW(), NOW(), 3, 1);
insert into community_comment (id, content, created_at, modified_at, user_id, community_article_id)
values  (10, '첫번째 게시물의 수정된 다섯번째 댓글', NOW(), NOW(), 2, 1);

-- INSERT COMMUNITY_LIKE
-- INSERT COMMUNITY_CLOSET_ITEM