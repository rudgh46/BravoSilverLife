import React, { useState, useEffect } from "react";
import "./Main.css";
import { Link, useLocation } from "react-router-dom";
import { TransitionGroup, CSSTransition } from "react-transition-group";
import mainChicken from "../assets/images/main-chicken.jpg";
import mainPizza from "../assets/images/main-pizza.jpg";
import logo from "../assets/images/mainlogo.svg";
import mainMiddle from "../assets/images/main-middle.jpg";
import { Token } from "@mui/icons-material";
import axios from "axios";
// import mainCook from "../assets/images/main-cook.jpg";

function Main(props) {
  useEffect(() => {
    if (localStorage.getItem("user") === null) {
      setIsLogin(false);
    } else {
      getuserInfo();
    }
  });

  const [isLogin, setIsLogin] = useState(false);
  const location = useLocation();
  const [userinfo, setUserinfo] = useState("");

  const getuserInfo = () => {
    if (userinfo !== localStorage.getItem("user")) {
      setUserinfo(localStorage?.getItem("user"));
      setIsLogin(true);
    } else {
      setIsLogin(true);
    }
  };

  const handleLogout = () => {
    setIsLogin(false);
    localStorage.removeItem("user");
    localStorage.removeItem("userdata");
    setUserinfo("");
  };

  const [fade, setFade] = useState("");
  const [menu, setMenu] = useState("");
  const menuToggle = () => {
    if (menu === "") {
      setMenu("active");
    } else {
      setMenu("");
    }
  };
  const switchRanking = () => {
    setTimeout(setFade("fade"), 3000);
  };
  const loginbutton = (
    <>
      <div className="middle-menu__user-login" title="로그인 하러가기">
        <Link to="/signin" className="middle-menu__user-login__link">
          SIGN IN
        </Link>
      </div>
      <div className="middle-menu__user-join" title="회원가입 하러가기">
        <Link to="/signup" className="middle-menu__user-join__link">
          SIGN UP
        </Link>
      </div>
    </>
  );

  const logoutbutton = (
    <>
      <div
        className="middle-menu__user-logout"
        onClick={() => handleLogout()}
        title="로그아웃 하기"
      >
        <Link to="/" className="middle-menu__user-logout__link">
          LOGOUT
        </Link>
      </div>
      <div className="middle-menu__user-mypage">
        <Link to="/mypage" className="middle-menu__user-mypage__link">
          MYPAGE
        </Link>
      </div>
    </>
  );
  return (
    <div id="main" data-barba="wrapper">
      <div className="main-container">
        <header className="main-header">
          <div className="header__container">
            <div className="header__head">
              <div
                className={`header__head-circle ${
                  menu === "active" ? "--active" : ""
                }`}
                onClick={menuToggle}
              >
                <div className="header__head-circle-in">
                  <img src={logo} alt="logo" className="head-circle-logo" />
                </div>
              </div>
            </div>
          </div>
        </header>

        <div className={`main-ground ${menu === "active" ? "--active" : ""}`}>
          <div
            className={`main-description  ${
              menu === "active" ? "--active" : ""
            }`}
          >
            문을 열고 당신의 2번째 인생을 시작하세요!
          </div>
          <div className="ground-left">
            <div className="ground-left__img-wrapper">
              <img
                src={mainChicken}
                alt="ground-left-img"
                className="ground-left__img"
              />
            </div>
            <div className="ground-left__title">
              <h1>BRAVO!!</h1>
            </div>
          </div>
          <div className={`middle-menu ${menu === "active" ? "--active" : ""}`}>
            <div className="middle-menu__bg">
              <img src={mainMiddle} alt="main-middle" />
            </div>
            <div className="middle-menu__analysis">
              <Link to="/anal" style={{ textDecoration: "none" }}>
                <div className="middle-menu__analysis-title">상권분석</div>
              </Link>
            </div>
            <div className="middle-menu__ranking">
              <Link to="/ranking" style={{ textDecoration: "none" }}>
                <div
                  className="middle-menu__ranking-title"
                  onClick={() => setFade("fade")}
                >
                  인기매장
                </div>
              </Link>
            </div>
            <div
              className={`middle-menu__description-analysis ${
                menu === "active" ? "--active" : ""
              }`}
            >
              상권분석에서 최적의 위치를
            </div>
            <div
              className={`middle-menu__description-ranking ${
                menu === "active" ? "--active" : ""
              }`}
            >
              인기매장에서 최고의 브랜드를
            </div>
            <div
              className={`middle-menu__description-main ${
                menu === "active" ? "--active" : ""
              }`}
            >
              당신의 2번째 인생을 시작해보세요
            </div>
            <div className="middle-menu__user"></div>
          </div>
          <div className={`ground-right ${fade ? { menuToggle } : ""}`}>
            <div className="ground-right__img-wrapper">
              <img
                src={mainPizza}
                alt="ground-right-img"
                className="ground-right__img"
              />
            </div>
            <div className="ground-right__title">
              <h1>
                SILVER
                <br />
                LIFE!!
              </h1>
            </div>
            <div className="ground-right__admin">
              {isLogin ? logoutbutton : loginbutton}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Main;
