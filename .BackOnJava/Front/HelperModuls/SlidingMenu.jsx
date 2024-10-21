// SlidingMenu.js
import React, {useContext} from 'react';
import '../Styles/SlidingMenu.css';
import {MyContext} from "./ContextForAudio"; // Подключаем стили

const SlidingMenu = ({ tracks, val}) => {
    const {data, setDat} = useContext(MyContext); // Деструктурируем setDat


    const handleClick = (id) => {
        setDat(id); // Правильно вызываем setDat
    };

    return (
        <div className={`menu1 ${(tracks.length > 0 && val.trim() !== '') ? 'open' : 'closed'}`}>
            <ul>
                {/* Проверяем, есть ли треки */}
                {tracks && tracks.length > 0 ? (
                    tracks.map((audio, index) => (
                        <li key={index} className="track-item1" onClick={() => handleClick(audio.id)}>
                            {/* Выводим изображение трека */}
                            <img className="track-image1"  src={`data:image/jpeg;base64,${audio.imagesc}`}/>
                            {audio.name}
                            {/* Выводим имя трека */}
                        </li>
                    ))
                ) : (
                    <li>Треки не найдены</li>
                )}
            </ul>
        </div>
    );
};

export default SlidingMenu;