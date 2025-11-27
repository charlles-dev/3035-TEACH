// Interface para tipar o estado do contador
interface CounterState {
    clicks: number;
}

// Classe para gerenciar o contador de cliques
class ClickCounter {
    private state: CounterState;
    private clickButtonElement: HTMLButtonElement | null;
    private resetButtonElement: HTMLButtonElement | null;
    private clickCountElement: HTMLSpanElement | null;

    constructor() {
        // Inicializa o estado
        this.state = {
            clicks: 0
        };

        // Obtém referências aos elementos do DOM
        this.clickButtonElement = document.getElementById('clickButton') as HTMLButtonElement;
        this.resetButtonElement = document.getElementById('resetButton') as HTMLButtonElement;
        this.clickCountElement = document.getElementById('clickCount') as HTMLSpanElement;

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
    private initializeEventListeners(): void {
        this.clickButtonElement?.addEventListener('click', () => this.handleClick());
        this.resetButtonElement?.addEventListener('click', () => this.handleReset());
    }

    /**
     * Incrementa o contador de cliques e atualiza a exibição
     */
    private handleClick(): void {
        this.state.clicks++;
        this.updateDisplay();
        console.log(`Clique #${this.state.clicks}`);
    }

    /**
     * Reseta o contador para zero
     */
    private handleReset(): void {
        this.state.clicks = 0;
        this.updateDisplay();
        console.log('Contador resetado');
    }

    /**
     * Atualiza a exibição do contador no DOM
     */
    private updateDisplay(): void {
        if (this.clickCountElement) {
            this.clickCountElement.textContent = this.state.clicks.toString();
        }
    }

    /**
     * Retorna o estado atual do contador
     */
    public getState(): CounterState {
        return this.state;
    }
}

// Inicializa a aplicação quando o DOM estiver pronto
document.addEventListener('DOMContentLoaded', () => {
    new ClickCounter();
    console.log('Aplicação de Contador de Cliques iniciada');
});
