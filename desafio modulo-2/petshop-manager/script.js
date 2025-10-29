// ==========================================
// Storage Manager - Manages all in-memory storage operations
// ==========================================
const StorageManager = {
    // In-memory storage (replacing localStorage due to sandbox restrictions)
    clients: [],
    cart: [],
    
    // Get clients from memory
    getClients: function() {
        return this.clients;
    },
    
    // Save clients to memory
    saveClients: function(clients) {
        this.clients = clients;
        return true;
    },
    
    // Add a single client
    addClient: function(client) {
        client.id = Date.now();
        this.clients.push(client);
        return true;
    },
    
    // Get cart from memory
    getCart: function() {
        return this.cart;
    },
    
    // Add product to cart
    addToCart: function(product) {
        this.cart.push({
            id: Date.now(),
            name: product.name,
            price: product.price,
            image: product.image
        });
        return true;
    }
};

// ==========================================
// Navigation Manager
// ==========================================
const NavigationManager = {
    init: function() {
        const navLinks = document.querySelectorAll('.nav-link');
        const btnLinks = document.querySelectorAll('.btn-link');
        const hamburger = document.getElementById('hamburger');
        const navLinksContainer = document.getElementById('navLinks');
        
        // Handle navigation clicks
        navLinks.forEach(link => {
            link.addEventListener('click', (e) => {
                e.preventDefault();
                const page = link.getAttribute('data-page');
                this.navigateTo(page);
                
                // Close mobile menu if open
                navLinksContainer.classList.remove('active');
                hamburger.classList.remove('active');
            });
        });
        
        // Handle button link clicks
        btnLinks.forEach(link => {
            link.addEventListener('click', (e) => {
                e.preventDefault();
                const page = link.getAttribute('data-page');
                this.navigateTo(page);
            });
        });
        
        // Handle hamburger menu
        hamburger.addEventListener('click', () => {
            hamburger.classList.toggle('active');
            navLinksContainer.classList.toggle('active');
        });
    },
    
    navigateTo: function(pageName) {
        // Hide all pages
        const pages = document.querySelectorAll('.page');
        pages.forEach(page => page.classList.remove('active'));
        
        // Show selected page
        const selectedPage = document.getElementById(`page-${pageName}`);
        if (selectedPage) {
            selectedPage.classList.add('active');
        }
        
        // Update active nav link
        const navLinks = document.querySelectorAll('.nav-link');
        navLinks.forEach(link => {
            link.classList.remove('active');
            if (link.getAttribute('data-page') === pageName) {
                link.classList.add('active');
            }
        });
        
        // Load page-specific content
        if (pageName === 'clientes') {
            ClientsPage.loadClients();
        } else if (pageName === 'produtos') {
            ProductsPage.loadProducts();
        }
        
        // Scroll to top
        window.scrollTo({ top: 0, behavior: 'smooth' });
    }
};

// ==========================================
// Form Manager - Handles client registration
// ==========================================
const FormManager = {
    init: function() {
        const form = document.getElementById('clientForm');
        const phoneInput = document.getElementById('tutorPhone');
        
        // Phone mask
        phoneInput.addEventListener('input', (e) => {
            let value = e.target.value.replace(/\D/g, '');
            
            if (value.length <= 11) {
                value = value.replace(/^(\d{2})(\d)/g, '($1) $2');
                value = value.replace(/(\d{5})(\d)/, '$1-$2');
                e.target.value = value;
            } else {
                e.target.value = value.substring(0, 15);
            }
        });
        
        // Form submission
        form.addEventListener('submit', (e) => {
            e.preventDefault();
            
            if (this.validateForm()) {
                this.submitForm();
            }
        });
        
        // Real-time validation
        const inputs = form.querySelectorAll('.form-input');
        inputs.forEach(input => {
            input.addEventListener('blur', () => {
                this.validateField(input);
            });
            
            input.addEventListener('input', () => {
                if (input.classList.contains('error')) {
                    this.validateField(input);
                }
            });
        });
    },
    
    validateField: function(field) {
        const errorElement = document.getElementById(`${field.id}Error`);
        let isValid = true;
        let errorMessage = '';
        
        if (field.hasAttribute('required') && !field.value.trim()) {
            isValid = false;
            errorMessage = 'Este campo é obrigatório';
        } else if (field.type === 'tel' && field.value.trim()) {
            const phone = field.value.replace(/\D/g, '');
            if (phone.length < 10) {
                isValid = false;
                errorMessage = 'Telefone inválido';
            }
        } else if (field.type === 'number' && field.value < 0) {
            isValid = false;
            errorMessage = 'Idade deve ser maior ou igual a zero';
        } else if (field.tagName === 'SELECT' && !field.value) {
            isValid = false;
            errorMessage = 'Selecione uma opção';
        }
        
        if (!isValid) {
            field.classList.add('error');
            errorElement.textContent = errorMessage;
            errorElement.classList.add('visible');
        } else {
            field.classList.remove('error');
            errorElement.textContent = '';
            errorElement.classList.remove('visible');
        }
        
        return isValid;
    },
    
    validateForm: function() {
        const form = document.getElementById('clientForm');
        const inputs = form.querySelectorAll('.form-input');
        let isValid = true;
        
        inputs.forEach(input => {
            if (!this.validateField(input)) {
                isValid = false;
            }
        });
        
        return isValid;
    },
    
    submitForm: function() {
        const formData = {
            tutorName: document.getElementById('tutorName').value.trim(),
            tutorPhone: document.getElementById('tutorPhone').value.trim(),
            tutorAddress: document.getElementById('tutorAddress').value.trim(),
            serviceDate: document.getElementById('serviceDate').value,
            petName: document.getElementById('petName').value.trim(),
            petAge: document.getElementById('petAge').value,
            petSize: document.getElementById('petSize').value
        };
        
        // Save to localStorage
        if (StorageManager.addClient(formData)) {
            this.showSuccessMessage();
            this.resetForm();
        } else {
            alert('Erro ao salvar cliente. Por favor, tente novamente.');
        }
    },
    
    showSuccessMessage: function() {
        const successMessage = document.getElementById('successMessage');
        successMessage.classList.add('visible');
        
        setTimeout(() => {
            successMessage.classList.remove('visible');
        }, 5000);
    },
    
    resetForm: function() {
        const form = document.getElementById('clientForm');
        form.reset();
        
        // Clear all errors
        const errorMessages = form.querySelectorAll('.error-message');
        errorMessages.forEach(error => {
            error.classList.remove('visible');
            error.textContent = '';
        });
        
        const inputs = form.querySelectorAll('.form-input');
        inputs.forEach(input => {
            input.classList.remove('error');
        });
    }
};

// ==========================================
// Clients Page Manager
// ==========================================
const ClientsPage = {
    loadClients: function() {
        const clients = StorageManager.getClients();
        const clientsGrid = document.getElementById('clientsGrid');
        const emptyState = document.getElementById('emptyState');
        
        if (clients.length === 0) {
            clientsGrid.style.display = 'none';
            emptyState.style.display = 'flex';
        } else {
            clientsGrid.style.display = 'grid';
            emptyState.style.display = 'none';
            this.renderClients(clients);
        }
    },
    
    renderClients: function(clients) {
        const clientsGrid = document.getElementById('clientsGrid');
        clientsGrid.innerHTML = '';
        
        clients.forEach(client => {
            const card = this.createClientCard(client);
            clientsGrid.appendChild(card);
        });
    },
    
    createClientCard: function(client) {
        const card = document.createElement('div');
        card.className = 'client-card';
        
        // Format date
        const date = new Date(client.serviceDate + 'T00:00:00');
        const formattedDate = date.toLocaleDateString('pt-BR');
        
        card.innerHTML = `
            <div class="client-card-header">
                <svg class="client-card-icon" width="28" height="28" viewBox="0 0 28 28" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <ellipse cx="8" cy="6" rx="4" ry="5" fill="#F6B93B"/>
                    <ellipse cx="20" cy="6" rx="4" ry="5" fill="#F6B93B"/>
                    <circle cx="14" cy="14" r="9" fill="#1F6764"/>
                    <circle cx="11" cy="13" r="2" fill="#FAFAF8"/>
                    <circle cx="17" cy="13" r="2" fill="#FAFAF8"/>
                    <path d="M10 17 Q14 20 18 17" stroke="#FAFAF8" stroke-width="2" fill="none" stroke-linecap="round"/>
                </svg>
                <h3 class="client-card-name">${client.petName}</h3>
            </div>
            <p class="client-card-date">${formattedDate}</p>
        `;
        
        card.addEventListener('click', () => {
            this.openModal(client);
        });
        
        return card;
    },
    
    openModal: function(client) {
        const modal = document.getElementById('clientModal');
        const modalBody = document.getElementById('modalBody');
        
        // Format date
        const date = new Date(client.serviceDate + 'T00:00:00');
        const formattedDate = date.toLocaleDateString('pt-BR');
        
        modalBody.innerHTML = `
            <div class="modal-field">
                <div class="modal-field-label">Nome do Tutor</div>
                <div class="modal-field-value">${client.tutorName}</div>
            </div>
            <div class="modal-field">
                <div class="modal-field-label">Telefone</div>
                <div class="modal-field-value">${client.tutorPhone}</div>
            </div>
            <div class="modal-field">
                <div class="modal-field-label">Endereço</div>
                <div class="modal-field-value">${client.tutorAddress}</div>
            </div>
            <div class="modal-field">
                <div class="modal-field-label">Nome do Pet</div>
                <div class="modal-field-value">${client.petName}</div>
            </div>
            <div class="modal-field">
                <div class="modal-field-label">Idade</div>
                <div class="modal-field-value">${client.petAge} ${client.petAge == 1 ? 'ano' : 'anos'}</div>
            </div>
            <div class="modal-field">
                <div class="modal-field-label">Porte</div>
                <div class="modal-field-value">${client.petSize}</div>
            </div>
            <div class="modal-field">
                <div class="modal-field-label">Data do Atendimento</div>
                <div class="modal-field-value">${formattedDate}</div>
            </div>
        `;
        
        modal.classList.add('active');
    }
};

// ==========================================
// Products Page Manager
// ==========================================
const ProductsPage = {
    products: [
        { name: 'Ração Premium 15kg', price: 189.90, color: '#F6B93B', emoji: '🥘' },
        { name: 'Coleira Ajustável', price: 45.00, color: '#1F6764', emoji: '⛓️' },
        { name: 'Shampoo Pet', price: 32.50, color: '#A3D5D3', emoji: '🧴' },
        { name: 'Petiscos Naturais', price: 28.90, color: '#F6B93B', emoji: '🦴' },
        { name: 'Caminha Confortável', price: 159.90, color: '#E0E4E3', emoji: '🛏️' },
        { name: 'Brinquedo Interativo', price: 67.00, color: '#1F6764', emoji: '🎾' }
    ],
    
    loadProducts: function() {
        const productsGrid = document.getElementById('productsGrid');
        productsGrid.innerHTML = '';
        
        this.products.forEach(product => {
            const card = this.createProductCard(product);
            productsGrid.appendChild(card);
        });
    },
    
    createProductCard: function(product) {
        const card = document.createElement('div');
        card.className = 'product-card';
        
        const formattedPrice = product.price.toLocaleString('pt-BR', {
            style: 'currency',
            currency: 'BRL'
        });
        
        card.innerHTML = `
            <div class="product-image" style="background-color: ${product.color};">
                <span style="font-size: 5rem;">${product.emoji}</span>
            </div>
            <div class="product-info">
                <h3 class="product-name">${product.name}</h3>
                <p class="product-price">${formattedPrice}</p>
                <button class="btn-add-cart">Adicionar ao Carrinho</button>
            </div>
        `;
        
        const addButton = card.querySelector('.btn-add-cart');
        addButton.addEventListener('click', () => {
            this.addToCart(product, addButton);
        });
        
        return card;
    },
    
    addToCart: function(product, button) {
        // Add to cart in localStorage
        if (StorageManager.addToCart(product)) {
            // Show toast notification
            ToastManager.show('Produto adicionado ao carrinho!');
            
            // Visual feedback on button
            button.classList.add('added');
            button.textContent = 'Adicionado!';
            
            setTimeout(() => {
                button.classList.remove('added');
                button.textContent = 'Adicionar ao Carrinho';
            }, 2000);
        } else {
            ToastManager.show('Erro ao adicionar produto. Tente novamente.');
        }
    }
};

// ==========================================
// Modal Manager
// ==========================================
const ModalManager = {
    init: function() {
        const modal = document.getElementById('clientModal');
        const modalClose = document.getElementById('modalClose');
        const modalOverlay = document.getElementById('modalOverlay');
        
        modalClose.addEventListener('click', () => {
            modal.classList.remove('active');
        });
        
        modalOverlay.addEventListener('click', () => {
            modal.classList.remove('active');
        });
        
        // Close on Escape key
        document.addEventListener('keydown', (e) => {
            if (e.key === 'Escape' && modal.classList.contains('active')) {
                modal.classList.remove('active');
            }
        });
    }
};

// ==========================================
// Toast Manager
// ==========================================
const ToastManager = {
    show: function(message, duration = 3000) {
        const toast = document.getElementById('toast');
        const toastMessage = document.getElementById('toastMessage');
        
        toastMessage.textContent = message;
        toast.classList.add('visible');
        
        setTimeout(() => {
            toast.classList.remove('visible');
        }, duration);
    }
};

// ==========================================
// Initialize Application
// ==========================================
document.addEventListener('DOMContentLoaded', () => {
    NavigationManager.init();
    FormManager.init();
    ModalManager.init();
});