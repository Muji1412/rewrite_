$(document).ready(function(){
    // 하트 토글
    const heart = document.getElementById('heart');
    let liked = false;
    heart.addEventListener('click', function() {
      liked = !liked;
      if (liked) {
        heart.classList.remove('fa-heart-o');
        heart.classList.add('fa-heart');
        heart.style.color = 'red';
      } else {
        heart.classList.remove('fa-heart');
        heart.classList.add('fa-heart-o');
        heart.style.color = 'black';
      }
    });
  
    // 이미지+동영상 슬라이더 (슬라이드 애니메이션)
    const mediaData = document.getElementById('mediaData');
    const mediaList = mediaData.dataset.media.split(',');
    let currentIndex = 0;
    const prevBtn = document.getElementById('prevBtn');
    const nextBtn = document.getElementById('nextBtn');
    const mediaWrapper = document.getElementById('mediaWrapper');
  
    function createMediaElement(src) {
      if (src.match(/\.(mp4|webm|ogg)$/i)) {
        const video = document.createElement('video');
        video.src = src;
        video.width = 400;
        video.height = 400;
        video.controls = true;
        video.muted = true;
        video.autoplay = true;
        video.playsInline = true;
        video.style.objectFit = "cover";
        video.style.borderRadius = "10px";
        video.currentTime = 0;
        setTimeout(()=>{video.play().catch(()=>{});},100);
        return video;
      } else if (src.match(/\.(jpg|jpeg|png|gif|bmp|webp)$/i)) {
        const img = document.createElement('img');
        img.src = src;
        img.style.width = "400px";
        img.style.height = "400px";
        img.style.objectFit = "cover";
        img.style.borderRadius = "10px";
        return img;
      } else {
        const div = document.createElement('div');
        div.innerText = "지원하지 않는 파일 형식입니다.";
        return div;
      }
    }
  
    function slideTo(newIndex, direction) {
        if (newIndex === currentIndex) return;
        const oldMedia = mediaWrapper.firstChild;
        const newMedia = createMediaElement(mediaList[newIndex].trim());
      
        // 방향에 따라 위치 설정
        // 오른쪽(다음) 버튼: 기존은 왼쪽(-400), 새 미디어는 오른쪽(400)에서 들어옴
        // 왼쪽(이전) 버튼: 기존은 오른쪽(400), 새 미디어는 왼쪽(-400)에서 들어옴
        let oldTarget, newStart;
        if (direction === 'left') {
          oldTarget = -400; // 기존 미디어가 왼쪽으로 나감
          newStart = 400;   // 새 미디어가 오른쪽에서 들어옴
        } else {
          oldTarget = 400;  // 기존 미디어가 오른쪽으로 나감
          newStart = -400;  // 새 미디어가 왼쪽에서 들어옴
        }
      
        newMedia.style.position = 'absolute';
        newMedia.style.left = newStart + "px";
        newMedia.style.top = "0";
        newMedia.style.transition = "left 0.5s cubic-bezier(.77,0,.18,1)";
        mediaWrapper.appendChild(newMedia);
      
        if (oldMedia) {
          oldMedia.style.position = 'absolute';
          oldMedia.style.left = "0px";
          oldMedia.style.top = "0";
          oldMedia.style.transition = "left 0.5s cubic-bezier(.77,0,.18,1)";
          setTimeout(() => {
            oldMedia.style.left = oldTarget + "px";
            newMedia.style.left = "0px";
          }, 10);
          setTimeout(() => {
            if (mediaWrapper.contains(oldMedia)) mediaWrapper.removeChild(oldMedia);
            newMedia.style.position = '';
            newMedia.style.left = '';
            newMedia.style.top = '';
            newMedia.style.transition = '';
          }, 510);
        } else {
          newMedia.style.position = '';
          newMedia.style.left = '';
          newMedia.style.top = '';
          newMedia.style.transition = '';
        }
        currentIndex = newIndex;
      }
  
    function showInitialMedia() {
      mediaWrapper.innerHTML = "";
      const media = createMediaElement(mediaList[currentIndex].trim());
      mediaWrapper.appendChild(media);
    }
  
    prevBtn.addEventListener('click', () => {
      const newIndex = (currentIndex - 1 + mediaList.length) % mediaList.length;
      slideTo(newIndex, 'right');
    });
    nextBtn.addEventListener('click', () => {
      const newIndex = (currentIndex + 1) % mediaList.length;
      slideTo(newIndex, 'left');
    });
  
    showInitialMedia();
  });
  