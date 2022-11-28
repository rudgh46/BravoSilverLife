import React, { useState, useEffect } from "react";
import "./MyPage.css";
import axios from "axios";
import banner from "../assets/images/mypage-banner.jpg";
import logoimg from "../assets/AnalysisImages/BSL_Logo.png";
import { Link, useNavigate } from "react-router-dom";
import authService from "./sign/AuthService";

function MyPage() {
  const [userNickname, setUserNickname] = useState("");
  const [userId, setUserId] = useState("");
  const [userphoneNumber, setUserphonNumber] = useState("");
  const [newphoneNumber, setNewphonNumber] = useState("");
  const [authNumber, setAuthNumber] = useState("");

  const getuserphoneNumber = loginUser => {
    if (loginUser.phoneNumber !== null) {
      setUserphonNumber(loginUser.phoneNumber);
    } else { setUserphonNumber("") }
  };

  const navigate = useNavigate();

  const getuserId = loginUser => {
    setUserId(loginUser.id);
  };

  const getuserNickname = loginUser => {
    setUserNickname(loginUser.nickname);
  };

  const handlePutphoneNumber = async e => {
    e.preventDefault();
    try {
      await authService
        .putPhoneNumber(userId, newphoneNumber, authNumber)
        .then(response => {
          if (response === 1) {
            const userdata = JSON.stringify({
              id: userId,
              nickname: userNickname,
              phoneNumber: newphoneNumber
            });
            localStorage.removeItem("userdata");
            localStorage.setItem("userdata", userdata);

            navigate("/");
          } else {
            alert("인증번호를 확인해 주세요");
          }
        });
    } catch (error) {
      console.log(error);
    }
  };

  const sendAuthNumber = () => {
    if (newphoneNumber !== "") {
      axios
        .get(`https://k7c208.p.ssafy.io/api/auth/check/${newphoneNumber}`, {
          newphoneNumber,
          authNumber
        })
        .then(res => {
          setAuthNumber(res.data.authNumber);
          console.log(res);
        })
        .catch(error => {
          if (error.response.status === 409) {
            alert("중복된 번호입니다. 다른번호를 입력해 주세요");
          }
          console.log(error.response);
        });
    } else {
      alert("번호를 입력해주세요!!");
    }
  };

  useEffect(() => {
    if (userphoneNumber === null) {
      setUserphonNumber("");
    }

    if (localStorage.getItem("userdata") !== null) {
      getuserphoneNumber(JSON.parse(localStorage.getItem("userdata")));
      getuserId(JSON.parse(localStorage.getItem("userdata")));
      getuserNickname(JSON.parse(localStorage.getItem("userdata")));
    }
  });

  // onSubmit={handleSignUp}

  return (
    <div id="my-page">
      <div className="mypage-banner">
        <img src={banner} alt="banner" />
      </div>
      <div className="mypage-container">
        <form onSubmit={handlePutphoneNumber} className="profileform">
          <Link to="/" style={{ textDecoration: "none", color: "black" }}>
            <h1 className="title-h1">
              <img src={logoimg} alt="logoimg" />
            </h1>
          </Link>
          <div className="info-id">아이디 : {userId}</div>
          <div className="info-phone">전화번호 : {userphoneNumber}</div>
          <input
            className="newphoneinput"
            title="새로운 핸드폰 번호를 입력해주세요"
            type="string"
            placeholder={userphoneNumber}
            value={newphoneNumber}
            onChange={e => {
              setNewphonNumber(e.target.value);
            }}
            required
          />
          <input
            className="authnumberinput"
            title="인증번호를 입력해주세요"
            type="string"
            placeholder="인증번호"
            value={authNumber}
            onChange={e => {
              setAuthNumber(e.target.value);
            }}
            required
          />
          {/* <div className="info__name">{userphoneNumber}</div> */}
          {/* <div className="info__number">{userInfo.nickname}</div>
          <div className="info__password">{userInfo.phonenumber}</div> */}
          <button
            className="mypage-getauthnumber"
            type="button"
            onClick={() => sendAuthNumber()}
          >
            인증번호받기
          </button>
          <button className="mypage-signupbutton" type="submit">
            휴대폰번호 수정
          </button>
        </form>
      </div>
    </div>
  );
}

export default MyPage;

