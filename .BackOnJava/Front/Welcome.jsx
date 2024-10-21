import React, {createContext, useContext, useEffect, useState} from 'react';
import { useNavigate } from 'react-router-dom';
import './Styles/Welcome.css';

import SlidingMenu from "./HelperModuls/SlidingMenu";


const Welcome = () => {
    const navigate = useNavigate();
    const [isButtonVisible, setButtonVisible] = useState(true);
    const [inputValue, setInputValue] = useState('');  // Значение из поля ввода
    const [tracks, setTracks] = useState([]);  // Список треков


    // Функция для поиска треков по названию
    const searchTracks = async (query) => {
        try {
            const response = await fetch(`https://localhost:8080/api/searchOfTrack/${query}`, {
                method: 'GET',
                credentials: 'include'
            });
            if (response.ok) {
                const data = await response.json();
                setTracks(null)
                setTracks(data);  // Устанавливаем найденные треки в состояние
            } else {
                setTracks([]);  // Если ничего не найдено, очищаем список
            }
        } catch (error) {
            console.error('Error fetching track info', error);
        }
    };

    // Отправка запроса на сервер при изменении inputValue
    useEffect(() => {
        if (inputValue) {
            searchTracks(inputValue);  // Вызываем функцию поиска, если есть текст в поле
        } else {
            setTracks([]);  // Если поле ввода пустое, очищаем список треков
        }
    }, [inputValue]);

    useEffect(() => {
        loginButton();
    }, []);

    const loginButton = async () => {
        try {
            const response = await fetch('https://localhost:8080/api/authorization', {
                method: 'GET',
                credentials: 'include'
            });
            if (response.status === 200) {
                setButtonVisible(false);
            } else {
                setButtonVisible(true);
            }
            console.log(isButtonVisible)
        } catch (error) {
            console.error('Failed to fetch authorization:', error);
        }
    };

    const handleInputChange = (event) => {
        setInputValue(event.target.value);
    };

    const redirectToLogin = () => {
        navigate('/login');
    };

    const redirectToAll = () => {
        navigate('/All');
    };

    const redirectToProfile = () => {
        navigate('/profile');
    };

    const redirectToAudioList = () => {
        navigate('/audio_playlist');
    };

    return (
        <div className="container">
            <button
                className={isButtonVisible ? "top" : "top active"}

                onClick={redirectToLogin}
            >
                Sign in or sign up
            </button>
            <div className="form-container">
                <h1>
                    Enter name of track
                </h1>
                <input
                    className={"inputForTracks"}
                    type="text"
                    id="userId"
                    placeholder="Enter name"
                    value={inputValue}
                    onChange={handleInputChange}
                />
            </div>

            {/* Вставляем SlidingMenu и передаем туда список треков */}
            <SlidingMenu tracks={tracks} val ={inputValue}/>

            <button className="All_button" onClick={redirectToAll}>
                All
            </button>
            <button className="top-left-button5" onClick={redirectToProfile}>
                Профиль
            </button>
            <button className="top-left-button3" onClick={redirectToAudioList}>
                Музон
            </button>
        </div>
    );
};

export default Welcome;
