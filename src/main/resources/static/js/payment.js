// 결제 금액 정보
let amount = {
    currency: "KRW",
    value: 50000, // 기본값 (실제로는 서버에서 받은 값으로 대체됨)
};

let selectedPaymentMethod = "CARD"; // 기본값은 신용카드
let orderItems = [];

// ------  SDK 초기화 ------
const clientKey = "test_ck_D5GePWvyJnrK0W0k6q8gLzN97Eoq";
const customerKey = generateRandomString();
const tossPayments = TossPayments(clientKey);
const payment = tossPayments.payment({
    customerKey,
});

// 페이지 로드 시 이벤트 리스너 설정
//ㅎㅎ
document.addEventListener('DOMContentLoaded', function() {
    // 결제 방법 라디오 버튼에 이벤트 리스너 추가
    const radios = document.querySelectorAll('input[name="payment-method"]');
    radios.forEach(radio => {
        radio.addEventListener('change', function() {
            if (this.checked) {
                switch(this.value) {
                    case "credit-card":
                        selectPaymentMethod("CARD");
                        break;
                    case "kakao-pay":
                        selectPaymentMethod("KAKAO_PAY");
                        break;
                    case "toss-pay":
                        selectPaymentMethod("TOSS_PAY");
                        break;
                }
            }
        });
    });

    updatePaymentAmount();
    collectOrderItems();
});

function selectPaymentMethod(method) {
    selectedPaymentMethod = method;
}

function collectOrderItems() {
    const productRows = document.querySelectorAll('.product-table tbody tr');
    orderItems = Array.from(productRows).map(row => {
        const productName = row.querySelector('.product-name').textContent;
        const productPrice = row.querySelector('.product-price').textContent.replace(/[^0-9]/g, '');
        const productImage = row.querySelector('.product-thumbnail').getAttribute('src');
        return {
            name: productName,
            price: parseInt(productPrice, 10),
            image: productImage
        };
    });
}

function updatePaymentAmount() {
    const orderAmountElement = document.querySelector('.summary-row:nth-child(1) span:nth-child(2)');
    const shippingFeeElement = document.querySelector('.summary-row:nth-child(2) span:nth-child(2)');
    const totalAmountElement = document.querySelector('.total-amount');

    if (orderAmountElement && shippingFeeElement && totalAmountElement) {
        const orderAmountValue = parseInt(orderAmountElement.textContent.replace(/[^0-9]/g, ''), 10);
        const shippingFeeValue = parseInt(shippingFeeElement.textContent.replace(/[^0-9]/g, ''), 10);
        const totalAmountValue = parseInt(totalAmountElement.textContent.replace(/[^0-9]/g, ''), 10);

        amount.value = totalAmountValue;
        window.orderAmount = orderAmountValue;
        window.shippingFee = shippingFeeValue;
    }
}

async function placeOrder() {
    updatePaymentAmount();
    try {
        const orderId = generateRandomString();
        // 수정: 올바른 orderName 생성
        let orderName = orderItems.length > 0 ?
            orderItems[0].name + (orderItems.length > 1 ? ` 외 ${orderItems.length - 1}건` : '') :
            "상품 주문";

        const receiverName = document.querySelector('.info-row:nth-child(1) .info-value').textContent.trim();
        const receiverPhone = document.querySelector('.info-row:nth-child(2) .info-value').textContent.trim();
        const addressInfo = document.querySelector('.info-row:nth-child(3) .info-value span').textContent.trim();
        const deliveryRequest = document.querySelector('.info-value.input-value input').value.trim();

        // 수정: 우편번호 올바르게 추출
        const postcodeMatch = addressInfo.match(/\(([^)]+)\)/);
        const postcode = postcodeMatch ? postcodeMatch[1] : '';
        const addressWithoutPostcode = addressInfo.replace(/\s*\([^)]+\)/, '').trim();
        const addrParts = addressWithoutPostcode.split(' ');
        const detailAddr = addrParts.pop() || '';
        const addr = addrParts.join(' ');

        // Orders 엔티티에 맞게 데이터 구성
        const orderData = {
            // 주문 정보
            orderId: orderId,
            totalPrice: window.orderAmount || 0,
            shippingFee: window.shippingFee || 0,
            finalPrice: amount.value,
            // 배송지 정보
            receiverName: receiverName,
            receiverPhone: receiverPhone,
            postcode: postcode,
            addr: addr,
            detailAddr: detailAddr,
            deliveryRequest: deliveryRequest,
            // 주문 시각은 서버에서 처리
            orderedAt: new Date().toISOString(),
            // 상품 정보
            orderItems: orderItems,
            paymentMethod: selectedPaymentMethod
        };

        sessionStorage.setItem('orderData', JSON.stringify(orderData));

        const commonParams = {
            amount: amount,
            orderId: orderId,
            orderName: orderName,
            successUrl: window.location.origin + "/payment/success",
            failUrl: window.location.origin + "/payment/fail",
            customerEmail: "", // 사용자 이메일 정보가 있으면 추가
            customerName: receiverName
        };

        switch (selectedPaymentMethod) {
            case "CARD":
                await payment.requestPayment({
                    method: "CARD",
                    ...commonParams,
                    card: {
                        useEscrow: false,
                        flowMode: "DEFAULT",
                        useCardPoint: false,
                        useAppCardOnly: false,
                    },
                });
                break;
            case "KAKAO_PAY":
                await payment.requestPayment({
                    method: "EASY_PAY",
                    ...commonParams,
                    easyPay: "KAKAO_PAY",
                });
                break;
            case "TOSS_PAY":
                await payment.requestPayment({
                    method: "EASY_PAY",
                    ...commonParams,
                    easyPay: "TOSSPAY",
                });
                break;
            default:
                alert("결제 방법을 선택해주세요.");
                break;
        }
    } catch (error) {
        console.error("결제 요청 중 오류가 발생했습니다:", error);
        alert("결제 요청 중 오류가 발생했습니다: " + error.message);
    }
}

function generateRandomString() {
    return window.btoa(Math.random()).slice(0, 20);
}

// 주소 팝업 열기 함수
function openAddressPopup() {
    // 배송지 변경 팝업 구현
    // 예: 새 창 또는 모달 다이얼로그로 주소 선택 인터페이스 표시
    alert("배송지 변경 기능은 아직 구현되지 않았습니다.");
}
