// 카센터 관련 JavaScript 기능

class ServiceCenterManager {
    constructor() {
        this.currentSort = 'rating';
        this.userLocation = null;
        this.isLoading = false;
        this.init();
    }

    init() {
        this.bindEvents();
        this.initializeCards();
    }

    bindEvents() {
        // 정렬 버튼 이벤트
        document.addEventListener('click', (e) => {
            if (e.target.classList.contains('sort-btn')) {
                this.handleSortClick(e.target);
            }
        });

        // 카센터 카드 클릭 이벤트
        document.addEventListener('click', (e) => {
            const card = e.target.closest('.service-center-card');
            if (card && card.dataset.centerId) {
                this.navigateToDetail(card.dataset.centerId);
            }
        });

        // 위치 권한 요청 시 처리
        this.requestLocationPermission();
    }

    initializeCards() {
        // 카드에 애니메이션 효과 추가
        const cards = document.querySelectorAll('.service-center-card');
        cards.forEach((card, index) => {
            card.style.animationDelay = `${index * 0.1}s`;
            card.classList.add('fade-in');
        });
    }

    async handleSortClick(button) {
        if (this.isLoading) return;

        const sortBy = button.dataset.sort;
        if (this.currentSort === sortBy) return;

        // 버튼 상태 업데이트
        this.updateSortButtons(button);

        // 정렬 실행
        await this.sortCenters(sortBy);
    }

    updateSortButtons(activeButton) {
        // 모든 버튼의 active 클래스 제거
        document.querySelectorAll('.sort-btn').forEach(btn => {
            btn.classList.remove('active');
            btn.classList.remove('loading');
        });

        // 클릭된 버튼 활성화 및 로딩 표시
        activeButton.classList.add('active', 'loading');
    }

    async sortCenters(sortBy) {
        try {
            this.isLoading = true;
            this.currentSort = sortBy;

            let url = `/center/list?sortBy=${sortBy}`;

            // 거리순 정렬인 경우 위치 정보 추가
            if (sortBy === 'distance') {
                const location = await this.getUserLocation();
                if (location) {
                    url += `&userLat=${location.latitude}&userLng=${location.longitude}`;
                    this.userLocation = location;
                } else {
                    // 위치 정보를 가져올 수 없는 경우 평점순으로 변경
                    this.showLocationError();
                    url = '/center/list?sortBy=rating';
                    this.currentSort = 'rating';
                }
            }

            const response = await fetch(url);
            if (!response.ok) throw new Error('Network response was not ok');

            const centers = await response.json();
            this.renderServiceCenters(centers);

        } catch (error) {
            console.error('Error loading service centers:', error);
            this.showError('카센터 정보를 불러오는데 실패했습니다.');
        } finally {
            this.isLoading = false;
            // 로딩 상태 제거
            document.querySelectorAll('.sort-btn').forEach(btn => {
                btn.classList.remove('loading');
            });
        }
    }

    async getUserLocation() {
        return new Promise((resolve) => {
            if (!navigator.geolocation) {
                resolve(null);
                return;
            }

            navigator.geolocation.getCurrentPosition(
                (position) => {
                    resolve({
                        latitude: position.coords.latitude,
                        longitude: position.coords.longitude
                    });
                },
                () => {
                    resolve(null);
                },
                { timeout: 10000, maximumAge: 300000 } // 5분 캐시
            );
        });
    }

    renderServiceCenters(centers) {
        const grid = document.getElementById('serviceCenterGrid');
        if (!grid) return;

        if (centers.length === 0) {
            this.renderEmptyState(grid);
            return;
        }

        grid.innerHTML = '';

        centers.forEach((center, index) => {
            const card = this.createServiceCenterCard(center);
            card.style.animationDelay = `${index * 0.1}s`;
            grid.appendChild(card);
        });
    }

    createServiceCenterCard(center) {
        const card = document.createElement('div');
        card.className = 'service-center-card fade-in';
        card.dataset.centerId = center.id;

        // 거리 정보 계산
        let distanceHtml = '';
        if (this.currentSort === 'distance' && this.userLocation) {
            const distance = this.calculateDistance(
                this.userLocation.latitude,
                this.userLocation.longitude,
                center.latitude,
                center.longitude
            );
            distanceHtml = `<div class="distance-info">${distance}km</div>`;
        }

        // 별점 표시
        const stars = this.generateStarRating(center.rating);

        // 리뷰 개수
        const reviewCount = center.reviews ? center.reviews.length : 0;

        card.innerHTML = `
            ${distanceHtml}
            <div class="center-name">${center.name}</div>
            <div class="center-rating">
                <span class="star-rating">${stars}</span>
                <span class="rating-number">${center.rating}</span>
                <span class="review-count">(${reviewCount}개 리뷰)</span>
            </div>
            <div class="center-info">
                <span class="icon">📍</span>
                <span>${center.address}</span>
            </div>
            <div class="center-info">
                <span class="icon">📞</span>
                <span>${center.phoneNumber}</span>
            </div>
            <div class="center-info">
                <span class="icon">🕒</span>
                <span>${center.operatingHours}</span>
            </div>
            ${center.description ? `<div class="center-description">${center.description}</div>` : ''}
        `;

        return card;
    }

    generateStarRating(rating) {
        const fullStars = Math.floor(rating);
        const hasHalfStar = rating % 1 >= 0.5;
        const emptyStars = 5 - fullStars - (hasHalfStar ? 1 : 0);

        return '★'.repeat(fullStars) +
               (hasHalfStar ? '☆' : '') +
               '☆'.repeat(emptyStars);
    }

    calculateDistance(lat1, lng1, lat2, lng2) {
        const R = 6371; // 지구 반지름 (km)
        const dLat = this.toRadians(lat2 - lat1);
        const dLng = this.toRadians(lng2 - lng1);

        const a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(this.toRadians(lat1)) * Math.cos(this.toRadians(lat2)) *
                Math.sin(dLng / 2) * Math.sin(dLng / 2);

        const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        const distance = R * c;

        return distance.toFixed(1);
    }

    toRadians(degrees) {
        return degrees * (Math.PI / 180);
    }

    renderEmptyState(container) {
        container.innerHTML = `
            <div class="empty-state">
                <div class="icon">🏢</div>
                <h3>카센터 정보가 없습니다</h3>
                <p>현재 조건에 맞는 카센터를 찾을 수 없습니다.</p>
            </div>
        `;
    }

    navigateToDetail(centerId) {
        window.location.href = `/center/${centerId}`;
    }

    showLocationError() {
        this.showNotification('위치 정보를 가져올 수 없어 평점순으로 정렬합니다.', 'warning');
        // 평점순 버튼을 다시 활성화
        setTimeout(() => {
            const ratingBtn = document.querySelector('[data-sort="rating"]');
            if (ratingBtn) {
                ratingBtn.classList.add('active');
            }
            const distanceBtn = document.querySelector('[data-sort="distance"]');
            if (distanceBtn) {
                distanceBtn.classList.remove('active');
            }
        }, 100);
    }

    showError(message) {
        this.showNotification(message, 'error');
    }

    showNotification(message, type = 'info') {
        // 간단한 토스트 알림 구현
        const notification = document.createElement('div');
        notification.className = `notification notification-${type}`;
        notification.textContent = message;

        // 스타일 설정
        Object.assign(notification.style, {
            position: 'fixed',
            top: '20px',
            right: '20px',
            padding: '12px 20px',
            borderRadius: '6px',
            color: 'white',
            backgroundColor: type === 'error' ? '#dc3545' : type === 'warning' ? '#ffc107' : '#007bff',
            zIndex: '9999',
            transition: 'all 0.3s ease',
            transform: 'translateX(100%)',
            opacity: '0'
        });

        document.body.appendChild(notification);

        // 애니메이션
        setTimeout(() => {
            notification.style.transform = 'translateX(0)';
            notification.style.opacity = '1';
        }, 100);

        // 3초 후 제거
        setTimeout(() => {
            notification.style.transform = 'translateX(100%)';
            notification.style.opacity = '0';
            setTimeout(() => {
                if (notification.parentNode) {
                    notification.parentNode.removeChild(notification);
                }
            }, 300);
        }, 3000);
    }

    requestLocationPermission() {
        // 거리순 버튼 클릭 시 위치 권한이 필요함을 사용자에게 알림
        const distanceBtn = document.querySelector('[data-sort="distance"]');
        if (distanceBtn) {
            distanceBtn.addEventListener('mouseenter', () => {
                if (!this.userLocation) {
                    distanceBtn.title = '거리순 정렬을 위해 위치 권한이 필요합니다';
                }
            });
        }
    }
}

// DOM이 로드되면 ServiceCenterManager 초기화
document.addEventListener('DOMContentLoaded', () => {
    window.serviceCenterManager = new ServiceCenterManager();
});

// 전역 함수로 내보내기 (기존 코드와의 호환성을 위해)
window.sortCenters = function(sortBy) {
    const button = document.querySelector(`[data-sort="${sortBy}"]`);
    if (button && window.serviceCenterManager) {
        window.serviceCenterManager.handleSortClick(button);
    }
};
