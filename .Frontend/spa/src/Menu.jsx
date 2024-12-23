import React, {useContext, useEffect, useState} from 'react'
import './Styles/Menu.css'
import {useNavigate} from "react-router-dom";
import {TypeContext} from "./Context";


const Menu = ({active,setActive}) => {
    const [isOpen, setIsOpen] = useState(false);
    const [isRotated, setIsRotated] = useState(false);
    const [user, setUser] = useState(null);
    const [userImage, setUserImage] = useState('');
    const [isChecked, setIsChecked] = useState(false);
    const navigate=useNavigate()
    const backendUrl = process.env.REACT_APP_BACKEND_URL;
    const {color,setColorTheme} = useContext(TypeContext)

    const toggleToolbar = () => {
        setIsOpen(!isOpen);
        setIsRotated(!isRotated);
    };
    const redirectTo=(url)=>{

        navigate(url);
    }
    const redirectToLogout = () => {
        window.location.replace('http://localhost:8080/logout');
    };


    useEffect(() => {
        const initialBackgroundColor = getComputedStyle(document.body).backgroundColor;
        setIsChecked(!color); // Проверка на темный цвет
        getUser();
        userInfo();
    }, []);
    const userInfo = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/userInfo', {
                method: 'GET',
                credentials: 'include'
            });
            const user = await response.text();
            setUserImage(user);
        } catch (error) {
            console.error('Error:', error);
        }
    };

    const getUser = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/infoAboutUser', {
                method: 'GET',
                credentials: 'include'
            });

            const userok = await response.json();
            setUser(userok);

        } catch (error) {
            console.error('Error fetching user info', error);
        }


    };
    const colorfunc=()=>{
        document.body.style.backgroundColor = color ? "#353845": "#cccccc";
        setColorTheme(!color)

    }

    const redirectToDelete = async () => {
        try {
            if (window.confirm('Вы уверены, что хотите удалить свою учетную запись?')) {
                const response = await fetch('http://localhost:8080/api/user', {
                    method: 'DELETE',
                    credentials: 'include'
                });
                if (response.ok)
                    window.location.replace('http://localhost:8080/api/logout');
            }
        } catch (error) {
            console.error('Error deleting', error);
            alert('Error deleting.');
        }
    };
    const handleChange = (event) => {
        const checked = event.target.checked;
        setIsChecked(checked);
    };
    return (
        <div className={active? !color?'menu active':'menu active light' : !color?'menu':'menu light'}>
            <div className={active?'blur active':'blur'}/>
            <div className="menu-content">
                <ul className={"ul_menu"}>
                    {user && (
                        <>
                            <li className={color?"li_menu light" :"li_menu"}><img id="myImage" src={`http://localhost:8080/api/images/${userImage}`} alt="Profile"/>
                            </li>
                            <li className={color?"li_menu light" :"li_menu"}>Имя: {user.name}</li>
                            <li className={color?"li_menu light" :"li_menu"}>Created: {new Date(user.created).toLocaleDateString()}</li>
                            <li className={color?"li_menu light" :"li_menu"}>Updated: {new Date(user.updated).toLocaleDateString()}</li>
                        </>
                    )}
                    <li className="li_menu delete"><a className={"a_menu"} onClick={redirectToDelete}>Удалить учетную запись</a></li>
                    <li className={"li_menu"} ><a onClick={() => redirectTo('/update')}>Обновить мои данные</a></li>
                    <li className='li_menu exit'><a className={"a_menu"} onClick={redirectToLogout}>Выйти</a></li>
                    <li className={"li_menu"}>
                        <div className="toggle-switch">
                            <input
                                type="checkbox"
                                id="toggle"
                                className="toggle-input"
                                onClick={colorfunc}
                                checked={isChecked}
                                onChange={handleChange}
                            />
                            <label htmlFor="toggle" className="toggle-label"></label>
                        </div>
                    </li>
                </ul>
            </div>


        </div>)


}
export default Menu;