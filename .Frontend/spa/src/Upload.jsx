import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

export const Upload = () => {
    const navigate = useNavigate();
    const [file, setFile] = useState(null); // initialize file state to null
    const backendUrl = process.env.REACT_APP_BACKEND_URL; // ensure this is set in .env file

    const handleSubmit = async (event) => {
        event.preventDefault(); // prevent default form submission behavior

        const formData = new FormData();
        formData.append('file', file);

        try {
            await fetch(`https://localhost:8080/api/pdf`, {
                method: 'POST',
                credentials: 'include',
                body: formData

            });

            navigate('/'); // navigate to home on successful upload
        } catch (error) {
            console.error('Error:', error);
            alert('Ошибка при отправке данных');
        }
    };

    return (
        <div className="form-container">
            <h1>Загрузить файл</h1>
]
            <form id="userForm" onSubmit={handleSubmit} encType="application/octet-stream">


                <div className="file-input-container">
                    <label className="file-input-label" htmlFor="file">Выберите музон</label>
                    <input
                        type="file"
                        id="file"
                        name="file"
                        onChange={(e) => setFile(e.target.files[0])}
                        required
                    />
                </div>

                {/* Move the submit button inside the form */}
                <button id="but" type="submit">Send</button>
            </form>

            <button className="Back" onClick={() => navigate('/')}>Назад</button>
        </div>
    );
};

export default Upload;
