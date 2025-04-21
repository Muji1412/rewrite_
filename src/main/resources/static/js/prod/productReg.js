const categoryMap = {
    '의류': ['여성의류', '남성의류'],
    '게임': ['PC게임', '콘솔'],
    '전자기기': ['PC', '모바일'],
    '가전제품': ['대형가전', '소형가전']
};

document.addEventListener('DOMContentLoaded', function() {
    const mainCategoryBtns = document.querySelectorAll('.category-box .category-item');
    mainCategoryBtns.forEach(btn => {
        btn.addEventListener('click', function() {
            mainCategoryBtns.forEach(b => b.classList.remove('selected'));
            this.classList.add('selected');
            showSubCategory(this.innerText);
        });
    });
});

function showSubCategory(mainCategory) {
    const subDiv = document.getElementById('sub-category');
    subDiv.innerHTML = ''; // 기존 중분류 버튼 지우기

    const subs = categoryMap[mainCategory];
    if (subs.length === 0) {
        subDiv.innerHTML = '<span>중분류 없음</span>';
        return;
    }
    subs.forEach(sub => {
        const btn = document.createElement('button');
        btn.innerText = sub;
        btn.type = "button";
        btn.className = 'category-item';
        btn.style.display = 'block';

        btn.addEventListener('click', function(event) {
            event.stopPropagation(); // ★ 이벤트 버블링 방지
            const allBtns = subDiv.querySelectorAll('.category-item');
            allBtns.forEach(b => b.classList.remove('selected'));
            this.classList.add('selected');
        });

        subDiv.appendChild(btn);
    });
}