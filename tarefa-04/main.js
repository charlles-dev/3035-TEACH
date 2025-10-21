        const colorBox = document.getElementById('color-box');
        const colorCodeSpan = colorBox.querySelector('.color-code');
        const changeColorBtn = document.getElementById('change-color-btn');
        const colorHistoryContainer = document.getElementById('color-history');

        let colorHistory = [];
        const maxHistoryItems = 5;

        function getRandomHexColor() {
            const letters = '0123456789ABCDEF';
            let color = '#';
            for (let i = 0; i < 6; i++) {
                color += letters[Math.floor(Math.random() * 16)];
            }
            return color;
        }

        function applyNewColor(color) {
            colorBox.style.backgroundColor = color;
            colorCodeSpan.textContent = color.toUpperCase();
            addToHistory(color);
        }

        function addToHistory(color) {
            if (colorHistory[0] === color) return;

            colorHistory.unshift(color);

            if (colorHistory.length > maxHistoryItems) {
                colorHistory.pop();
            }

            renderHistory();
        }

        function renderHistory() {
            colorHistoryContainer.innerHTML = '';
            colorHistory.forEach(color => {
                const historyItem = document.createElement('div');
                historyItem.className = 'history-item';
                historyItem.style.backgroundColor = color;
                historyItem.title = color;
                historyItem.addEventListener('click', () => applyNewColor(color));
                colorHistoryContainer.appendChild(historyItem);
            });
        }

        changeColorBtn.addEventListener('click', () => {
            const newColor = getRandomHexColor();
            applyNewColor(newColor);
        });

        colorBox.addEventListener('click', () => {
            const newColor = getRandomHexColor();
            applyNewColor(newColor);
        });

        addToHistory('#6C5CE7');