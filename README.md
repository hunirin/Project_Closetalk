# CLOSETALK
<img src="docs/images/closetalk.png" alt=""/>

**패션 스타일을 간단하게 커뮤니티 하나로, 패션에 대해 함께 나눌 수 있는 공간**

<br><br>

## 프로젝트 정보
|    분류     |                                              내용                                               |
|:---------:|:---------------------------------------------------------------------------------------------:|
|  **주제**   |                              자신만의 패션 스타일이나 패션 정보를 공유하는 커뮤니티 플랫폼                               |
| **제작 기간** |                                    2023.08.13 ~ 2023.09.14                                    |
| **배포 주소** | [CLOSETALK LINK](http://ec2-3-34-142-207.ap-northeast-2.compute.amazonaws.com:8080/loginPage) |

<br>

### 기획 배경
- 패션 트렌드의 빠른 변화로 인한 스타일링의 어려움
- 빠르게 패션 정보를 얻을 수 있는 공간 필요함
- 패션의 개인화에 따라 다양한 스타일을 공유하고 싶어함

### 기대 효과
- 개개인의 패션 스타일 발전
- 가상 옷장을 통한 심플한 소통
- 서로 패션을 공유하고 도와줌으로써 패션을 즐길 수 있음

<br><br>

## 팀원 소개
<table>
    <tbody>
        <tr>
            <td colspan="5" align="center"><b>김임김이나</b></td>
        </tr>
        <tr>
            <td align="center">임형택</td>
            <td align="center">김영섭</td>
            <td align="center">김민정</td>
            <td align="center">이광훈</td>
        </tr>
        <tr>
            <td align="center"><a href="https://github.com/Oh3gwnn">@Oh3gwnn</a></td>
            <td align="center"><a href="https://github.com/youngseobkim">@youngseobkim</a></td>
            <td align="center"><a href="https://github.com/bluelily555">@bluelily555</a></td>
            <td align="center"><a href="https://github.com/hunirin">@hunirin</a></td>
        </tr>
    </tbody>
</table>

<br><br>

## 기술 스택
|   분류   |                                                                                                                                                                                                                          기술 스택                                                                                                                                                                                                                          |
|:------:|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|
| 개발 언어  | ![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white) ![JavaScript](https://img.shields.io/badge/javascript-%23323330.svg?style=for-the-badge&logo=javascript&logoColor=%23F7DF1E) ![HTML5](https://img.shields.io/badge/html5-%23E34F26.svg?style=for-the-badge&logo=html5&logoColor=white) ![CSS3](https://img.shields.io/badge/css3-%231572B6.svg?style=for-the-badge&logo=css3&logoColor=white) |
| 프레임 워크 |                                                   ![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white) <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> <img src="https://img.shields.io/badge/springsecurity-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white">                                                    |
| 라이브러리  |      ![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)                                                                                                                                                                                                                             ![Bootstrap](https://img.shields.io/badge/bootstrap-%238511FA.svg?style=for-the-badge&logo=bootstrap&logoColor=white)      |
| 데이터베이스 |                                                                                                                   ![MariaDB](https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=mariadb&logoColor=white) ![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white)                                                                                                                    |
|   배포   |                                                                                                                 ![AWS](https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white) ![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)                                                                                                                 |
|  etc   |        ![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white) ![Notion](https://img.shields.io/badge/Notion-%23000000.svg?style=for-the-badge&logo=notion&logoColor=white) ![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white) ![Jira](https://img.shields.io/badge/jira-%230A0FFF.svg?style=for-the-badge&logo=jira&logoColor=white)        |
<br>


## Flow Chart
<img src="./docs/images/closetalk_flow_chart.png" alt=""/>

## ERD
<img src="./docs/images/closetalk_erd.png" alt=""/>

### 요구사항 명세서

[**[PDF]** CLOSETALK 요구사항 명세서](https://drive.google.com/file/d/18u3Lt0JIaxdUg51ZdSHZO3DF7r6RET4U/view?usp=sharing)

<br><br>
 

## 주요 기능

<details><summary> 🔸 <b>회원가입 및 로그인</b></summary>

        🔹 이메일 인증
        🔹 소셜 로그인
</details>

<details><summary> 🔸 <b>마이페이지</b></summary>

        🔹 회원정보 - 수정/삭제
        🔹 DM - 전송/조회/삭제
        🔹 나의 OOTD - 내 글 조회
</details>

<details><summary> 🔸 <b>CLOSET</b></summary>

        🔹 옷장 - 추가/수정/삭제
        🔹 아이템 등록
        🔹 아이템 조회/수정/삭제
</details>

<details><summary> 🔸 <b>COMMUNITY</b></summary>

        🔹 게시글 목록 조회/검색/작성/수정/삭제/좋아요
        🔹 Closetalk - 옷장 연동 게시물
        🔹 Smalltalk - 일반 게시물 
        🔹 댓글 작성, 대댓글 작성
</details>

<details><summary> 🔸 <b>OOTD</b></summary>

        🔹 게시글 목록 조회 - 무한스크롤 방식
        🔹 상단 배너 - 이슈/매거진 조회 스와이프형식
        🔹 게시글 조회/작성/수정/삭제/좋아요
        🔹 댓글 작성, 대댓글 작성
</details>

<details><summary> 🔸 <b>ISSUE</b></summary>

        🔹 게시글 목록 조회
        🔹 게시글 조회/작성/수정/삭제/좋아요
</details>

<details><summary> 🔸 <b>DIRECT MESSAGE</b></summary>

        🔹 DM 목록 조회 및 생성
        🔹 DM 조회 및 보내기
</details>
<br><br>

## 화면 구성

<table>
    <tbody>
        <tr>
            <td colspan="5" align="center"><b>Mobile</b></td>
        </tr>
        <tr>
            <td align="center"><b>LOGIN</b></td>
            <td align="center"><b>OOTD</b></td>
            <td align="center"><b>CLOSET</b></td>
        </tr>
        <tr>
            <td align="center"><b><img src="./docs/images/mobile-login.jpg" alt="" width="666" height="460"></b></td>
            <td align="center"><b><img src="./docs/images/mobile-ootd.jpg" alt="" width="666" height="460"></b></td>
            <td align="center"><b><img src="./docs/images/mobile-closet.jpg" alt="" width="666" height="460"></b></td>
        </tr>
    </tbody>
</table>

<br>

<table>
    <tbody>
        <tr>
            <td colspan="5" align="center"><b>PC</b></td>
        </tr>
        <tr>
            <td align="center"><b>COMMUNITY</b></td>
            <td align="center"><b>COMMUNITY : Closetalk details</b></td>
        </tr>
        <tr>
            <td align="center"><b><img src="./docs/images/pc-community.png" alt=""></b></td>
            <td align="center"><b><img src="./docs/images/pc-community-closetalk.png" alt=""></b></td>
        </tr>
        <tr>
            <td align="center"><b>COMMUNITY : Smalltalk details</b></td>
            <td align="center"><b>OOTD details</b></td>
        </tr>
        <tr>
            <td align="center"><b><img src="./docs/images/pc-community-smalltalk.png" alt=""></b></td>
            <td align="center"><b><img src="./docs/images/pc-ootd-detail.png" alt=""></b></td>
        </tr>
        <tr>
            <td align="center"><b>ISSUE</b></td>
        </tr>
        <tr>
            <td align="center"><b><img src="./docs/images/pc-issue.png" alt=""></b></td>
        </tr>
    </tbody>
</table>

<br><br>

## REST API
