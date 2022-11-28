import React, { useState } from 'react'
import "./SignIn.css"
import { Link, useNavigate } from 'react-router-dom'
import authService from "./sign/AuthService"
import axios from 'axios'
import logoimg from "../assets/AnalysisImages/BSL_Logo.png"
import loginbanner from "../assets/images/login-banner.jpg"


const SignIn = ({ setAuthenticate }) => {

  const [id, setId] = useState("")
  const [password, setPassword] = useState("")
  const [loginInfo, setLoginInfo] = useState(false)

  const navigate = useNavigate()

  const getuserdata = (refreshtoken) => {
    return axios
      .get("https://k7c208.p.ssafy.io/api/auth/userinfo", {
        headers: {
          RefreshToken: refreshtoken,
        },
      })
      .then((response) => {
        localStorage.setItem("userdata", JSON.stringify(response.data));
        console.log(response);
      })
  }
  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      await authService.signin(id, password).then(
        (response) => {
          const refreshtoken = response?.refreshToken
          setLoginInfo("True")
          getuserdata(refreshtoken);
          navigate('/'); // login 완료시 main page로 이동
          // window.location.reload()
        },
        (error) => {
          alert("아이디 혹은 비밀번호를 확인해주세요")
          console.log(error)
        }
      )

    } catch (err) {
      console.log(err)
    }

  }
  return (

    <section>
      <div id="signin-page">
        <div className="login-banner">
          <img src={loginbanner} alt="banner" />
        </div>
        <div className="signin-div">
          <form onSubmit={(event) => handleLogin(event)} className="signinform">
            <Link to="/" style={{ textDecoration: 'none', color: "black" }}>
              <h1 className="title-h1" title="메인화면으로 돌아가기">
                <img src={logoimg} alt="logoimg" />
              </h1>
            </Link>
            <input
              className="signinput"
              title="아이디를 입력해주세요"
              type="text"
              id="id"
              autoComplete="off"
              value={id}
              placeholder="아이디"
              onChange={(e) => {
                setId(e.target.value)
              }}
              required
            />
            <input
              className="signinput"
              title="비밀번호를 입력해주세요"
              type="password"
              id="password"
              value={password}
              placeholder="비밀번호"
              onChange={(e) => {
                setPassword(e.target.value);
              }}
              required
            />
            <button type="submit" className="signinButton">로그인</button>
          </form>
          <button className="kakaologinbutton" >
            <a href="http://k7c208.p.ssafy.io:8080/oauth2/authorization/kakao" style={{ textDecoration: "none", color: "black" }}>
              카카오톡 로그인하기
            </a>
          </button>
          <p className="gosignup-p">
            회원가입을 하시겠습니까?<br />
            <Link className="gosignupLink" to="/signup" style={{ textDecoration: "none", color: "black" }}>회원가입 하러가기</Link>
          </p>
        </div>
      </div>
    </section >
  )
}


export default SignIn