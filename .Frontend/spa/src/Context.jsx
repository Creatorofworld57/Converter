// ContextForAudio.js
import React, { useState } from 'react';

export const TypeContext = React.createContext({
    color: false,
    type:"",
    chatId: 0,
    id: 0,// Установить значение по умолчанию, соответствующее типу string
    setColorTheme: (color) => {},  // Указываем, что функция принимает аргумент
    setTypeValue: (type) => {},    // Указываем, что функция принимает аргумент
    setChatIdValue: (chatId) => {},    // Указываем, что функция принимает аргумент
    setIdUpdatedValue: (id) => {},    // Указываем, что функция принимает аргумент
});

const Context = (props) => {
    // Состояния для разных переменных
    const [color, setColor] = useState(false); // Цвет темы
    const [type, setType] = useState("pdf"); // ID
    const [chatId, setChatId] = useState(0); // Задать начальное значение как строку
    const [idUpdated, setIdUpdated] = useState(0); // Задать начальное значение как строку

    // Функции для обновления состояний
    const setColorTheme = (color) => setColor(color);
    const setTypeValue = (updated) => setType(updated);
    const setChatIdValue = (chatId) => setChatId(chatId);
    const setIdUpdatedValue = (idUpdated) => setIdUpdated(idUpdated);

    // Объект, который содержит все данные и функции для их обновления
    const info = {
        color,
        setColorTheme,
        type,
        setTypeValue,
        chatId,
        setChatIdValue,
        idUpdated,
        setIdUpdatedValue
    };

    return (
        <TypeContext.Provider value={info}>
            {props.children}
        </TypeContext.Provider>
    );
};
export default Context;
