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
    // 수정 모드인지 확인 (URL에서 prodId 파라미터 확인)
    const urlParams = new URLSearchParams(window.location.search);
    const prodId = urlParams.get('prodId');
    let isUpdateMode = prodId ? true : false;

    // 수정 모드일 경우 기존 데이터 불러오기
    if (isUpdateMode) {
        fetch(`/api/products/${prodId}`)
            .then(response => {
                if (!response.ok) throw new Error('상품 정보를 불러올 수 없습니다.');
                return response.json();
            })
            .then(product => {
                // 폼 필드에 기존 데이터 채우기
                document.querySelector('input[name="title"]').value = product.title;
                document.querySelector('input[name="price"]').value = product.price;
                document.querySelector('textarea[name="description"]').value = product.description;

                // 카테고리 선택 상태 복원
                if (product.categoryMax) {
                    document.getElementById('category_max').value = product.categoryMax;
                    // 대분류 버튼 선택 상태 표시
                    document.querySelectorAll('.category-box .category-item').forEach(btn => {
                        if (btn.innerText === product.categoryMax) {
                            btn.classList.add('selected');
                            showSubCategory(product.categoryMax);

                            // 하위 카테고리 선택 (타이밍 문제로 setTimeout 사용)
                            if (product.categoryMin) {
                                document.getElementById('category_min').value = product.categoryMin;
                                setTimeout(() => {
                                    const subButtons = document.querySelectorAll('#sub-category .category-item');
                                    subButtons.forEach(btn => {
                                        if (btn.innerText === product.categoryMin) {
                                            btn.classList.add('selected');
                                        }
                                    });
                                }, 300);
                            }
                        }
                    });
                }

                // 기존 이미지 URL 로드
                if (product.img1) uploadedImageUrls.push(product.img1);
                if (product.img2) uploadedImageUrls.push(product.img2);
                if (product.img3) uploadedImageUrls.push(product.img3);
                if (product.img4) uploadedImageUrls.push(product.img4);

                // 기존 비디오 URL 로드
                if (product.videoUrl) uploadedVideoUrl = product.videoUrl;

                // 미리보기 렌더링
                renderPreview();

                // 수정 모드임을 표시
                document.querySelector('.register-btn').textContent = '수정하기';
            })
            .catch(error => {
                console.error('상품 정보 로드 실패:', error);
                alert('상품 정보를 불러오는데 실패했습니다.');
            });
    }

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
    const videoInput = document.getElementById('video-input');
    const previewContainer = document.getElementById('preview-container');
    const fileCount = document.getElementById('file-count');

    // 클릭 시 파일 선택창 열기
    uploadBox.addEventListener('click', function(e) {
        if (e.target === fileInput || e.target === videoInput) return;
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
}

// 미리보기 렌더링 함수
function renderPreview() {
    const previewContainer = document.getElementById('preview-container');
    const fileCount = document.getElementById('file-count');

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

// 등록하기/수정하기 버튼 submit 시 REST API 호출
document.getElementById('product-form').addEventListener('submit', function(e) {
    e.preventDefault(); // 기본 폼 제출 방지

    // URL에서 prodId 확인
    const urlParams = new URLSearchParams(window.location.search);
    const prodId = urlParams.get('prodId');
    const isUpdateMode = prodId ? true : false;

    // 이미지 URL 준비
    const imageUrls = [];
    for (let i = 0; i < uploadedImageUrls.length && i < 4; i++) {
        imageUrls.push(uploadedImageUrls[i]);
    }

    // 폼 데이터 JSON 객체로 변환
    const productData = {
        title: document.querySelector('input[name="title"]').value,
        categoryMax: document.getElementById('category_max').value,
        categoryMin: document.getElementById('category_min').value,
        price: document.querySelector('input[name="price"]').value,
        description: document.querySelector('textarea[name="description"]').value,
        img1: imageUrls[0] || "",
        img2: imageUrls[1] || "",
        img3: imageUrls[2] || "",
        img4: imageUrls[3] || "",
        videoUrl: uploadedVideoUrl || ""
    };

    // 수정 모드일 경우 prodId 추가
    if (isUpdateMode) {
        productData.prodId = parseInt(prodId);
    }

    // API 호출 (등록 또는 수정)
    fetch(isUpdateMode ? `/api/products/${prodId}` : '/api/products', {
        method: isUpdateMode ? 'PUT' : 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(productData)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('서버 응답 오류: ' + response.status);
            }
            return response.json();
        })
        .then(data => {
            console.log(isUpdateMode ? '상품 수정 성공:' : '상품 등록 성공:', data);
            // 성공 시 상품 상세 또는 목록 페이지로 이동
            window.location.href = isUpdateMode
                ? `/prod/prodDetail?prodId=${prodId}`
                : '/prod/prodList';
        })
        .catch(error => {
            console.error(isUpdateMode ? '상품 수정 실패:' : '상품 등록 실패:', error);
            alert(isUpdateMode ? '상품 수정에 실패했습니다.' : '상품 등록에 실패했습니다. 다시 시도해주세요.');
        });
});
