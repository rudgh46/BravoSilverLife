import React, { useState } from 'react'
import "./SignUp.css"
import authService from './sign/AuthService'
import { Link, useNavigate } from 'react-router-dom'
import axios from 'axios'
import logoimg from "../assets/AnalysisImages/BSL_Logo.png"
import signupbanner from "../assets/images/signup-banner.jpg"

const SignUp = () => {
  const [id, setId] = useState("")
  const [nickname, setNickname] = useState("")
  const [password, setPassword] = useState("")
  const [authNumber, setAuthNumber] = useState("")
  const [phoneNumber, setPhoneNumber] = useState("")

  const navigate = useNavigate()

  const sendAuthNumber = () => {
    if (phoneNumber !== "") {
      axios.get(`https://k7c208.p.ssafy.io/api/auth/check/${phoneNumber}`, {
        phoneNumber, authNumber
      })
        .then((response) => {
          setAuthNumber(response.data.authNumber)
          console.log((response));
        })
        .catch(error => {
          if (error.response.status === 409) {
            alert("중복된 번호입니다. 다른번호를 입력해 주세요")
          }
          console.log(error.response)
        })
    } else { alert("번호를 입력해주세요!!") };
  }

  const handleSignUp = async (e) => {
    e.preventDefault();
    try {
      await authService.signup(id, nickname, password, phoneNumber, authNumber)
        .then((response) => {
          navigate('/signin')
          window.location.reload()
        }
        )
    } catch (error) {
      console.log(error)
    }
  }

  return (
    <div id="signup-page" >
      <div className="signup-banner">
        <img src={signupbanner} alt="banner"/>
      </div>
      <div className='signupdiv'>
        <form onSubmit={handleSignUp} className="signupform">
          <Link to="/" style={{ textDecoration: 'none', color: "black" }}>
            <h1 className="title-h1" title="메인화면으로 돌아가기">
              <img src={logoimg} alt="logoimg" />
            </h1>
          </Link>
          <input
            className="signinput"
            title="닉네임을 입력해주세요"
            type="text"
            name="nickname"
            placeholder="닉네임"
            value={nickname}
            required
            onChange={(e) => setNickname(e.target.value)}
          />
          <input
            className="signinput"
            title="아이디를 입력해주세요"
            type="text"
            name="id"
            placeholder="아이디"
            value={id}
            required
            onChange={(e) => setId(e.target.value)}
          />
          <input
            className="signinput"
            title="비밀번호를 입력해주세요"
            type="password"
            name="password"
            placeholder="비밀번호"
            value={password}
            required
            onChange={(e) => setPassword(e.target.value)}
          />
          <input
            className="signinput"
            title="휴대폰번호를 입력해주세요"
            type="number"
            name="phoneNumber"
            placeholder="휴대전화번호"
            value={phoneNumber}
            required
            onChange={(e) => setPhoneNumber(e.target.value)}

          />
          <input
            className="signinput"
            title="인증번호를 입력해주세요"
            type="number"
            name="authNumber"
            placeholder="인증번호"
            value={authNumber}
            required
            onChange={(e) => setAuthNumber(e.target.value)}
          />
          <button className="getauthnumber" type="button" onClick={() => sendAuthNumber()}>인증번호받기</button>
          <button className="signupbutton" type="submit" >회원가입</button>
        </form>
        <p className="gosignin-p">
          아이디가 있으신가요? <br />
          <Link className="gosigninLink" to="/signin" style={{ textDecoration: 'none', color: "black" }}>로그인하러가기</Link>
        </p>
      </div>
    </div >
  )
}

export default SignUp