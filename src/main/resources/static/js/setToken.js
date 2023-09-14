// setToken.js

function sendAuthenticatedRequest(url, method, accessToken) {
    return fetch(url, {
        method: method,
        headers: {
            'Authorization': `Bearer ${accessToken}`,
            'Content-Type': 'application/json'
        },
        credentials: 'include',
    })
        .then(response => {
            if (response.status === 200) {
                const authorizationHeader = response.headers.get('Authorization');
                if (authorizationHeader && authorizationHeader.startsWith('Bearer ')) {
                    const newAccessToken = authorizationHeader.substring('Bearer '.length);
                    if (newAccessToken) {
                        localStorage.setItem('accessToken', newAccessToken);
                        location.reload();
                    }
                } else {
                    console.log('AccessToken 만료되지 않음');
                }
            } else {
                console.error('Authentication failed');
            }
        })
        .catch(error => {
            console.error('AccessToken 갱신 실패:', error);
            alert('세션이 종료되었습니다. 다시 로그인해주세요.' + error.message);
        });
}
// 사용 예시입니다.
// html -> <script src ="/static/js/setToken.js"></script> 추가
//
// script -> document.write('<script src ="/static/js/setToken.js"></script>'); 추가
//
// 아래처럼 인증 필요한 곳에서 사용하면 됩니다. fetch도 따로 작성 안해도 돼요
// 추가로 작성할 것이 필요하면 함수 따로 가져다가 사용하시면 됩니다.
//
// sendAuthenticatedRequest('/users/test', 'POST', accessToken)
//     .then(data => {
//         // data를 사용하여 추가적인 작업을 수행
//         console.log('서버 응답 데이터:', data);
//     })
//     .catch(error => {
//         // 오류 처리
//         console.error('오류 발생:', error);
//     });