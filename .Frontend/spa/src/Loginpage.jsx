// LoginPage.jsx
import React from 'react';
import { useNavigate } from 'react-router-dom';
import './Styles/Loginpage.css';
import {FaGithub} from "react-icons/fa";

const Loginpage = () => {
  const navigate = useNavigate();
    const backendUrl = process.env.REACT_APP_BACKEND_URL;
    const loginUrl =`${backendUrl}/perform_login`
  const redirectTo = (url) => {
    navigate(url);
  };
    const close = () => {
       window.close();
    };

  return (
    <div className="container">
      <button className="top-left-button" onClick={() => redirectTo('/')}>
        Назад
      </button>

      <form action={loginUrl} method="post" content="application/x-www-form-urlencoded">
          <div className="naming">
              <img
                  src="https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png"
                  alt="GitHub Logo"
                  className="logo"
              />
              <h1>Вход в систему</h1>

              <input
                  type="text"
                  id="name"
                  name="name"
                  placeholder="Введите имя"
                  required
              />

              <input
                  type="password"
                  id="password"
                  name="password"
                  placeholder="Введите пароль"
                  required
              />
              <div className="imgEntry"><a  href="http://localhost:8080/oauth2/authorization/github" target="_blank"
                                           rel="noopener noreferrer">
                  Продолжить с      <FaGithub/>
              </a></div>

              <button className="button" type="submit">
                 Войти
              </button>
          </div>
      </form>

        <a className="reg_ref" onClick={() => redirectTo('/reg')}>У меня нет аккаунта</a>
    </div>
  );
};

export default Loginpage;
