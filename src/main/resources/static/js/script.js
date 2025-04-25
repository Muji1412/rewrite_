$(document).ready(function () {
    /* txt_slide */
    var txt = $("#ts_mid div");
    var cnt = 0;
    var max = txt.length - 1;

    setInterval(next, 1500);

    function next() {
        if (txt.is(":animated")) return false;
        $(txt[cnt]).animate({"top": "-100%"}).siblings().css({"top": "100%"});
        cnt++;
        if (cnt > max) cnt = 0;
        $(txt[cnt]).animate({"top": 0});
    } // 닫는 중괄호 추가

    function openModal() {
        $('#pop').css({"display": "block"});
        // $('#semi_wrap').css({"position":"sticky"});
        // $('section').css({"margin-top":"0"});
    } // 닫는 중괄호 추가

    // 모달 닫기 함수
    function closeModal() {
        $('#pop').css({"display": "none"});
        // $('#semi_wrap').css({"position":"fixed"});
        // $('section').css({"margin-top":"101px"});
    } // 닫는 중괄호 추가

    // 이벤트 리스너 연결
    $(".title_right").on("click", openModal); // openModal로 변경
    $("#close").on("click", closeModal); // closeModal로 변경

});
