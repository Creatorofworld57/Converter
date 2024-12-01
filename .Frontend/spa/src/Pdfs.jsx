import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import './Styles/Playlist.css';

const Pdfs = () => {
    const [pdfIds, setPdfIds] = useState([]); // Store the array of PDF IDs
    const [pdfUrls, setPdfUrls] = useState({}); // Map of ID -> Object URL for PDFs
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);
    const navigate = useNavigate();
    const backendUrl = process.env.REACT_APP_BACKEND_URL;

    const redirectTo = (url) => navigate(url);

    const fetchPdfIds = async () => {
        try {
            setIsLoading(true);
            const response = await fetch(`${backendUrl}/api/pdfs`, {
                method: "GET",
                credentials: "include",
            });

            if (!response.ok) {
                throw new Error("Failed to fetch PDF IDs");
            }

            const data = await response.json(); // Array of IDs, e.g., [1, 2, 3]
            setPdfIds(data);
        } catch (err) {
            setError(err.message);
        } finally {
            setIsLoading(false);
        }
    };

    const fetchPdfData = async (id) => {
        try {
            const response = await fetch(`${backendUrl}/api/pdfUser/${id}`, {
                method: "GET",
                credentials: "include",
            });

            if (!response.ok) {
                throw new Error(`Failed to fetch PDF with ID: ${id}`);
            }

            const blob = await response.blob();
            return URL.createObjectURL(blob); // Convert Blob to Object URL
        } catch (err) {
            console.error(`Error fetching PDF with ID ${id}:`, err);
            return null;
        }
    };

    useEffect(() => {
        fetchPdfIds();
    }, []);

    useEffect(() => {
        const fetchAllPdfData = async () => {
            const urls = await Promise.all(
                pdfIds.map(async (id) => {
                    const pdfUrl = await fetchPdfData(id);
                    return { id, pdfUrl };
                })
            );

            // Convert the array of {id, pdfUrl} into a map for easier access
            const urlMap = urls.reduce((acc, { id, pdfUrl }) => {
                if (pdfUrl) acc[id] = pdfUrl;
                return acc;
            }, {});
            setPdfUrls(urlMap);
        };

        if (pdfIds.length > 0) {
            fetchAllPdfData();
        }
    }, [pdfIds]);

    return (
        <div>
            <div className="tittlePlaylist">Мои PDF документы</div>
            <div className="menu-items">
                <ul>
                    {isLoading ? (
                        Array.from({ length: 5 }).map((_, index) => (
                            <li key={index} className="skeleton-track-item">
                                <div className="skeleton-image1"></div>
                                <div className="skeleton-text1"></div>
                            </li>
                        ))
                    ) : error ? (
                        <li className="error-message">{error}</li>
                    ) : pdfIds.length > 0 ? (
                        pdfIds.map((id) => (
                            <li key={id} className="item">
                                <div className="pdf-preview-container">
                                    {pdfUrls[id] ? (
                                        <iframe
                                            src={pdfUrls[id]}
                                            title={`PDF Preview - ID ${id}`}
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
                                <div className="playlist-container">
                                    <span>PDF ID: {id}</span>
                                </div>
                            </li>
                        ))
                    ) : (
                        <li>PDF документы не найдены</li>
                    )}
                </ul>
                <button className="back_up" onClick={() => redirectTo("/profile")}>
                </button>
            </div>
        </div>
    );
};

export default Pdfs;
