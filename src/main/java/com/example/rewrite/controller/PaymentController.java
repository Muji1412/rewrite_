package com.example.rewrite.controller;

import com.example.rewrite.entity.Address;
import com.example.rewrite.entity.Cart;
import com.example.rewrite.entity.Orders;
import com.example.rewrite.entity.Users;
import com.example.rewrite.repository.users.UsersRepository;
import com.example.rewrite.service.address.AddressService;
import com.example.rewrite.service.cart.CartService;
import com.example.rewrite.service.order.OrderService;
import com.example.rewrite.service.payment.PaymentService;
import com.example.rewrite.service.prod.ProdService;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PaymentController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String WIDGET_SECRET_KEY = "test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6";
    private static final String API_SECRET_KEY = "test_sk_pP2YxJ4K872d45m2A4XprRGZwXLO";
    private final Map<String, String> billingKeyMap = new HashMap<>();

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProdService prodService;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private AddressService addressService;
    @Autowired
    private PaymentService paymentService;

    // 우리 서비스단에서 쓸 컨트롤러
    @RequestMapping("/confirm/payDone")
    public String confirmPayDone(HttpServletRequest request,
                                 @RequestParam("uid") Long uid,
                                 @RequestParam("orderId") Long orderId,
                                 @RequestParam("paymentKey") String paymentKey,
                                 @RequestParam("tossOrderId") String tossOrderId,
                                 @RequestParam("finalPrice") Integer finalPrice) {
        logger.info("confirmPayDone 메서드 실행 - 결제 끝남.");

        // 1. 사용자 및 배송지 정보 조회
        Users buyer = usersRepository.findById(uid).orElseThrow();
        Address defaultAddress = addressService.getDefaultAddress(uid);

        //체크된 장바구니 상품 조회
        List<Cart> checkedCarts = cartService.getCheckedCarts(uid);

        //배송지
        String[] addressParts = defaultAddress.getAddress().split("/");
        String postcode = addressParts.length > 0 ? addressParts[0] : "";
        String addr = addressParts.length > 1 ? addressParts[1] : "";
        String detailAddr = addressParts.length > 2 ? addressParts[2] : "";

        // 배송 요청사항 추출
        String deliveryRequest = request.getParameter("deliveryRequest");
        //결제 정보를 바탕으로 주문 객체 생성 및 저장
        Orders order = Orders.builder()
                .buyer(buyer)
                .address(defaultAddress)
                .receiverName(buyer.getName())
                .receiverPhone(buyer.getPhone())
                .postcode(postcode)
                .addr(addr)
                .detailAddr(detailAddr)
                .finalPrice(finalPrice)
                .orderedAt(LocalDateTime.now())
                .paidAt(LocalDateTime.now())
                .deliveryRequest(deliveryRequest)
                .orderStatus("주문완료")
                .paymentStatus("결제")
                .paymentKey(paymentKey)
                .paymentMethod(request.getParameter("paymentMethod") != null ?
                        request.getParameter("paymentMethod") : "CARD")
                .tossOrderId(tossOrderId)
                .build();

        orderService.saveOrder(order, checkedCarts);
        // TODO - UID에 관련된 카트 삭제 (장바구니 초기화시키기)
        cartService.clearUserCart(uid);
        // TODO - 결제내역 테이블에 기록해주기
        paymentService.recordPaymentHistory(order,buyer,paymentKey,"결제완료",request.getParameter("paymentMethod"),LocalDateTime.now());
        // TODO - 결제한 물품들 status 변경해주기 (판매됨으로)
        List<Cart> orderedCarts = cartService.getCheckedCarts(uid);
        for (Cart cart : orderedCarts) {
            prodService.updateProductStatus(cart.getProduct().getProdId(), "판매완료");
        }
        // TODO - 다음에 컨트롤러나 api 하나 만들어서, 오더카트(오더에 해당되는 물품들 가져오는거 만들기)

        return "redirect:/prod/orderSuccess";
    }




    // 토스 제공 성공시 쓸 컨트롤러
    // @RequestMapping은 method를 지정하지 않겠다는 이야기, Post, get, delete 등 전부 사용이 가능함
    @RequestMapping(value = {"/confirm/widget", "/confirm/payment"})
    public ResponseEntity<JSONObject> confirmPayment(HttpServletRequest request, @RequestBody String jsonBody) throws Exception {

        // 요청받은 컨트롤러 이름에 따라서 위젯인지, 일반결제인지 구분하여 api 구별해서 사용
        String secretKey = request.getRequestURI().contains("/confirm/payment") ? API_SECRET_KEY : WIDGET_SECRET_KEY;

        // sendRequest로 보냄, get방식으로 받아둔 데이터를 json으로 변환해서 받고, api 호출
        // 페이먼트키, 오더아이디, amount 세개 담아주고, 시크릿키로 인증, api에서 체크
        JSONObject response = sendRequest(parseRequestData(jsonBody), secretKey, "https://api.tosspayments.com/v1/payments/confirm");

        // 반환되는 값에 따라서 200 보낼지, 400 보낼지 판단, ResponseEntity로 Json 반환해줌.
        int statusCode = response.containsKey("error") ? 400 : 200;
        return ResponseEntity.status(statusCode).body(response);
    }







    //유틸 메서드 목록


    private JSONObject parseRequestData(String jsonBody) {
        try {
            return (JSONObject) new JSONParser().parse(jsonBody);
        } catch (ParseException e) {
            logger.error("JSON Parsing Error", e);
            return new JSONObject();
        }
    }


    // 파라미터로 json, 키, api url
    private JSONObject sendRequest(JSONObject requestData, String secretKey, String urlString) throws IOException {

        // HttpURLConnection 객체 생성, creatCOnnection 메서드에 키랑 api삽입해서 넣을 커넥션 객체 만들어옴
        HttpURLConnection connection = createConnection(secretKey, urlString);

        // 연결에 데이터를 쓸 수 있는 아웃풋스트림 얻음
        try (OutputStream os = connection.getOutputStream()) {

            //json데이터 문자열 반환 후, utf-8 인코딩해서 서버로 전송
            os.write(requestData.toString().getBytes(StandardCharsets.UTF_8));
        }

        // 전송 후

        // 커넥션 객체에서 response 코드 찾음 - 에러인지 아닌지 판단 - responseStream에 넣어줌.
        try (InputStream responseStream = connection.getResponseCode() == 200 ? connection.getInputStream() : connection.getErrorStream();

             // 바이트로 받아온 스트림을 문자로 변환
             Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8)) {

            //그렇게 만든 문자객체를 JSON으로 파싱해서 리턴해줌.
            return (JSONObject) new JSONParser().parse(reader);
        } catch (Exception e) { // 처리중 실패시 에러메세지 반환
            logger.error("Error reading response", e);
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("error", "Error reading response");
            return errorResponse;
        }
    }

    private HttpURLConnection createConnection(String secretKey, String urlString) throws IOException {
        // 문자열 객체를 URL로 만들어줌
        URL url = new URL(urlString);

        //HttpURLConnection 형변환, 그냥 url이 아니라 Http 통신에 필요한애들 만들어줌
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // 각각
        // 헤더 설정 - Authorization: 서버에 인증 정보를 전달
        // 컨텐츠타입 설정
        // 리퀘스트 메서드 설정
        connection.setRequestProperty("Authorization", "Basic " + Base64.getEncoder().encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8)));
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");

        //데이터 보내는것으로 설정 / 반환
        connection.setDoOutput(true);
        return connection;
    }

}
