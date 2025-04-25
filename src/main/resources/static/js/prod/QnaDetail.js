$(document).ready(function(){
    // 모달 열기 함수
    function open(){
        $('#pop').css({"display":"block"});
        // $('#semi_wrap').css({"position":"sticky"});
        // $('section').css({"margin-top":"0"});
    }

    // 모달 닫기 함수
    function close(){
        $('#pop').css({"display":"none"});
        // $('#semi_wrap').css({"position":"fixed"});
        // $('section').css({"margin-top":"101px"});
    }

    // 이벤트 리스너 연결
    $(".title_right").on("click", open);
    $("#close").on("click", close);
});
