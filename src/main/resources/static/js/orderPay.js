$(document).ready(function () {
    function openAddressPopup() {
        const popup = window.open('/user/address-popup', '배송지 변경', 'width=600,height=600,scrollbars=yes');
    }

    function selectAddress(name, phone, addressFull, addressId) {
        if (window.opener) {
            window.opener.document.querySelector('.info-value.receiver-name').innerText = name;
            window.opener.document.querySelector('.info-value.receiver-phone').innerText = phone;
            window.opener.document.querySelector('.info-value.receiver-address').innerText = addressFull;
            window.opener.document.querySelector('input[name="addressId"]').value = addressId;
            window.close();
        }
    }

    /* 기본 모달팝업임 */
    function openPay() {
        $('#payPop').css({"display": "block"});
    }

    // 모달 닫기 함수
    function closePay() {
        $('#payPop').css({"display": "none"});
    }

    // 이벤트 리스너 연결
    $(document).on("click", ".payPop", openPay); // 동적 요소에도 작동
    $(document).on("click", "#closePay", closePay);


    /* 가져온 카드 js */
    $(document).ready(function () {

        //아무 주소지도 없을경우
        if ($('.address-list').children().length === 0) {
            $('.address-list').append('<div class="no-address" style="margin: 30px 200px;">등록된 주소지가 없습니다.</div>');
        }

        $('.btn-edit').click(function () { //주소지 수정
            var form = $(this).closest('form');
            form.attr('action', '/address/edit');
            form.submit(); // form 제출
        });

        $('.btn-select').click(function () { //기본주소지 선택
            var form = $(this).closest('form');
            form.attr('action', '/address/default');
            form.submit(); // form 제출
        });

        $('.btn-delete').click(function () { //삭제 버튼
            var form = $(this).closest('form');

            if (form.parent().prev().find('.default').text() === '기본주소지') { //기본주소지 삭제 방지
                alert("기본 주소지는 삭제할 수 없습니다.");
                return;
            }
            if (confirm("정말 삭제 하시겠습니까?")) {
                form.attr('action', '/address/delete'); // form action 설정
                form.submit(); // form 제출
            }
        });


        $('.btn-add').click(function () { //주소지 추가

            window.location.href = '/address/reg'; // 주소지 추가 페이지로 이동
        });
    });
});