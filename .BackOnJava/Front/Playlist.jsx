import React, {useState, useEffect, useContext} from 'react';

import {MyContext} from "./HelperModuls/ContextForAudio";
import {useNavigate} from "react-router-dom";

const Playlist = ({name}) => {
  const [tracks,setTracks] = useState([])
  const [isLoading,setIsLoading] = useState(true)
    const navigate = useNavigate();
    const redirectTo = (url) => {
        navigate(url);
    };
    const responseForTracks = async () => {
        setIsLoading(true)
        const response = await fetch('https://localhost:8080/api/likedPlaylist',{method: 'GET',credentials:'include'})
        const data = await response.json()
        setTracks(data);
        setIsLoading(false)
    }
    const {data, setDat} = useContext(MyContext); // Деструктурируем setDat

    const handleClick = (id) => {
        setDat(id); // Правильно вызываем setDat
    };
    useEffect(() => {
        responseForTracks();
    }, []);

  return (
      <div className="menu1.open">
          <ul>
              {isLoading ? (
                  // Показываем скелетоны во время загрузки данных
                  Array.from({length: 5}).map((_, index) => (
                      <li key={index} className="skeleton-track-item">
                          <div className="skeleton-image1"></div>
                          <div className="skeleton-text1"></div>
                      </li>
                  ))
              ) : tracks && tracks.length > 0 ? (
                  // Показываем реальные данные после загрузки
                  tracks.map((audio, index) => (
                      <li key={index} className="track-item1" onClick={() => handleClick(audio.id)}>
                          <img className="track-image1" src={`data:image/jpeg;base64,${audio.imagesc}`}/>
                          {audio.name}
                      </li>
                  ))
              ) : (
                  <li>Треки не найдены</li>
              )}
          </ul>
          <button className="back_up" onClick={() => redirectTo('/profile')}></button>
      </div>
  );
};
export default Playlist;
