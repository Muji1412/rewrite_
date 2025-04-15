document.addEventListener('DOMContentLoaded', function() {
    // --- 요소 가져오기 ---
    const selectAllCheckbox = document.getElementById('select-all');
    const itemCheckboxes = document.querySelectorAll('.item-checkbox');
    const deleteSelectedButton = document.querySelector('.delete-selected-button');
    const orderButton = document.querySelector('.order-button');
    const cartItemsContainer = document.querySelector('.cart-items'); // 상품 목록 컨테이너

    // --- 함수 정의 ---

    // 가격 문자열에서 숫자만 추출하는 함수 (예: "1,000,000원" -> 1000000)
    function parsePrice(priceString) {
        // 숫자와 관련된 문자(숫자, 쉼표) 외 제거 후 쉼표 제거, 숫자로 변환
        // 좀 더 안전하게 숫자 아닌 모든 문자를 제거
        const numericString = priceString.replace(/[^0-9]/g, '');
        return parseInt(numericString, 10) || 0; // 숫자로 변환, 실패 시 0 반환
    }

    // 숫자를 통화 형식 문자열로 변환하는 함수 (예: 1000000 -> "1,000,000")
    function formatPrice(priceNumber) {
        return priceNumber.toLocaleString('ko-KR');
    }

    // 총 주문 금액 및 버튼 텍스트 업데이트 함수
    function updateTotalPrice() {
        let totalPrice = 0;
        const checkedItems = document.querySelectorAll('.item-checkbox:checked'); // 체크된 상품만 선택

        checkedItems.forEach(checkbox => {
            const cartItem = checkbox.closest('.cart-item'); // 체크박스가 속한 .cart-item 찾기
            if (cartItem) {
                const priceElement = cartItem.querySelector('.item-price');
                if (priceElement) {
                    totalPrice += parsePrice(priceElement.textContent);
                }
            }
        });

        // 주문 버튼 텍스트 업데이트
        orderButton.textContent = `${formatPrice(totalPrice)} 원 주문하기`;
    }

    // '전체 선택' 체크박스 상태 업데이트 함수
    function updateSelectAllStatus() {
        const allItemCheckboxes = document.querySelectorAll('.item-checkbox'); // 현재 화면의 모든 상품 체크박스
        const allChecked = allItemCheckboxes.length > 0 && Array.from(allItemCheckboxes).every(cb => cb.checked);
        // 상품이 하나 이상 있고 && 모든 상품이 체크되었는지 확인

        selectAllCheckbox.checked = allChecked;
    }

    // 상품 삭제 함수 (개별 삭제 또는 선택 삭제 시 공통 사용 가능)
    function deleteCartItem(itemElement) {
        if (itemElement) {
            itemElement.remove(); // DOM에서 해당 상품 요소 제거
            // 삭제 후 총 가격 및 전체선택 상태 업데이트
            updateTotalPrice();
            updateSelectAllStatus();
        }
    }


    // --- 이벤트 리스너 연결 ---

    // 1. '전체 선택' 체크박스 변경 시
    selectAllCheckbox.addEventListener('change', function() {
        const isChecked = this.checked;
        itemCheckboxes.forEach(checkbox => {
            checkbox.checked = isChecked;
        });
        updateTotalPrice(); // 전체 선택/해제 시 가격 업데이트
    });

    // 2. 개별 상품 체크박스 변경 시
    cartItemsContainer.addEventListener('change', function(event) {
        // 이벤트 위임: .item-checkbox 에서 이벤트가 발생했을 때만 처리
        if (event.target.classList.contains('item-checkbox')) {
            updateSelectAllStatus(); // 전체 선택 상태 업데이트
            updateTotalPrice();     // 가격 업데이트
        }
    });

    // 3. '선택 삭제' 버튼 클릭 시
    deleteSelectedButton.addEventListener('click', function() {
        const checkedItems = document.querySelectorAll('.item-checkbox:checked');
        if (checkedItems.length === 0) {
            alert('삭제할 상품을 선택해주세요.');
            return;
        }

        if (confirm(`선택된 ${checkedItems.length}개 상품을 삭제하시겠습니까?`)) {
            checkedItems.forEach(checkbox => {
                const cartItem = checkbox.closest('.cart-item');
                deleteCartItem(cartItem); // 각 체크된 상품 삭제
            });
        }
    });

    // 4. 개별 '삭제' 버튼 클릭 시
    cartItemsContainer.addEventListener('click', function(event) {
        // 이벤트 위임: .item-delete-button 에서 이벤트가 발생했을 때만 처리
        if (event.target.classList.contains('item-delete-button')) {
            if (confirm('이 상품을 장바구니에서 삭제하시겠습니까?')) {
                const cartItem = event.target.closest('.cart-item');
                deleteCartItem(cartItem); // 해당 상품 삭제
            }
        }
    });

    // --- 초기 상태 설정 ---
    updateTotalPrice();     // 페이지 로드 시 총 가격 계산
    updateSelectAllStatus(); // 페이지 로드 시 전체 선택 상태 확인
});