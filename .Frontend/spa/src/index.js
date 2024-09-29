import React from 'react';
import { createRoot } from 'react-dom/client';
import App from './App';

// Находим корневой элемент в HTML
const container = document.getElementById('root');

// Создаем корень и рендерим в него приложение
const root = createRoot(container);
root.render(<App />);
