import React, {useContext, useEffect, useState} from 'react'
import './Styles/Menu.css'
import {useNavigate} from "react-router-dom";


const Menu = ({active,setActive}) => {
    const [isOpen, setIsOpen] = useState(false);
    const [isRotated, setIsRotated] = useState(false);
    const [user, setUser] = useState(null);
    const [userImage, setUserImage] = useState('');
    const [isChecked, setIsChecked] = useState(false);
    const navigate=useNavigate()


    const toggleToolbar = () => {
        setIsOpen(!isOpen);
        setIsRotated(!isRotated);
    };
    const redirectTo=(url)=>{

        navigate(url);
    }
    const redirectToLogout = () => {
        window.location.replace('https://localhost:8080/api/logout');
    };


    useEffect(() => {
        const initialBackgroundColor = getComputedStyle(document.body).backgroundColor;
        setIsChecked(initialBackgroundColor === 'rgb(46, 46, 46)'); // Проверка на темный цвет
        getUser();
        userInfo();
    }, []);
    const userInfo = async () => {
        try {
            const response = await fetch('https://localhost:8080/api/userInfo', {
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
            const response = await fetch('https://localhost:8080/api/infoAboutUser', {
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
        document.body.backgroundColor="white";
    }

    const redirectToDelete = async () => {
        try {
            if (window.confirm('Вы уверены, что хотите удалить свою учетную запись?')) {
                const response = await fetch('https://localhost:8080/api/user', {
                    method: 'DELETE',
                    credentials: 'include'
                });
                if (response.ok)
                    window.location.replace('https://localhost:8080/api/logout');
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
        <div className={active?'menu active':'menu'}>
            <div className={active?'blur active':'blur'}/>
            <div className="menu-content">
                <ul>
                    {user && (
                        <>
                            <li><img id="myImage" src={`https://localhost:8080/api/images/${userImage}`} alt="Profile"/>
                            </li>
                            <li>Name: {user.name}</li>
                            <li>Created: {new Date(user.created).toLocaleDateString()}</li>
                            <li>Updated: {new Date(user.updated).toLocaleDateString()}</li>
                            <li>Role: {user.roles}</li>
                        </>
                    )}
                    <li className="delete"><a onClick={redirectToDelete}>Удалить учетную запись</a></li>
                    <li><a onClick={() => redirectTo('/update')}>Обновить мои данные</a></li>
                    <li className='exit'><a className="exit" onClick={redirectToLogout}>Выйти</a></li>
                    <li>
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