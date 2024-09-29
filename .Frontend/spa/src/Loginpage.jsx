// LoginPage.jsx
import React from 'react';
import { useNavigate } from 'react-router-dom';
import './Styles/Loginpage.css';
import {FaGithub} from "react-icons/fa";

const Loginpage = () => {
  const navigate = useNavigate();

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

      <form action="https://localhost:8080/perform_login" method="post" content="application/x-www-form-urlencoded">
          <div className="naming">
              <img
                  src="https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png"
                  alt="GitHub Logo"
                  className="logo"
              />
              <h1>Sign in to Base</h1>

              <input
                  type="text"
                  id="name"
                  name="name"
                  placeholder="Enter your Name"
                  required
              />

              <input
                  type="password"
                  id="password"
                  name="password"
                  placeholder="Enter your password"
                  required
              />
              <div className="imgEntry"><a  href="https://localhost:8080/oauth2/authorization/github" target="_blank"
                                           rel="noopener noreferrer">
                  Continue with     <FaGithub/>
              </a></div>

              <button className="button" type="submit">
                  Login
              </button>
          </div>
      </form>

        <a className="reg_ref" onClick={() => redirectTo('/reg')}>I haven't account</a>
    </div>
  );
};

export default Loginpage;
