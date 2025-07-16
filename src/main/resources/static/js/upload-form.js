// Upload Form Handler
class UploadForm {
    constructor() {
        this.fileInput = document.getElementById('fileInput');
        this.uploadArea = document.getElementById('uploadArea');
        this.uploadPlaceholder = document.getElementById('uploadPlaceholder');
        this.previewArea = document.getElementById('previewArea');
        this.submitBtn = document.getElementById('submitBtn');
        this.selectedCarPart = document.getElementById('selectedCarPart');
        this.uploadForm = document.getElementById('uploadForm');

        this.files = [];
        this.maxFiles = 5;

        this.init();
    }

    init() {
        this.setupEventListeners();
        this.updateSubmitButton();
    }

    setupEventListeners() {
        // File input change
        if (this.fileInput) {
            this.fileInput.addEventListener('change', (e) => {
                this.handleFiles(e.target.files);
            });
        }

        // Drag and drop
        if (this.uploadArea) {
            this.uploadArea.addEventListener('dragover', (e) => {
                e.preventDefault();
                this.uploadArea.classList.add('drag-over');
            });

            this.uploadArea.addEventListener('dragleave', (e) => {
                e.preventDefault();
                this.uploadArea.classList.remove('drag-over');
            });

            this.uploadArea.addEventListener('drop', (e) => {
                e.preventDefault();
                this.uploadArea.classList.remove('drag-over');
                this.handleFiles(e.dataTransfer.files);
            });
        }

        // Part selection change
        if (this.selectedCarPart) {
            this.selectedCarPart.addEventListener('change', () => {
                this.updateSubmitButton();
            });
        }

        // Form submit
        if (this.uploadForm) {
            this.uploadForm.addEventListener('submit', (e) => {
                this.handleSubmit(e);
            });
        }
    }

    handleFiles(newFiles) {
        const fileArray = Array.from(newFiles);

        // Filter image files only
        const imageFiles = fileArray.filter(file =>
            file.type.startsWith('image/')
        );

        if (imageFiles.length === 0) {
            this.showToast('이미지 파일만 업로드 가능합니다.', 'error');
            return;
        }

        // Check file limit
        const totalFiles = this.files.length + imageFiles.length;
        if (totalFiles > this.maxFiles) {
            this.showToast(`최대 ${this.maxFiles}장까지 업로드 가능합니다.`, 'warning');
            return;
        }

        // Add files to array
        imageFiles.forEach(file => {
            const fileData = {
                file: file,
                id: Date.now() + Math.random(),
                url: URL.createObjectURL(file)
            };
            this.files.push(fileData);
        });

        this.updatePreview();
        this.updateSubmitButton();
    }

    removeFile(fileId) {
        const index = this.files.findIndex(f => f.id === fileId);
        if (index > -1) {
            URL.revokeObjectURL(this.files[index].url);
            this.files.splice(index, 1);
            this.updatePreview();
            this.updateSubmitButton();
        }
    }

    updatePreview() {
        if (this.files.length === 0) {
            if (this.uploadPlaceholder) this.uploadPlaceholder.style.display = 'block';
            if (this.previewArea) this.previewArea.style.display = 'none';
            return;
        }

        if (this.uploadPlaceholder) this.uploadPlaceholder.style.display = 'none';
        if (this.previewArea) {
            this.previewArea.style.display = 'grid';
            this.previewArea.innerHTML = this.files.map(fileData => `
                <div class="preview-item">
                    <img src="${fileData.url}" alt="Preview" class="preview-image">
                    <button type="button" class="preview-remove" onclick="uploadForm.removeFile(${fileData.id})">
                        ×
                    </button>
                </div>
            `).join('');
        }
    }

    updateSubmitButton() {
        if (!this.submitBtn) return;

        const hasFiles = this.files.length > 0;
        const hasPartSelected = this.selectedCarPart ? this.selectedCarPart.value !== '' : false;

        this.submitBtn.disabled = !(hasFiles && hasPartSelected);

        if (hasFiles && hasPartSelected) {
            this.submitBtn.classList.remove('btn-disabled');
        } else {
            this.submitBtn.classList.add('btn-disabled');
        }
    }

    handleSubmit(e) {
        e.preventDefault();

        if (this.files.length === 0) {
            this.showToast('최소 1장의 사진을 업로드해주세요.', 'error');
            return;
        }

        if (!this.selectedCarPart || !this.selectedCarPart.value) {
            this.showToast('손상 부위를 선택해주세요.', 'error');
            return;
        }

        // Show loading state
        this.submitBtn.disabled = true;
        this.submitBtn.innerHTML = `
            <div class="loading-spinner"></div>
            분석 중...
        `;

        // Create FormData
        const formData = new FormData();
        formData.append('selectedCarPart', this.selectedCarPart.value);

        this.files.forEach(fileData => {
            formData.append('files', fileData.file);
        });

        // Submit form
        fetch('/upload', {
            method: 'POST',
            body: formData
        })
        .then(response => {
            if (response.ok) {
                return response.text();
            }
            throw new Error('업로드에 실패했습니다.');
        })
        .then(html => {
            // Replace current page with result
            document.open();
            document.write(html);
            document.close();
        })
        .catch(error => {
            console.error('Error:', error);
            this.showToast('업로드 중 오류가 발생했습니다.', 'error');
            this.resetSubmitButton();
        });
    }

    resetSubmitButton() {
        if (!this.submitBtn) return;

        this.submitBtn.disabled = false;
        this.submitBtn.innerHTML = `
            <svg width="20" height="20" fill="currentColor" viewBox="0 0 24 24">
                <path d="M9,20.42L2.79,14.21L5.62,11.38L9,14.77L18.88,4.88L21.71,7.71L9,20.42Z"/>
            </svg>
            견적 분석 시작
        `;
        this.updateSubmitButton();
    }

    showToast(message, type = 'info') {
        // Simple toast notification
        const toast = document.createElement('div');
        toast.className = `toast toast-${type}`;
        toast.textContent = message;

        // Add toast styles
        Object.assign(toast.style, {
            position: 'fixed',
            top: '20px',
            right: '20px',
            padding: '12px 16px',
            borderRadius: '8px',
            color: 'white',
            fontSize: '14px',
            fontWeight: '500',
            zIndex: '1000',
            opacity: '0',
            transform: 'translateY(-20px)',
            transition: 'all 0.3s ease'
        });

        // Set color based on type
        const colors = {
            success: '#059669',
            error: '#dc2626',
            warning: '#d97706',
            info: '#4f46e5'
        };
        toast.style.backgroundColor = colors[type] || colors.info;

        document.body.appendChild(toast);

        // Animate in
        setTimeout(() => {
            toast.style.opacity = '1';
            toast.style.transform = 'translateY(0)';
        }, 100);

        // Animate out and remove
        setTimeout(() => {
            toast.style.opacity = '0';
            toast.style.transform = 'translateY(-20px)';
            setTimeout(() => {
                if (document.body.contains(toast)) {
                    document.body.removeChild(toast);
                }
            }, 300);
        }, 3000);
    }
}

// Initialize when DOM is ready
document.addEventListener('DOMContentLoaded', () => {
    // Add necessary CSS for loading spinner and disabled button
    const style = document.createElement('style');
    style.textContent = `
        .loading-spinner {
            width: 16px;
            height: 16px;
            border: 2px solid transparent;
            border-top: 2px solid currentColor;
            border-radius: 50%;
            animation: spin 1s linear infinite;
            display: inline-block;
            margin-right: 8px;
        }
        
        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }
        
        .btn-disabled {
            opacity: 0.5;
            cursor: not-allowed;
        }
    `;
    document.head.appendChild(style);

    // Initialize upload form
    window.uploadForm = new UploadForm();
});

