import React, { useState } from 'react';
import './Styles/Registr.css';
import { useNavigate } from 'react-router-dom';
import * as events from "events";

const UserForm = () => {
    const [name, setName] = useState('');
    const [password, setPassword] = useState('');
    const [roles, setRoles] = useState('');
    const [file, setFile] = useState(null);
    const [check, setCheck] = useState(true);

    const navigate = useNavigate();

    const handleSubmit = async (event) => {
        event.preventDefault(); // предотвращаем стандартное поведение отправки формы

        const formData = new FormData();
        formData.append('name', name);
        formData.append('password', password);
        formData.append('roles', "ROLE_ADMIN");
        formData.append('file', file);
        const dataForLogin = {
            name: name,
            password: password
        };
        const resp = async()=> {
            await fetch('http://localhost:8080/api/user', {
                method: 'POST',
                body: formData,
                credentials: 'include'
            });

        }
        try {
           await resp()
            navigate('/')
        } catch (error) {
            console.error('Error:', error);
            alert('Ошибка при отправке данных');
        }

    };

    const checking = async (name) => {
        try {
            console.log(name)
            const response = await fetch('http://localhost:8080/api/checking', {
                method: 'POST',
                body: JSON.stringify({ name }),
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            if (response.status === 200) {
                setCheck(false);
            } else {
                setCheck(true);
            }
        } catch (error) {
            console.error('Error:', error);
            setCheck(true);
        }
    };

    const handleNameChange = async (events) => {
        const newName = events.target.value;
        setName(newName);
        await checking(newName);
    };

    return (
        <div className="form-container">
            <h1>Введите данные</h1>
            <form id="userForm" onSubmit={handleSubmit} encType="multipart/form-data">
                <input
                    className={check ? 'input-valid' : 'input-invalid'}
                    type="text"
                    id="name"
                    name="name"
                    placeholder="Введите имя"
                    value={name}
                    onChange={handleNameChange}
                    required
                />
                <a className={check ? 'Nevalid' : 'Valid'}>Это имя уже используется</a>
                <input
                    type="password"
                    id="password"
                    name="password"
                    placeholder="Введите пароль"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                />
                <div className="file-input-container">
                    <label className="file-input-label" htmlFor="file">Выберите аватар</label>
                    <input
                        type="file"
                        id="file"
                        name="file"
                        onChange={(e) => setFile(e.target.files[0])}
                        required
                    />
                </div>
                <button id="but" className={check?'send':'unsent'} type="submit">Зарегаться</button>
            </form>
            <button className="Back" onClick={() => navigate('/')}>Назад</button>
        </div>
    );
};

export default UserForm;
