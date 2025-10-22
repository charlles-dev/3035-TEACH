        // Calculator state
        const state = {
            currentInput: '0',
            previousInput: '',
            operation: null,
            shouldResetDisplay: false,
            memory: 0,
            history: [],
            isScientificMode: false,
            isPowerMode: false,
            expression: '',
            lastResult: 0
        };

        // DOM elements
        const display = document.getElementById('display');
        const expressionDisplay = document.getElementById('expression');
        const memoryIndicator = document.getElementById('memoryIndicator');
        const historyList = document.getElementById('historyList');
        const calculator = document.getElementById('calculator');
        const themeToggle = document.getElementById('themeToggle');
        const tooltip = document.getElementById('tooltip');

        // Update display
        function updateDisplay() {
            display.textContent = formatNumber(state.currentInput);
            expressionDisplay.textContent = state.expression;
            
            // Update memory indicator
            if (state.memory !== 0) {
                memoryIndicator.classList.add('active');
            } else {
                memoryIndicator.classList.remove('active');
            }
            
            // Add pulse animation
            display.classList.add('pulse');
            setTimeout(() => display.classList.remove('pulse'), 300);
        }

        // Format number with thousand separators
        function formatNumber(num) {
            if (num === 'Erro' || num === 'Infinity' || num === '-Infinity') {
                return num;
            }
            
            const number = parseFloat(num);
            if (isNaN(number)) return num;
            
            // Handle very large or small numbers
            if (Math.abs(number) > 999999999 || (Math.abs(number) < 0.000001 && number !== 0)) {
                return number.toExponential(6);
            }
            
            // Regular formatting
            const parts = num.toString().split('.');
            parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, '.');
            return parts.join('.');
        }

        // Clear functions
        function clearAll() {
            state.currentInput = '0';
            state.previousInput = '';
            state.operation = null;
            state.expression = '';
            state.isPowerMode = false;
            clearOperationIndicators();
            updateDisplay();
        }

        function clearEntry() {
            state.currentInput = '0';
            updateDisplay();
        }

        function backspace() {
            if (state.currentInput.length > 1) {
                state.currentInput = state.currentInput.slice(0, -1);
            } else {
                state.currentInput = '0';
            }
            updateDisplay();
        }

        // Clear operation indicators
        function clearOperationIndicators() {
            document.querySelectorAll('.operator-btn').forEach(btn => {
                btn.classList.remove('operation-active');
            });
        }

        // Memory functions
        function memoryClear() {
            state.memory = 0;
            updateDisplay();
            showTooltip('Memória limpa', event.target);
        }

        function memoryRecall() {
            state.currentInput = state.memory.toString();
            updateDisplay();
            showTooltip('Memória recuperada', event.target);
        }

        function memoryAdd() {
            state.memory += parseFloat(state.currentInput);
            updateDisplay();
            showTooltip('Adicionado à memória', event.target);
        }

        function memorySubtract() {
            state.memory -= parseFloat(state.currentInput);
            updateDisplay();
            showTooltip('Subtraído da memória', event.target);
        }

        function memoryStore() {
            state.memory = parseFloat(state.currentInput);
            updateDisplay();
            showTooltip('Armazenado na memória', event.target);
        }

        function memorySwap() {
            const temp = state.currentInput;
            state.currentInput = state.memory.toString();
            state.memory = parseFloat(temp);
            updateDisplay();
            showTooltip('Memória trocada', event.target);
        }

        // Input functions
        function inputNumber(num) {
            if (state.shouldResetDisplay) {
                state.currentInput = '0';
                state.shouldResetDisplay = false;
            }
            
            if (state.currentInput === '0') {
                state.currentInput = num;
            } else if (state.currentInput.length < 16) {
                state.currentInput += num;
            }
            
            updateDisplay();
        }

        function inputDecimal() {
            if (state.shouldResetDisplay) {
                state.currentInput = '0';
                state.shouldResetDisplay = false;
            }
            
            if (!state.currentInput.includes('.')) {
                state.currentInput += '.';
            }
            
            updateDisplay();
        }

        function inputPercent() {
            const current = parseFloat(state.currentInput);
            if (state.operation && state.previousInput) {
                const previous = parseFloat(state.previousInput);
                switch (state.operation) {
                    case '+':
                    case '−':
                        state.currentInput = (previous * current / 100).toString();
                        break;
                    case '×':
                    case '÷':
                        state.currentInput = (current / 100).toString();
                        break;
                }
            } else {
                state.currentInput = (current / 100).toString();
            }
            updateDisplay();
        }

        // Scientific functions
        function scientificFunction(func) {
            const current = parseFloat(state.currentInput);
            let result;
            
            switch (func) {
                case 'sqrt':
                    result = Math.sqrt(current);
                    break;
                case 'square':
                    result = current * current;
                    break;
                case 'cube':
                    result = current * current * current;
                    break;
                case 'power':
                    state.isPowerMode = true;
                    state.previousInput = state.currentInput;
                    state.operation = '^';
                    state.expression = `${state.currentInput}^`;
                    state.shouldResetDisplay = true;
                    updateDisplay();
                    return;
                case 'reciprocal':
                    result = 1 / current;
                    break;
                case 'log':
                    result = Math.log10(current);
                    break;
                case 'ln':
                    result = Math.log(current);
                    break;
                case 'sin':
                    result = Math.sin(current * Math.PI / 180);
                    break;
                case 'cos':
                    result = Math.cos(current * Math.PI / 180);
                    break;
                case 'tan':
                    result = Math.tan(current * Math.PI / 180);
                    break;
                case 'factorial':
                    result = factorial(Math.floor(current));
                    break;
                case 'pi':
                    state.currentInput = Math.PI.toString();
                    updateDisplay();
                    return;
                case 'e':
                    state.currentInput = Math.E.toString();
                    updateDisplay();
                    return;
                case 'abs':
                    result = Math.abs(current);
                    break;
                case 'negate':
                    state.currentInput = (-current).toString();
                    updateDisplay();
                    return;
            }
            
            if (!isNaN(result) && isFinite(result)) {
                addToHistory(`${func}(${state.currentInput})`, result.toString());
                state.currentInput = result.toString();
                state.shouldResetDisplay = true;
            } else {
                state.currentInput = 'Erro';
            }
            updateDisplay();
        }

        function factorial(n) {
            if (n < 0) return NaN;
            if (n === 0 || n === 1) return 1;
            let result = 1;
            for (let i = 2; i <= n; i++) {
                result *= i;
            }
            return result;
        }

        // Operation functions
        function setOperation(op, buttonElement) {
            clearOperationIndicators();
            
            if (state.operation !== null && !state.shouldResetDisplay) {
                calculate();
            }
            
            state.previousInput = state.currentInput;
            state.operation = op;
            state.expression = `${state.previousInput} ${op}`;
            state.shouldResetDisplay = true;
            
            // Highlight active operation
            if (buttonElement) {
                buttonElement.classList.add('operation-active');
            }
            
            updateDisplay();
        }

        function calculate() {
            if (state.operation === null || state.shouldResetDisplay && !state.isPowerMode) return;
            
            let result;
            const prev = parseFloat(state.previousInput);
            const current = parseFloat(state.currentInput);
            
            switch (state.operation) {
                case '+':
                    result = prev + current;
                    break;
                case '−':
                    result = prev - current;
                    break;
                case '×':
                    result = prev * current;
                    break;
                case '÷':
                    if (current === 0) {
                        state.currentInput = 'Erro';
                        updateDisplay();
                        return;
                    }
                    result = prev / current;
                    break;
                case '^':
                    result = Math.pow(prev, current);
                    state.isPowerMode = false;
                    break;
                default:
                    return;
            }
            
            const fullExpression = `${state.previousInput} ${state.operation} ${state.currentInput}`;
            addToHistory(fullExpression, result.toString());
            
            state.currentInput = result.toString();
            state.operation = null;
            state.expression = '';
            state.shouldResetDisplay = true;
            state.lastResult = result;
            clearOperationIndicators();
            updateDisplay();
        }

        // History functions
        function addToHistory(expression, result) {
            const historyItem = {
                expression: expression,
                result: result,
                timestamp: new Date().toLocaleTimeString()
            };
            
            state.history.unshift(historyItem);
            if (state.history.length > 50) {
                state.history.pop();
            }
            
            renderHistory();
        }

        function renderHistory() {
            historyList.innerHTML = '';
            
            state.history.forEach(item => {
                const historyElement = document.createElement('div');
                historyElement.className = 'history-item';
                historyElement.innerHTML = `
                    <div class="history-expression">${item.expression}</div>
                    <div class="history-result">= ${formatNumber(item.result)}</div>
                `;
                historyElement.addEventListener('click', () => {
                    state.currentInput = item.result;
                    updateDisplay();
                });
                historyList.appendChild(historyElement);
            });
        }

        function clearHistory() {
            state.history = [];
            renderHistory();
            showTooltip('Histórico limpo', event.target);
        }

        // Theme toggle
        function toggleTheme() {
            document.body.classList.toggle('light-theme');
            const isLight = document.body.classList.contains('light-theme');
            themeToggle.textContent = isLight ? '☀️' : '🌙';
            localStorage.setItem('theme', isLight ? 'light' : 'dark');
        }

        // Mode toggle
        function toggleMode(mode) {
            const modeBtns = document.querySelectorAll('.mode-btn');
            modeBtns.forEach(btn => {
                btn.classList.toggle('active', btn.dataset.mode === mode);
            });
            
            calculator.classList.toggle('scientific-mode', mode === 'scientific');
            state.isScientificMode = mode === 'scientific';
        }

        // Tooltip
        function showTooltip(text, element) {
            const rect = element.getBoundingClientRect();
            tooltip.textContent = text;
            tooltip.style.left = rect.left + rect.width / 2 - tooltip.offsetWidth / 2 + 'px';
            tooltip.style.top = rect.top - tooltip.offsetHeight - 10 + 'px';
            tooltip.classList.add('show');
            
            setTimeout(() => {
                tooltip.classList.remove('show');
            }, 2000);
        }

        // Copy to clipboard
        function copyToClipboard() {
            navigator.clipboard.writeText(state.currentInput);
            showTooltip('Copiado!', display);
        }

        // Paste from clipboard
        async function pasteFromClipboard() {
            try {
                const text = await navigator.clipboard.readText();
                const num = parseFloat(text);
                if (!isNaN(num)) {
                    state.currentInput = num.toString();
                    updateDisplay();
                }
            } catch (err) {
                console.error('Failed to paste:', err);
            }
        }

        // Event listeners for numbers
        document.getElementById('zero').addEventListener('click', () => inputNumber('0'));
        document.getElementById('one').addEventListener('click', () => inputNumber('1'));
        document.getElementById('two').addEventListener('click', () => inputNumber('2'));
        document.getElementById('three').addEventListener('click', () => inputNumber('3'));
        document.getElementById('four').addEventListener('click', () => inputNumber('4'));
        document.getElementById('five').addEventListener('click', () => inputNumber('5'));
        document.getElementById('six').addEventListener('click', () => inputNumber('6'));
        document.getElementById('seven').addEventListener('click', () => inputNumber('7'));
        document.getElementById('eight').addEventListener('click', () => inputNumber('8'));
        document.getElementById('nine').addEventListener('click', () => inputNumber('9'));
        document.getElementById('decimal').addEventListener('click', inputDecimal);
        
        // Event listeners for operations
        document.getElementById('add').addEventListener('click', (e) => setOperation('+', e.target));
        document.getElementById('subtract').addEventListener('click', (e) => setOperation('−', e.target));
        document.getElementById('multiply').addEventListener('click', (e) => setOperation('×', e.target));
        document.getElementById('divide').addEventListener('click', (e) => setOperation('÷', e.target));
        document.getElementById('equals').addEventListener('click', calculate);
        
        // Event listeners for clear functions
        document.getElementById('c').addEventListener('click', clearAll);
        document.getElementById('ce').addEventListener('click', clearEntry);
        document.getElementById('backspace').addEventListener('click', backspace);
        document.getElementById('percent').addEventListener('click', inputPercent);
        
        // Event listeners for memory functions
        document.getElementById('mc').addEventListener('click', memoryClear);
        document.getElementById('mr').addEventListener('click', memoryRecall);
        document.getElementById('mplus').addEventListener('click', memoryAdd);
        document.getElementById('mminus').addEventListener('click', memorySubtract);
        document.getElementById('ms').addEventListener('click', memoryStore);
        document.getElementById('m-').addEventListener('click', memorySwap);
        
        // Event listeners for scientific functions
        document.querySelectorAll('.scientific-btn').forEach(btn => {
            btn.addEventListener('click', () => scientificFunction(btn.dataset.func));
        });
        
        // Event listeners for mode and theme
        document.querySelectorAll('.mode-btn').forEach(btn => {
            btn.addEventListener('click', () => toggleMode(btn.dataset.mode));
        });
        
        themeToggle.addEventListener('click', toggleTheme);
        document.getElementById('clearHistory').addEventListener('click', clearHistory);
        
        // Copy/Paste with Ctrl+C/Ctrl+V
        display.addEventListener('dblclick', copyToClipboard);
        
        // Keyboard support
        document.addEventListener('keydown', (e) => {
            if (e.ctrlKey || e.metaKey) {
                switch(e.key) {
                    case 'c':
                        e.preventDefault();
                        copyToClipboard();
                        break;
                    case 'v':
                        e.preventDefault();
                        pasteFromClipboard();
                        break;
                    case 'm':
                        e.preventDefault();
                        memoryStore();
                        break;
                }
                return;
            }
            
            if (e.key >= '0' && e.key <= '9') {
                inputNumber(e.key);
            } else if (e.key === '.') {
                inputDecimal();
            } else if (e.key === '+' || e.key === '-' || e.key === '*' || e.key === '/') {
                const opMap = {
                    '+': '+',
                    '-': '−',
                    '*': '×',
                    '/': '÷'
                };
                const buttonMap = {
                    '+': 'add',
                    '-': 'subtract',
                    '*': 'multiply',
                    '/': 'divide'
                };
                const buttonElement = document.getElementById(buttonMap[e.key]);
                setOperation(opMap[e.key], buttonElement);
            } else if (e.key === 'Enter' || e.key === '=') {
                calculate();
            } else if (e.key === 'Escape') {
                clearAll();
            } else if (e.key === 'Backspace') {
                backspace();
            } else if (e.key === 'Delete') {
                clearEntry();
            } else if (e.key === '%') {
                inputPercent();
            } else if (e.key === '^') {
                scientificFunction('power');
            }
        });
        
        // Initialize theme from localStorage
        const savedTheme = localStorage.getItem('theme');
        if (savedTheme === 'light') {
            document.body.classList.add('light-theme');
            themeToggle.textContent = '☀️';
        }
        
        // Initialize display
        updateDisplay();