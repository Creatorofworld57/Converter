import React, { createContext, useState } from 'react';

// Создаем контекст
export const ThemeContext = createContext();

// Создаем провайдер для оборачивания компонентов
export const ThemeProvider = ({ children }) => {
    const [isDarkMode, setIsDarkMode] = useState(false);

    const toggleTheme = () => {
        setIsDarkMode(!isDarkMode);
    };

    return (
        <ThemeContext.Provider value={{ isDarkMode, toggleTheme }}>
            {children}
        </ThemeContext.Provider>
    );
};
