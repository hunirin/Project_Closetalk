// MyCloset.js

// 서버로부터 HTML을 요청하고 받아오는 함수
const accessToken = localStorage.getItem('accessToken');
const currentUrl = window.location.pathname;
const nickname = currentUrl.split('/').pop();
let selectedCategory = null; // 기본 카테고리 설정
let selectedCloset = null;

function fetchClosetList() {
    fetch('/closet', {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${accessToken}`
        }
    })
        .then(response => {
            if (response.status === 200) {
                return response.json();
            } else {
                throw new Error('요청 실패');
            }
        })
        .then(data => {
            const closetSelect = document.getElementById('closetSelect');
            // 1. 옷장 목록 선택 옵션
            data.forEach(closet => {
                const closetOption = document.createElement('option');
                closetOption.value = closet.closetName;
                closetOption.textContent = closet.closetName;
                closetSelect.appendChild(closetOption);
            });

            // 2. 옷장 선택 시 이벤트
            closetSelect.addEventListener('change', function() {
                selectedCloset = this.value;
                selectedCategory = null;
                fetchItemsForCloset(selectedCloset); // 선택한 옷장으로 아이템 목록 요청을 보내고 표시하는 함수 호출
                closeModal();
            });

            // 첫 번째 옵션을 기본 선택값으로 설정
            if (data.length > 0) {
                selectedCloset = data[0].closetName;
                closetSelect.value = selectedCloset;
                fetchItemsForCloset(selectedCloset);
            }
        })
        .catch(error => {
            console.error('요청 실패:', error);
        });
}

// 선택한 옷장의 아이템 목록을 서버에서 가져오는 함수
function fetchItemsForCloset(selectedCloset) {
    let requestUrl = `/closet/${nickname}/${selectedCloset}`;
    if (selectedCategory) {
        requestUrl += `?category=${selectedCategory}`;
    }

    fetch(requestUrl, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${accessToken}`
        }
    })
        .then(response => {
            if (response.status === 200) {
                return response.json();
            } else {
                throw new Error('아이템 목록을 불러오는데 실패했습니다.');
            }
        })
        .then(data => {
            displayItemsByCategory(data);
        })
        .catch(error => {
            console.error('요청 실패:', error);
        });
}

function openModal() {
    const modal = document.getElementById('itemDetailsModal');
    const close = document.getElementById('modal-close');
    modal.style.display = 'block';
    close.style.display = 'block';
}

// 모달 닫기
function closeModal() {
    const modal = document.getElementById('itemDetailsModal');
    const close = document.getElementById('modal-close');
    modal.style.display = 'none';
    close.style.display = 'none';
}

// 아이템 목록 클릭 이벤트 처리
function handleItemClick(item) {
    displayItemDetails(item);
    openModal();
}

// 선택한 아이템의 세부 정보를 표시
function displayItemDetails(item) {
    const itemDetailsContainer = document.getElementById('itemDetailsContainer');
    itemDetailsContainer.innerHTML = '';

    const detailsElement = document.createElement('div');
    detailsElement.innerHTML = `이름: ${item.itemName}<br>브랜드: ${item.brand}<br>카테고리: ${item.category}`;

    itemDetailsContainer.appendChild(detailsElement);
}

// 카테고리 버튼 클릭 시
const categoryButtons = document.querySelectorAll('.category-button');
categoryButtons.forEach(button => {
    button.addEventListener('click', () => {
        // 선택한 버튼의 카테고리 값을 가져와서 서버에 전달
        selectedCategory = button.getAttribute('data-category');

        // 다른 버튼들의 선택 상태를 초기화
        categoryButtons.forEach(otherButton => {
            if (otherButton !== button) {
                otherButton.classList.remove('selected');
            }
        });

        // 선택한 버튼을 선택 상태로 변경
        button.classList.add('selected');
        fetchItemsForCloset(selectedCloset);

        closeModal();
    });
});

const closeModalButton = document.querySelector('.close');
closeModalButton.addEventListener('click', closeModal);

function filterItemsByCategory(data, category) {
    return data.filter(item => item.category === category);
}

function displayItemsByCategory(data) {
    const itemList = document.getElementById('itemList');
    itemList.innerHTML = ''; // 이전 아이템 목록 초기화

    const categories = [...new Set(data.map(item => item.category))]; // 중복되지 않는 카테고리 목록

    categories.forEach(category => {
        const categoryItems = filterItemsByCategory(data, category);
        if (categoryItems.length > 0) {
            const categoryTitle = document.createElement('h3');
            categoryTitle.textContent = category;
            itemList.appendChild(categoryTitle);

            categoryItems.forEach(item => {
                const itemImage = document.createElement('img');
                itemImage.src = item.itemImageUrl; // 아이템 이미지 URL 설정
                itemImage.alt = item.itemName; // 아이템 이름을 alt 속성으로 설정

                // 이미지 크기 조절 및 상세 설정
                itemImage.style.width = '150px';
                itemImage.style.height = '150px';
                itemImage.style.objectFit = 'cover';
                itemImage.style.marginRight = '10px';
                // 아이템을 클릭했을 때 세부 정보를 표시하기 위한 이벤트 리스너 추가
                itemImage.addEventListener('click', () => {
                    handleItemClick(item);
                });

                itemList.appendChild(itemImage);
            });
        }
    });
}

// 옷장 생성 버튼 눌렸을 때
const addClothingButton = document.querySelector('.bi-plus-circle');
addClothingButton.addEventListener('click', () => {
    openAddItemModal();
});

// 옷장 생성 모달 열기
function openAddItemModal() {
    const addItemModal = document.getElementById('addItemModal');
    addItemModal.style.display = 'block';
}

// 옷장 생성 모달 닫기
function closeAddItemModal() {
    const addItemModal = document.getElementById('addItemModal');
    addItemModal.style.display = 'none';
}

// 저장 버튼 클릭 이벤트 핸들러
const saveItemButton = document.getElementById('saveClosetButton');
saveItemButton.addEventListener('click', () => {
    const closetName = document.getElementById('closetName').value;
    const isHiddenCheckbox = document.querySelector('input[name="hide"]');
    const isHidden = isHiddenCheckbox.checked;

    const queryParams = new URLSearchParams({
        closetName: closetName,
        isHidden: isHidden
    }).toString();

    sendItemToServer(queryParams);
});

// 취소 버튼 클릭 이벤트 핸들러
const cancelClosetButton = document.getElementById('cancelClosetButton');
cancelClosetButton.addEventListener('click', () => {
    closeAddItemModal();
});

// 서버로 아이템 이름 전송
function sendItemToServer(queryParams) {
    let requestUrl = `/closet/create?${queryParams}`;
    fetch(requestUrl, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${accessToken}`
        },
    })
        .then(response => {
            if (response.status === 200) {
                closeAddItemModal();
                alert('옷장이 생성되었습니다.');
                location.reload();
            } else if (response.status === 400) {
                alert('옷장을 더 이상 생성할 수 없습니다.');
            } else {
                throw new Error('옷장 추가 실패');
            }
        })
        .catch(error => {
            console.error('옷장 추가 실패:', error);
            alert('옷장 추가 실패: ' + error.message);
        });
}

// 옷장 삭제 모달 열기
function openDeleteModal() {
    const deleteModal = document.getElementById('deleteClosetModal');
    deleteModal.style.display = 'block';
}

// 옷장 삭제 모달 닫기
function closeDeleteModal() {
    const deleteModal = document.getElementById('deleteClosetModal');
    deleteModal.style.display = 'none';
}

// 옷장 삭제 버튼 눌렸을 때
const deleteClosetButton = document.querySelector('.bi-trash3');
deleteClosetButton.addEventListener('click', () => {
    openDeleteModal();
});

// 취소 버튼 클릭 이벤트 핸들러
const cancelButton = document.getElementById('cancelButton');
cancelButton.addEventListener('click', () => {
    closeDeleteModal();
});

// 삭제 확인 버튼 클릭 이벤트 핸들러
const acceptButton = document.getElementById('acceptButton');
acceptButton.addEventListener('click', () => {
    if (selectedCloset) {
        deleteCloset(selectedCloset);
    }
});

// 옷장 삭제 요청 보내는 함수
function deleteCloset(closetName) {
    fetch(`/closet/${closetName}`, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${accessToken}`
        },
    })
        .then(response => {
            if (response.status === 200) {
                closeDeleteModal();
                alert(`옷장 '${closetName}'이(가) 삭제되었습니다.`);
                location.reload(); // 페이지 새로고침
            } else {
                throw new Error(`옷장 '${closetName}' 삭제 실패`);
            }
        })
        .catch(error => {
            console.error('옷장 삭제 실패:', error);
            alert(`옷장 삭제 실패: ${error.message}`);
        });
}

// 페이지 로드 시 옷장 목록 호출
fetchClosetList();