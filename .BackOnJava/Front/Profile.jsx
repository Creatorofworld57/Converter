import React, { useEffect, useState } from 'react';
import './Styles/Profile.css';
import { useNavigate } from 'react-router-dom';
import Menu from './HelperModuls/Menu';
import { FaGithub, FaTelegramPlane } from 'react-icons/fa';

const Profile = () => {
    const navigate = useNavigate();
    const [menuActive, setMenuActive] = useState(false);
    const [url1, setUrl1] = useState('');
    const [url2, setUrl2] = useState('');

    // Массив с плейлистами
    const playlists = [
        {
            id: 1,
            title: 'Мне нравится',
            imageUrl: 'https://images.genius.com/3c0be727f05563c85daea580c688d316.1000x1000x1.png',
            url: '/playlist/liked'
        },
        {
            id: 2,
            title: 'Классика',
            imageUrl: 'https://localhost:8080/api/images/5',
            url: '/playlist/classic'
        },

    ];

    const redirectTo = (url) => {
        navigate(url);
    };

    const userSocials = async () => {
        try {
            const response = await fetch('https://localhost:8080/api/socials', {
                method: 'GET',
                credentials: 'include'
            });
            const user = await response.json();
            setUrl1(user.telegram);
            setUrl2(user.git);
        } catch (error) {
            console.error('Error:', error);
        }
    };

    useEffect(() => {
        userSocials();
    }, []);

    return (
        <div>
            <nav>
                <div className='burger-btn' onClick={() => setMenuActive(!menuActive)}>
                    <span className={menuActive ? 'line1 active' : 'line1'} />
                    <span className={menuActive ? 'line2 active' : 'line2'} />
                    <span className={menuActive ? 'line3 active' : 'line3'} />
                </div>
            </nav>

            <main>
                <div className="playlists-container">
                    {playlists.map((playlist) => (
                        <div key={playlist.id} className="playlist-item" onClick={() => redirectTo("playlist")}>
                            <img src={playlist.imageUrl} alt={playlist.title} />
                            <p>{playlist.title}</p>
                        </div>
                    ))}
                </div>
            </main>

            <button className="back_up" onClick={() => redirectTo('/home')}></button>

            <Menu active={menuActive} setActive={setMenuActive} />

            {url1 && (
                <div className="link-preview">
                    <a href={url1} target="_blank" rel="noopener noreferrer">
                        <FaTelegramPlane /> {url1}
                    </a>
                </div>
            )}
            {url2 && (
                <div className="link-preview1">
                    <a href={url2} target="_blank" rel="noopener noreferrer">
                        <FaGithub /> {url2}
                    </a>
                </div>
            )}
        </div>
    );
};

export default Profile;
