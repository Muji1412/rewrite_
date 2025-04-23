document.addEventListener('DOMContentLoaded', function() {
    // 모든 카드 요소 선택 (미디어 컨테이너가 아닌 카드 전체)
    const cards = document.querySelectorAll('.card');

    // 각 카드에 이벤트 리스너 추가
    cards.forEach(card => {
        const mediaContainer = card.querySelector('.media-container');
        const video = mediaContainer ? mediaContainer.querySelector('.prod-video') : null;
        const hoverText = mediaContainer ? mediaContainer.querySelector('.hover-text') : null;
        let previewTimeout = null;

        // 비디오가 있는 경우에만 이벤트 리스너 추가
        if (video) {
            // 비디오 소스 설정 (지연 로딩)
            const videoSrc = video.getAttribute('data-src');
            if (videoSrc) {
                // 소스 요소 생성 및 추가
                const source = document.createElement('source');
                source.src = videoSrc;
                source.type = 'video/mp4';
                video.appendChild(source);
            }

            // 마우스 진입 이벤트 - 카드 전체에 적용
            card.addEventListener('mouseenter', () => {
                // 비디오 시작 위치 설정 (1초부터)
                video.currentTime = 1;
                video.playbackRate = 0.5; // 50% 속도로 재생

                // 비디오 재생
                const playPromise = video.play();

                // 재생 프로미스 처리
                if (playPromise !== undefined) {
                    playPromise.then(() => {
                        // 컨테이너에 비디오 활성화 클래스 추가
                        mediaContainer.classList.add('video-active');
                        // 호버 텍스트 숨기기
                        if (hoverText) hoverText.classList.remove('active');

                        // 4초 후 자동 정지
                        previewTimeout = setTimeout(() => {
                            video.pause();
                            mediaContainer.classList.remove('video-active');
                            if (hoverText) hoverText.classList.add('active');
                        }, 4000);
                    }).catch(error => {
                        console.error('비디오 재생 실패:', error);
                    });
                }
            });

            // 마우스 이탈 이벤트 - 카드 전체에 적용
            card.addEventListener('mouseleave', () => {
                // 타임아웃 정리
                if (previewTimeout) {
                    clearTimeout(previewTimeout);
                    previewTimeout = null;
                }

                // 비디오 정지 및 초기화
                video.pause();
                video.currentTime = 0;

                // 비디오 숨기기
                mediaContainer.classList.remove('video-active');
                // 호버 텍스트 표시
                if (hoverText) hoverText.classList.add('active');
            });
        }

        // 카드 클릭 이벤트 - 상세 페이지로 이동 (미디어 컨테이너 예외처리 제거)
        card.addEventListener('click', function() {
            // 상품 ID를 사용하여 상세 페이지로 이동
            const prodId = this.getAttribute('data-prod-id');
            if (prodId) {
                window.location.href = `/prod/prodDetail?prodId=${prodId}`;
            }
        });
    });

    // 페이지 맨 위로 스크롤하는 함수
    window.scrollToTop = function() {
        window.scrollTo({
            top: 0,
            behavior: 'smooth'
        });
    };
});