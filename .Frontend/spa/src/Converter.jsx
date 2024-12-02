import React, { useContext, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { TypeContext } from "./Context";

export const Converter = () => {
    const navigate = useNavigate();
    const [files, setFiles] = useState([]); // Хранение массива файлов
    const { type, setTypeValue } = useContext(TypeContext);
    const [tittleType, setTittleType] = useState("файл");
    const [isLoading, setIsLoading] = useState(true);
    const [fileName, setFileName] = useState(null); // Имя итогового файла для скачивания
    const [isWatermark, setIsWatermark] = useState(false);
    const [pdfUrls, setPdfUrls] = useState({});// Имя итогового файла для скачивания
    const backendUrl = process.env.REACT_APP_BACKEND_URL;

    const handleSubmit = async (event) => {
        event.preventDefault();

        const formData = new FormData();
        if (type === "pdfmerge") {
            // Добавляем все файлы в FormData

            files.forEach(file => {
                formData.append("files[]", file); // Каждый файл добавляется под "files[]"
            });

        }
        else if(type==="watermarkpdf"){
            console.log(files.length)
            formData.append("files",files[0])
            formData.append("files",files[1])

        }


        else {
            formData.append("file", files[0]); // Берем первый файл для других типов операций
        }


        try {
            const response = await fetch(`http://localhost:8081/upload/${type}`, {
                method: "POST",
                body: formData,
            });
            if (response.ok) {
                const responseText = await response.text(); // Читаем строку из ответа
                console.log(responseText); // Посмотрим, что возвращается

                // Извлекаем имя файла с помощью регулярного выражения
                const match = responseText.match(/File '(.+?)'/);
                if (match && match[1]) {
                    const extractedFileName = match[1]; // Извлекаем имя файла
                    setFileName(extractedFileName); // Сохраняем имя файла в состоянии
                    console.log("Extracted filename:", extractedFileName); // Лог сразу после извлечения
                } else {
                    throw new Error("Имя файла не найдено в ответе");
                }

                setIsLoading(false);
                // Если нужно использовать `fileName` после `setFileName`, сделайте это внутри useEffect:
                console.log(fileName); // Это может отобразить старое состояние, так как setFileName работает асинхронно
            }

        } catch (error) {
            console.error("Error:", error);
            alert("Ошибка при отправке данных");
        }
    };

    const downloadFile = async () => {
        try {
            const response = await fetch(`${backendUrl}/api/pdf/${fileName}`, {
                method: "GET",
            });

            if (response.status !== 200) {
                throw new Error("Ошибка загрузки файла");
            }

            const blob = await response.blob();
            const url = window.URL.createObjectURL(blob);

            const link = document.createElement("a");
            link.href = url;
            link.download = "merged_file.pdf"; // Укажите имя итогового файла
            link.click();

            window.URL.revokeObjectURL(url);
        } catch (error) {
            console.error("Error:", error);
            alert("Ошибка загрузки файла");
        }
    };

    useEffect(() => {
        if (type === "docxtopdf") {
            setTittleType("docx файл");
        } else if (type === "pdfmerge") {
            setTittleType("pdf файлы для объединения");
        }
        else if(type==="watermarkpdf"){
            setIsWatermark(true)
        }
        else if(type==="xlstopdf"){
            setTittleType("xls файл")
        }
        else if (type==="jpgtopdf"){
            setTittleType("jpg файл")
        }

    }, [type]);

    return (
        <div>
            {isLoading ? (
                    <div className="form-container">
                        <h1>Загрузить файл</h1>
                        {!isWatermark ? (
                            <form id="userForm" onSubmit={handleSubmit} encType="multipart/form-data">
                                <div className="file-input-container">
                                    <label className="file-input-label" htmlFor="file">
                                        Выберите {tittleType}
                                    </label>
                                    <input
                                        type="file"
                                        id="file"
                                        name="file"
                                        onChange={(e) => setFiles((prevFiles) => [...prevFiles, ...Array.from(e.target.files)])}
                                        multiple={type === "pdfmerge"} // Разрешаем загрузку нескольких файлов только для объединения
                                        required
                                    />
                                </div>

                                <button id="but" type="submit">
                                    Отправить
                                </button>
                            </form>) : (
                            <div>
                                <form id="userForm" onSubmit={handleSubmit} encType="multipart/form-data">
                                    <div className="file-input-container">
                                        <label className="file-input-label" htmlFor="file">
                                            Выберите основной файл
                                        </label>
                                        <input
                                            type="file"
                                            id="name"
                                            name="name"
                                            onChange={(e) => setFiles((prevFiles) => [...prevFiles, ...Array.from(e.target.files)])}
                                            // Разрешаем загрузку нескольких файлов только для объединения
                                            required
                                        />
                                    </div>
                                    <div className="file-input-container">
                                        <label className="file-input-label" htmlFor="file">
                                            Выберите вотермарку
                                        </label>
                                        <input
                                            type="file"
                                            id="watermark"
                                            name="watermark"
                                            onChange={(e) => setFiles((prevFiles) => [...prevFiles, ...Array.from(e.target.files)])}
                                            // Разрешаем загрузку нескольких файлов только для объединения
                                            required
                                        />
                                    </div>

                                    <button id="but" type="submit">
                                        Отправить
                                    </button>
                                </form>
                            </div>


                        )
                        }

                    </div>
                )
                :
                (
                    <div>
                        <button id="but" onClick={downloadFile}>
                            Скачать
                        </button>
                    </div>
                )
            }
            <div className="pdf-preview-container">
                {files !==null || files!==undefined ? (
                    <iframe
                        src={files[0]}
                        style={{
                            width: "100%",
                            height: "400px",
                            border: "none",
                        }}
                    />
                ) : (
                    <div className="error-message">
                        Загрузка PDF...
                    </div>
                )}
            </div>
            <button className="Back" onClick={() => navigate("/")}>
                Назад
            </button>
        </div>
    )
        ;
};

export default Converter;
