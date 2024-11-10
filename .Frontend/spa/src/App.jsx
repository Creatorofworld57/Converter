import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Welcome from './Welcome';
import Loginpage from './Loginpage';
import NotFoundPage from './NotFoundPage';
import Profile from './Profile';
import UserForm from './RegisterPage';
import Update from './Update';
import Upload from './Upload';
import "./Styles/App.css"
import './Styles/Audio.css';
import PrivateRoute from './PrivateRoutes/PrivateRoute';
import Converter from "./Converter";
import Context from "./Context";


const App = () => {
    return (
            <Context>
            <Router>
                <Routes>

                    <Route path="/" element={<Welcome />} />
                    <Route path="/login" element={<Loginpage />} />
                    <Route path="*" element={<NotFoundPage />} />
                    <Route element={<PrivateRoute />}>
                        <Route path="/profile" element={<Profile />} />
                        <Route path="/update" element={<Update />} />
                        <Route path ="/upload" element={<Upload/>}/>
                    </Route>
                    <Route path={"/converter"} element={<Converter/>}/>
                    <Route path="/reg" element={<UserForm />} />
                </Routes>
            </Router>
            </Context>

    );
};

export default App;
