import React, {useContext, useEffect, useState} from 'react';
import './Styles/Profile.css';
import { useNavigate } from 'react-router-dom';
import Menu from './Menu';
import { FaGithub, FaTelegramPlane } from 'react-icons/fa';



const Profile = () => {
    const navigate = useNavigate();
    const [menuActive, setMenuActive] = useState(false);
    const [isChecked, setIsChecked] = useState(false);
    const [url1, setUrl1] = useState('');
    const [url2, setUrl2] = useState('');




    const redirectTo = (url) => {
        navigate(url);
    };

    const userSocials = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/socials', {
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


        // Устанавливаем начальное значение isChecked в зависимости от цвета фона
        const initialBackgroundColor = getComputedStyle(document.body).backgroundColor;
        setIsChecked(initialBackgroundColor === 'rgb(46, 46, 46)'); // Проверка на темный цвет
    }, []);




    return (
        <div>
            <div className="tools__container">
                <div className="tools__item">
                    <a href="/ru/word_to_pdf" title="Word в PDF">
                        <div className="tools__item__icon">
                            <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 50 50">


                                <g fill-rule="evenodd">
                                    <path
                                        d="M22.855 17.676H44.82c1.8 0 2.453.188 3.113.543.648.344 1.184.875 1.527 1.527.352.656.54 1.31.54 3.11V44.82c0 1.8-.187 2.453-.54 3.113a3.69 3.69 0 0 1-1.527 1.527c-.66.352-1.312.54-3.113.54H22.855c-1.8 0-2.453-.187-3.113-.54-.648-.344-1.18-.88-1.527-1.527-.352-.66-.54-1.312-.54-3.113V22.855c0-1.8.188-2.453.54-3.113.348-.648.88-1.18 1.527-1.527.66-.352 1.313-.54 3.113-.54zm0 0"
                                        fill="rgb(83.921569%,74.901961%,17.647059%)"></path>
                                    <path
                                        d="M41.5 26c1.102 0 2 .898 2 2s-.898 2-2 2-2-.898-2-2 .898-2 2-2zM30.6 39h-6.344c-.1 0-.172-.047-.215-.125s-.043-.168.004-.242l6.574-11.02a.26.26 0 0 1 .426 0l3.832 6.422 2.57-2.625c.047-.05.11-.074.176-.074h.008c.07 0 .137.03.18.086l6.1 7.13c.07.043.11.12.11.203 0 .133-.113.242-.246.242H30.6v-.004zm0 0"
                                        fill="rgb(100%,100%,100%)"></path>
                                </g>
                            </svg>
                        </div>
                        <h3>JPG Storage</h3>
                        <div className="tools__item__content">
                            <p>Здесь хранятся ваши JPG файлы</p>
                        </div>
                    </a>
                </div>
                <div className="tools__item">
                    <a href="/ru/pdf_to_word" title="Word Storage">
                        <div className="tools__item__icon">
                            <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 50 50">

                               
                                <path
                                    d="M22.855 17.676H44.82c1.8 0 2.45.188 3.113.543a3.68 3.68 0 0 1 1.527 1.523c.352.66.54 1.313.54 3.113V44.82c0 1.8-.187 2.45-.54 3.113s-.867 1.176-1.527 1.527-1.312.54-3.113.54H22.855c-1.8 0-2.453-.187-3.113-.54-.648-.344-1.18-.88-1.527-1.527-.352-.66-.54-1.312-.54-3.113V22.855c0-1.8.188-2.453.54-3.113.348-.648.88-1.18 1.527-1.527.66-.352 1.313-.54 3.113-.54zm0 0"
                                    fill-rule="evenodd" fill="rgb(37.254902%,51.372549%,77.647059%)"></path>
                                <path
                                    d="M38.996 26.75h2.965l-2.94 14.64h-3.094l-1.777-9.035-1.824 9.035H29.12L26.2 26.75h3.164l1.508 9.363 1.938-9.363h3.004l1.727 9.297zm0 0"
                                    fill="rgb(100%,100%,100%)"></path>
                            </svg>
                        </div>
                        <h3>     Word Storage</h3>
                        <div className="tools__item__content">
                            <p>Здесь хранятся ваши документы word</p>
                        </div>
                    </a>
                </div>

                <div className="tools__item">
                    <a href="/ru/pdf_to_jpg" title="PDF в JPG">
                        <div className="tools__item__icon">
                            <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 50 50">


                                <g fill-rule="evenodd">
                                    <path
                                        d="M22.855 17.676H44.82c1.8 0 2.453.188 3.113.543.648.344 1.184.875 1.527 1.527.352.656.54 1.31.54 3.11V44.82c0 1.8-.187 2.453-.54 3.113a3.69 3.69 0 0 1-1.527 1.527c-.66.352-1.312.54-3.113.54H22.855c-1.8 0-2.453-.187-3.113-.54-.648-.344-1.18-.88-1.527-1.527-.352-.66-.54-1.312-.54-3.113V22.855c0-1.8.188-2.453.54-3.113.348-.648.88-1.18 1.527-1.527.66-.352 1.313-.54 3.113-.54zm0 0"
                                        fill="rgb(100%,46.27451%,31.764706%)"></path>
                                    <path
                                        d="M38.367 34.648C37.28 35.55 35.828 36 34.008 36H32.39v5H29V26.5h5.313c3.79 0 5.688 1.54 5.688 4.62 0 1.453-.543 2.633-1.633 3.535zM33.82 29H32.5v4.5h1.32c1.785 0 2.68-.758 2.68-2.273 0-1.484-.89-2.227-2.68-2.227zm0 0"
                                        fill="rgb(100%,100%,100%)"></path>
                                </g>
                            </svg>
                        </div>
                        <h3>PDF Storage</h3>
                        <div className="tools__item__content">
                            <p>Здесь хранятся ваши pdf файлы</p>
                        </div>
                    </a>
                </div>
            </div>
            <nav>

                <div>
                    <div className='burger-btn' onClick={() => setMenuActive(!menuActive)}>
                        <span className={menuActive ? 'line1 active' : 'line1'}/>
                        <span className={menuActive ? 'line2 active' : 'line2'}/>
                        <span className={menuActive ? 'line3 active' : 'line3'}/>
                    </div>

                </div>

            </nav>

            <main>
                <div>

                </div>
            </main>

            <button className="back_up" onClick={() => redirectTo('/')}></button>

            <Menu active={menuActive} setActive={setMenuActive}/>

            {url1 && (
                <div className="link-preview">
                    <a href={url1} target="_blank" rel="noopener noreferrer">
                        <FaTelegramPlane/> {url1}
                    </a>
                </div>
            )}
            {url2 && (
                <div className="link-preview1">
                    <a href={url2} target="_blank" rel="noopener noreferrer">
                        <FaGithub/> {url2}
                    </a>
                </div>
            )}

        </div>
    );
};

export default Profile;
