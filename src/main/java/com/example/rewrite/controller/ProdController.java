package com.example.rewrite.controller;

import com.example.rewrite.command.ProductDTO;
import com.example.rewrite.command.user.UserSessionDto;
import com.example.rewrite.entity.*;
import com.example.rewrite.repository.product.ProductRepository;
import com.example.rewrite.service.address.AddressService;
import com.example.rewrite.service.cart.CartService;
import com.example.rewrite.service.prod.ProdService;

import com.example.rewrite.service.wishlist.WishlistService;

import com.example.rewrite.service.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/prod")
public class ProdController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProdService prodService;

    @Autowired
    private final ProductRepository productRepository;

    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private UserService userService;
    @Autowired
    private AddressService addressService;


    public ProdController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/orderPay")
    public String orderPay(HttpSession session, Model model) {
        UserSessionDto user = (UserSessionDto) session.getAttribute("user");
        if(user == null) {
            return "redirect:/user/login";
        }
        Long uid = user.getUid();
        System.out.println("uid: " + uid);  // 문자열 + 변수
        Address defaultAddress = addressService.getDefaultAddress(uid);
        if (defaultAddress == null) {
            return "redirect:/address/detail";
        }
        model.addAttribute("defaultAddress", defaultAddress);

        if (defaultAddress != null && defaultAddress.getAddress() != null) {
            String[] parts = defaultAddress.getAddress().split("/");

            if (parts.length == 3) {
                model.addAttribute("postcode", parts[0]);
                model.addAttribute("addr", parts[1]);
                model.addAttribute("detailAddress", parts[2]);
            } else {
                model.addAttribute("postcode", "");
                model.addAttribute("addr", "");
                model.addAttribute("detailAddress", "");
            }
        }
        System.out.println("기본 주소: " + defaultAddress);
        if(defaultAddress != null && defaultAddress.getPhoneNum() != null) {
            String phoneNum = defaultAddress.getPhoneNum();
            String formatPhoneNum = phoneNum;

            if(phoneNum.length() == 11) {
                formatPhoneNum = phoneNum.replaceFirst("(\\d{3})(\\d{4})(\\d{4})","$1-$2-$3");
            }

            model.addAttribute("formatPhoneNum", formatPhoneNum);
        }

        List<Cart> checkedCarts = cartService.getCheckedCarts(uid);

        int totalPrice = 0;
        for (Cart cart : checkedCarts) {
            totalPrice += Integer.parseInt(cart.getProduct().getPrice());
        }

        String formattedTotalPrice = String.format("%,d", totalPrice) + "원";
        model.addAttribute("formattedTotalPrice", formattedTotalPrice);  // 주문금액

        int shippingFee = 0;
        model.addAttribute("shippingFee", shippingFee);

        // 최종 결제금액 예시
        int finalPrice = totalPrice;  // 예시로 총금액을 주문금액과 같게 설정
        String totalShippingFee = String.format("%,d", finalPrice) + "원";
        model.addAttribute("totalShippingFee", totalShippingFee);  // 총 결제금액


        model.addAttribute("cartList", checkedCarts);

        System.out.println("checkedCarts: " + checkedCarts.toString());

        return "prod/orderPay";
    }
    @GetMapping("/cart")
    public String cart(Model model, HttpSession session) {
        UserSessionDto user = (UserSessionDto)session.getAttribute("user");
        if(user == null) {
            return "redirect:/user/login";
        }
        Long uid = user.getUid();
        System.out.println(uid);
        List<Cart> cartList = cartService.getCarts(uid);

        int totalPrice = cartService.calculateTotalPrice(cartList);

        model.addAttribute("cartList", cartList);
        for (Cart cart : cartList) {
            System.out.println("cart tostring");
            System.out.println(cart.toString());
            System.out.println("cart getUser tostring");
            System.out.println(cart.getUser().toString());
            System.out.println("cart getProduct tostring");
            System.out.println(cart.getProduct().toString());

        }
        model.addAttribute("totalPrice", totalPrice);

        return "prod/cart";
    }

    @GetMapping("/orderDetail")
    public String orderDetail() {
        return "prod/orderDetail";
    }

    @GetMapping("/productReg")
    public String reg(HttpSession session, Model model) {
        UserSessionDto user = (UserSessionDto) session.getAttribute("user");
        if (user == null) {
            return "redirect:/user/login";
        }

        // 기본 주소 조회
        Address defaultAddress = addressService.getDefaultAddress(user.getUid());
        String postcode = "";
        String addr = "";
        String detailAddress = "";

        if (defaultAddress != null && defaultAddress.getAddress() != null) {
            String[] parts = defaultAddress.getAddress().split("/");
            if (parts.length == 3) {
                postcode = parts[0];
                addr = parts[1];
                detailAddress = parts[2];
            }
        }

        // 모델에 값 전달
        model.addAttribute("postCode", postcode);
        model.addAttribute("addr", addr);
        model.addAttribute("detailAddress", detailAddress);

        return "prod/productReg";
    }


    @GetMapping("/prodDetail")
    public String prodDetail(@RequestParam Long prodId, Model model, HttpSession session) {
        // 상품 상세 정보 조회
        ProductDTO product = prodService.getProductById(prodId);
        model.addAttribute("product", product);

        // 로그인한 경우 찜 상태 확인
        UserSessionDto user = (UserSessionDto) session.getAttribute("user");
        if (user != null) {
            boolean isWishlisted = wishlistService.isWishlisted(user.getUid(), prodId);
            model.addAttribute("isWishlisted", isWishlisted);
        }

        return "prod/prodDetail";
    }

    @GetMapping("/prodList")
    public String listProducts(Model model, @RequestParam(defaultValue = "latest") String sortBy, HttpSession session) {
        List<ProductDTO> products = prodService.getAllProducts(sortBy);

        UserSessionDto user = (UserSessionDto) session.getAttribute("user");
        Map<Long, Boolean> wishMap = new HashMap<>();
        if (user != null) {
            for (ProductDTO prod : products) {
                boolean isWishlisted = wishlistService.isWishlisted(user.getUid(), prod.getProdId());
                wishMap.put(prod.getProdId(), isWishlisted);
            }
        }
        model.addAttribute("products", products);
        model.addAttribute("wishMap", wishMap); // 상품ID별 찜 상태
        model.addAttribute("currentSort", sortBy);
        return "prod/prodList";
    }

    @GetMapping("/myProdList")
    public String myProdList(HttpSession session, Model model){
        UserSessionDto user = (UserSessionDto)session.getAttribute("user");
        if(user == null) {
            return "redirect:/user/login";
        }
        Map<Long, Boolean> wishMap = new HashMap<>();
        if (user != null) {
            for (ProductDTO prod

                    : prodService.getMyProducts(user.getUid())) {
                boolean isWishlisted = wishlistService.isWishlisted(user.getUid(), prod.getProdId());
                wishMap.put(prod.getProdId(), isWishlisted);
            }
        }

        model.addAttribute("user", userService.getProfile(user.getUid()));
        model.addAttribute("wishMap", wishMap);
        model.addAttribute("products",prodService.getMyProducts(user.getUid()) );

        return  "prod/prodList";
    }

    @GetMapping("/wishList")
    public String wishList(HttpSession session, Model model) {
        UserSessionDto user = (UserSessionDto) session.getAttribute("user");
        if (user == null) {
            return "redirect:/user/login";
        }

        List<Wishlist> wishList = wishlistService.getWishlistByUserId(user.getUid());
        System.out.println("wishList: " + wishList.toString());

        model.addAttribute("user", userService.getProfile(user.getUid()));
        model.addAttribute("wishList", wishList);
        return "prod/prodList";
    }

    @PostMapping("/productReg")
    public String register(
            @ModelAttribute ProductDTO productDTO,
            RedirectAttributes redirectAttributes) {

        // 서비스 레이어를 통해 상품 등록
        prodService.registerProduct(productDTO);

        // 성공 메시지 추가
        redirectAttributes.addFlashAttribute("success", "상품이 성공적으로 등록되었습니다.");
        System.out.println("Flash 속성 설정됨: success = 상품이 성공적으로 등록되었습니다.");

        return "redirect:/prod/prodList";
    }

    // 상품 수정 페이지 이동
    @GetMapping("/productUpdate")
    public String productUpdate(@RequestParam Long prodId, Model model) {
        ProductDTO product = prodService.getProductById(prodId);
        String postCode = "";
        String addr = "";
        String detailAddress = "";

        String pickupAddress = product.getPickupAddress();
        if (pickupAddress != null && pickupAddress.contains("/")) {
            String[] parts = pickupAddress.split("/");
            if (parts.length == 3) {
                postCode = parts[0];
                addr = parts[1];
                detailAddress = parts[2];
            }
        }
// 이후 model에 addAttribute
        model.addAttribute("postCode", postCode);
        model.addAttribute("addr", addr);
        model.addAttribute("detailAddress", detailAddress);
        return "prod/productReg";  // 등록 페이지를 재사용
    }

    // 상품 수정 처리
    @PostMapping("/productUpdate")
    public String updateProduct(@ModelAttribute ProductDTO productDTO) {
        // 서비스 레이어를 통해 상품 수정
        prodService.updateProduct(productDTO);
        return "redirect:/prod/prodDetail?prodId=" + productDTO.getProdId();
    }

    //주문 내역 페이지 이동
    @GetMapping("/orderList")
    public String orderList(){

        return "prod/orderList";
    }

    // 상품 삭제 처리
    @GetMapping("/productDelete")
    public String deleteProduct(@RequestParam Long prodId, HttpSession session, RedirectAttributes redirectAttributes) {
        // 로그인 확인
        UserSessionDto user = (UserSessionDto) session.getAttribute("user");
        if (user == null) {
            return "redirect:/user/login";
        }

        // 상품 조회
        ProductDTO product = prodService.getProductById(prodId);

        // 작성자 확인
        if (!user.getUid().equals(product.getUid())) {
            redirectAttributes.addFlashAttribute("error", "상품 삭제 권한이 없습니다.");
            return "redirect:/prod/prodDetail?prodId=" + prodId;
        }

        // 상품 삭제
        prodService.deleteProduct(prodId);

        redirectAttributes.addFlashAttribute("success", "상품이 성공적으로 삭제되었습니다.");
        return "redirect:/prod/prodList";
    }

    @PostMapping("/bump")
    public String bumpProduct(@RequestParam Long prodId, HttpSession session, RedirectAttributes redirectAttributes) {
        UserSessionDto user = (UserSessionDto) session.getAttribute("user");
        if (user == null) {
            return "redirect:/user/login";
        }
        ProductDTO product = prodService.getProductById(prodId);
        if (!user.getUid().equals(product.getUid())) {
            redirectAttributes.addFlashAttribute("error", "본인 글만 끌어올릴 수 있습니다.");
            return "redirect:/prod/prodDetail?prodId=" + prodId;
        }
        prodService.bumpProduct(prodId);
        redirectAttributes.addFlashAttribute("success", "끌어올리기가 완료되었습니다!");
        return "redirect:/prod/prodDetail?prodId=" + prodId; // 상세페이지로 리다이렉트
    }

    @GetMapping("/orderSuccess")
    public String orderSuccess(
            @RequestParam(value = "paymentKey", required = false) String paymentKey,
            @RequestParam(value = "orderId", required = false) String orderId,
            @RequestParam(value = "amount", required = false) Integer amount,
            Model model, HttpSession session) {

        // 결제 정보를 모델에 추가
        if (paymentKey != null) {
            model.addAttribute("paymentKey", paymentKey);
        }
        if (orderId != null) {
            model.addAttribute("orderId", orderId);
        }
        if (amount != null) {
            model.addAttribute("amount", amount);
        }

        // 세션에서 사용자 ID를 가져와 모델에 추가
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            model.addAttribute("userId", userId);
        }

        return "prod/orderSuccess";
    }

}
