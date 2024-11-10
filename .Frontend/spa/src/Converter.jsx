import React, { useContext, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { TypeContext } from "./Context";

export const Converter = () => {
    const navigate = useNavigate();
    const [file, setFile] = useState(null);
    const { type, setType } = useContext(TypeContext);
    const [isLoading,setIsLoading] = useState(true) // use type and setType from context

    const handleSubmit = async (event) => {
        event.preventDefault();

        const formData = new FormData();
        formData.append('file', file);

        try {
            await fetch(`http://localhost:8081/upload/docxtopdf`, {
                method: 'POST',
                body: formData
            });

            navigate('/');
        } catch (error) {
            console.error('Error:', error);
            alert('Ошибка при отправке данных');
        }
    };

    useEffect(() => {
        if (type === "pdfToDoc") {
            setType('pdf');
        } else {
            setType('файл');
        }
    }, [type, setType]);

    return (
        <div>
            {isLoading &&
        <div className="form-container">
            <h1>Загрузить файл</h1>
            <form id="userForm" onSubmit={handleSubmit} encType="multipart/form-data">
                <div className="file-input-container">
                    <label className="file-input-label" htmlFor="file">Выберите {type}</label>
                    <input
                        type="file"
                        id="file"
                        name="file"
                        onChange={(e) => setFile(e.target.files[0])}
                        required
                    />
                </div>

                <button id="but" type="submit">Send</button>
            </form>

            <button className="Back" onClick={() => navigate('/')}>Назад</button>
        </div>


            }
        </div>
    );
};

export default Converter;
