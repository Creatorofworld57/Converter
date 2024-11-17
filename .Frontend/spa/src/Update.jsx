import React, {useEffect, useState} from 'react';
import './Styles/Update.css';
import { useNavigate } from "react-router-dom";
import logo from './logo.svg';  // Import the default logo

const Update = () => {
    const [name, setName] = useState('');
    const [password, setPassword] = useState('');
    const [tele, setTele] = useState('');
    const [git, setGit] = useState('');
    const [file, setFile] = useState(null);  // Start with no file selected
    const navigate = useNavigate();
    useEffect(() => {
       getUser();
    }, []);
    const handleSubmit = async (event) => {
        event.preventDefault();

        const formData = new FormData();
        formData.append('name', name);
        formData.append('password', password);
        formData.append('tele', tele);
        formData.append('git', git);

        // If file is selected, append it; otherwise, append the default logo
        if (file) {
            formData.append('file', file);
        } else {
            // Fetch the logo file as Blob
            const response = await fetch(logo);
            const logoBlob = await response.blob();
            formData.append('file', logoBlob);
        }

        const json = JSON.stringify({ name, password, tele, git });
        formData.append('json', new Blob([json], { type: 'application/json' }));

        try {
            const response = await fetch('http://localhost:8080/api/user', {
                method: 'PATCH',
                body: formData,
                credentials: "include"
            });

            if (response.ok) {
                alert('Data sent successfully!');
            } else {
                const errorMessage = await response.text();
                alert(`Failed to send data: ${errorMessage}`);
            }
        } catch (error) {
            alert(`An error occurred: ${error.message}`);
        }
    };
    const getUser = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/infoAboutUser', {
                method: 'GET',
                credentials: 'include'
            });

            const userok = await response.json();
            setName(userok.name)
            setTele(userok.tele)
            setGit(userok.git)

        } catch (error) {
            console.error('Error fetching user info', error);
        }


    };

    const handleRedirect = () => {
        navigate('/profile');
    };
    return (
        <div className="form-container">
            <h1>Enter Data</h1>
            <form onSubmit={handleSubmit} encType="multipart/form-data">
                <input
                    type="text"
                    id="name"
                    name="name"
                    placeholder="Enter your Name"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                />
                <input
                    type="password"
                    id="password"
                    name="password"
                    placeholder="Enter your Password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />
                <input
                    type="text"
                    id="tele"
                    name="tele"
                    placeholder="Enter your Telegram"
                    value={tele}
                    onChange={(e) => setTele(e.target.value)}
                />
                <input
                    type="text"
                    id="git"
                    name="git"
                    placeholder="Enter your GitHub"
                    value={git}
                    onChange={(e) => setGit(e.target.value)}
                />
                <div className="file-input-container">
                    <label className="file-input-label" htmlFor="file">Выберите новый аватар</label>
                    <input
                        type="file"
                        id="file"
                        name="file"
                        onChange={(e) => setFile(e.target.files[0])}
                    />
                </div>
                <button type="submit">Send</button>
            </form>
            <button className="Back" onClick={handleRedirect}>Назад</button>
        </div>
    );
};

export default Update;
