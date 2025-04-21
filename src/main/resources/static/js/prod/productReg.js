// 카테고리 맵
const categoryMap = {
    '의류': ['여성의류', '남성의류'],
    '게임': ['PC게임', '콘솔'],
    '전자기기': ['PC', '모바일'],
    '가전제품': ['대형가전', '소형가전']
};

// 업로드 제한
const MAX_IMAGE = 4;
const MAX_VIDEO = 1;
const MAX_TOTAL = 5;

let uploadedImageUrls = []; // 이미지 URL 배열
let uploadedVideoUrl = null; // 동영상 URL

document.addEventListener('DOMContentLoaded', function() {
    // 대분류 버튼
    const mainCategoryBtns = document.querySelectorAll('.category-box .category-item');
    mainCategoryBtns.forEach(btn => {
        btn.addEventListener('click', function() {
            mainCategoryBtns.forEach(b => b.classList.remove('selected'));
            this.classList.add('selected');
            document.getElementById('category_max').value = this.innerText;
            showSubCategory(this.innerText);
        });
    });

    // 파일 업로드 기능 초기화
    initFileUpload();
});

// 중분류 버튼 동적 생성 및 클릭 이벤트
function showSubCategory(mainCategory) {
    const subDiv = document.getElementById('sub-category');
    subDiv.innerHTML = ''; // 기존 중분류 버튼 지우기
    const subs = categoryMap[mainCategory];

    if (!subs || subs.length === 0) {
        subDiv.innerHTML = '중분류 없음';
        return;
    }

    subs.forEach(sub => {
        const btn = document.createElement('button');
        btn.innerText = sub;
        btn.type = "button";
        btn.className = 'category-item';
        btn.style.display = 'block';
        btn.addEventListener('click', function(event) {
            event.stopPropagation();
            const allBtns = subDiv.querySelectorAll('.category-item');
            allBtns.forEach(b => b.classList.remove('selected'));
            this.classList.add('selected');
            document.getElementById('category_min').value = sub;
        });
        subDiv.appendChild(btn);
    });
}

// 파일 업로드 기능 초기화 함수
function initFileUpload() {
    const uploadBox = document.getElementById('file-upload-box');
    const fileInput = document.getElementById('file-input');
    const previewContainer = document.getElementById('preview-container');
    const fileCount = document.getElementById('file-count');

    // 클릭 시 파일 선택창 열기
    uploadBox.addEventListener('click', function(e) {
        if (e.target === fileInput) return;
        fileInput.click();
    });

    // 드래그 앤 드롭
    uploadBox.addEventListener('dragover', function(e) {
        e.preventDefault();
        uploadBox.style.backgroundColor = '#e6f7ff';
    });

    uploadBox.addEventListener('dragleave', function(e) {
        e.preventDefault();
        uploadBox.style.backgroundColor = '';
    });

    uploadBox.addEventListener('drop', function(e) {
        e.preventDefault();
        uploadBox.style.backgroundColor = '';
        handleFiles(Array.from(e.dataTransfer.files));
    });

    // 파일 input으로 선택 시
    fileInput.addEventListener('change', function(e) {
        handleFiles(Array.from(e.target.files));
        e.target.value = '';
    });

    // 파일 처리 함수 (업로드 후 URL 저장)
    function handleFiles(files) {
        let imageCount = uploadedImageUrls.length;
        let videoCount = uploadedVideoUrl ? 1 : 0;

        for (const file of files) {
            if (imageCount + videoCount >= MAX_TOTAL) break;
            if (file.type.startsWith('image/')) {
                if (imageCount >= MAX_IMAGE) continue;
                imageCount++;
                uploadFile(file, 'image');
            } else if (file.type.startsWith('video/')) {
                if (videoCount >= MAX_VIDEO) continue;
                videoCount++;
                uploadFile(file, 'video');
            } else {
                continue; // 이미지/비디오가 아니면 무시
            }
        }
    }

    // 실제 업로드 함수 (upload.js 방식)
    function uploadFile(file, type) {
        const formData = new FormData();
        formData.append('file', file);
        formData.append('folderPath', 'product_picture');

        fetch('/api/files', {
            method: 'POST',
            body: formData
        })
            .then(response => {
                if (!response.ok) throw new Error('서버 오류: ' + response.status);
                return response.text();
            })
            .then(url => {
                if (type === 'image') {
                    uploadedImageUrls.push(url);
                } else if (type === 'video') {
                    uploadedVideoUrl = url;
                }
                renderPreview();
            })
            .catch(error => {
                alert('업로드 실패: ' + error.message);
            });
    }

    // 미리보기 렌더링
    function renderPreview() {
        // 파일 개수 카운트 업데이트
        const totalCount = uploadedImageUrls.length + (uploadedVideoUrl ? 1 : 0);
        fileCount.innerText = totalCount + "/5";
        previewContainer.innerHTML = '';

        // 이미지 썸네일
        uploadedImageUrls.forEach((url, idx) => {
            const box = document.createElement('div');
            box.className = 'image-upload-box';
            const img = document.createElement('img');
            img.src = url;
            img.alt = '이미지 미리보기';
            box.appendChild(img);

            // 삭제 버튼
            const delBtn = document.createElement('button');
            delBtn.innerText = 'X';
            delBtn.className = 'delete-btn';
            delBtn.addEventListener('click', () => {
                uploadedImageUrls.splice(idx, 1);
                renderPreview();
            });
            box.appendChild(delBtn);

            previewContainer.appendChild(box);
        });

        // 동영상 썸네일
        if (uploadedVideoUrl) {
            const box = document.createElement('div');
            box.className = 'image-upload-box';
            const video = document.createElement('video');
            video.src = uploadedVideoUrl;
            video.controls = true;
            video.width = 60;
            video.height = 60;
            box.appendChild(video);

            const delBtn = document.createElement('button');
            delBtn.innerText = 'X';
            delBtn.className = 'delete-btn';
            delBtn.addEventListener('click', () => {
                uploadedVideoUrl = null;
                renderPreview();
            });
            box.appendChild(delBtn);

            previewContainer.appendChild(box);
        }
    }
}

// 등록하기 버튼 submit 시 URL을 hidden input에 넣어서 전송
document.getElementById('product-form').addEventListener('submit', function(e) {
    // 이미지 URL hidden input 처리
    for (let i = 0; i < 4; i++) {
        let inputName = `img_${i+1}`;
        let input = document.querySelector(`input[name="${inputName}"]`);

        if (!input) {
            input = document.createElement('input');
            input.type = 'hidden';
            input.name = inputName;
            this.appendChild(input);
        }

        input.value = (i < uploadedImageUrls.length) ? uploadedImageUrls[i] : '';
    }

    let videoInput = document.querySelector('input[name="video_url"]');
    if (!videoInput) {
        videoInput = document.createElement('input');
        videoInput.type = 'hidden';
        videoInput.name = 'video_url';
        this.appendChild(videoInput);
    }
    videoInput.value = uploadedVideoUrl || '';

    console.log('폼 제출 전 값들:', {
        images: uploadedImageUrls,
        video: uploadedVideoUrl
    });
});
