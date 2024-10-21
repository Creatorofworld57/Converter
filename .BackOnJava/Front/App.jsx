import React from 'react';
import {BrowserRouter as Router, Routes, Route, useLocation} from 'react-router-dom';
import Welcome from './Welcome';
import Loginpage from './Loginpage';
import NotFoundPage from './NotFoundPage';
import AudioPlayer from './Audio';
import Profile from './Profile';
import UserForm from './RegisterPage';
import Id from './ID';
import Update from './Update';
import All from './All';
import './Styles/Audio.css';
import AudioPlaylist from './AudioPlaylist';
import { AudioUpload } from './AudioUpload';
import PrivateRoute from './PrivateRoutes/PrivateRoute';
import ContextForAudio from './HelperModuls/ContextForAudio';
import Playlist from './Playlist';
import Home from "./Home";

const Layout = ({ children }) => {
    const location = useLocation(); // Получаем текущее местоположение

    // Проверяем текущий маршрут и скрываем header на странице "/home"
    const showHeader =
        location.pathname !== '/' &&
        location.pathname !== '/login' &&
        location.pathname !== '/update' &&
        location.pathname !== '/reg';

    return (
        <>
            {showHeader && <AudioPlayer />}  {/* Условный рендеринг Header */}
            {children}
        </>
    );
};

const App = () => {
    return (
        <ContextForAudio>
            <Router>
                <Layout>
                    <Routes>
                        <Route path="/login" element={<Loginpage />} />
                        <Route path="*" element={<NotFoundPage />} />
                        <Route path="/" element={<Home />} />
                        <Route path="/reg" element={<UserForm />} />
                        <Route element={<PrivateRoute />}>
                            <Route path="/profile" element={<Profile />} />
                            <Route path="/all" element={<All />} />
                            <Route path="/id" element={<Id />} />
                            <Route path="/update" element={<Update />} />
                            <Route path="/profile/playlist" element={<Playlist />} />
                            <Route path="/home" element={<Welcome />} />
                            <Route path="/audio_upload" element={<AudioUpload />} />
                            <Route path="/audio_playlist" element={<AudioPlaylist />} />
                        </Route>

                    </Routes>
                </Layout>
            </Router>
        </ContextForAudio>
    );
};

export default App;