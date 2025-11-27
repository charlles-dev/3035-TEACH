// Classe para gerenciar o contador de cliques
class ClickCounterApp {
    constructor() {
        // Inicializa o estado
        this.state = {
            clicks: 0
        };

        // Obtém referências aos elementos do DOM
        this.clickButtonElement = document.getElementById('clickButton');
        this.resetButtonElement = document.getElementById('resetButton');
        this.clickCountElement = document.getElementById('clickCount');

        // Verifica se os elementos foram encontrados
        if (!this.clickButtonElement || !this.clickCountElement || !this.resetButtonElement) {
            console.error('Elementos necessários não foram encontrados no DOM');
            return;
        }

        // Inicializa os event listeners
        this.initializeEventListeners();
    }

    /**
     * Inicializa os listeners de eventos para os botões
     */
    initializeEventListeners() {
        this.clickButtonElement?.addEventListener('click', () => this.handleClick());
        this.resetButtonElement?.addEventListener('click', () => this.handleReset());
    }

    /**
     * Incrementa o contador de cliques e atualiza a exibição
     */
    handleClick() {
        this.state.clicks++;
        this.updateDisplay();
        console.log(`Clique #${this.state.clicks}`);
    }

    /**
     * Reseta o contador para zero
     */
    handleReset() {
        this.state.clicks = 0;
        this.updateDisplay();
        console.log('Contador resetado');
    }

    /**
     * Atualiza a exibição do contador no DOM
     */
    updateDisplay() {
        if (this.clickCountElement) {
            this.clickCountElement.textContent = this.state.clicks.toString();
        }
    }

    /**
     * Retorna o estado atual do contador
     */
    getState() {
        return this.state;
    }
}

// Inicializa a aplicação quando o DOM estiver pronto
document.addEventListener('DOMContentLoaded', () => {
    new ClickCounterApp();
    console.log('Aplicação de Contador de Cliques iniciada');
});
