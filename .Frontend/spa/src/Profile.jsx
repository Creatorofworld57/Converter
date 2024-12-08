import React, {useContext, useEffect, useState} from 'react';
import './Styles/Profile.css';
import { useNavigate } from 'react-router-dom';
import Menu from './Menu';
import { FaGithub, FaTelegramPlane } from 'react-icons/fa';
import {TypeContext} from "./Context";



const Profile = () => {
    const navigate = useNavigate();
    const [menuActive, setMenuActive] = useState(false);
    const [isChecked, setIsChecked] = useState(false);
    const [url1, setUrl1] = useState('');
    const [url2, setUrl2] = useState('');
    const {color} = useContext(TypeContext)
    const backendUrl = process.env.REACT_APP_BACKEND_URL;



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



                <div className={color?"tools__item light":"tools__item"}>
                    <a href="/pdfs" title="PDF в JPG">
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

            <nav className={color?"nav_pro light":"nav_pro"}>

                <div>
                    <div className='burger-btn' onClick={() => setMenuActive(!menuActive)}>
                        <span className={menuActive ? color?'line1 active light':'line1 active' :  color?'line1 light':'line1'}/>
                        <span className={menuActive ? color?'line2 active light':'line2 active' :  color?'line2 light':'line2'}/>
                        <span className={menuActive ? color?'line3 active light':'line3 active' :  color?'line3 light':'line3'}/>
                    </div>

                </div>

            </nav>



            <button className={color?"back_up light" :"back_up"} onClick={() => redirectTo('/')}></button>


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
