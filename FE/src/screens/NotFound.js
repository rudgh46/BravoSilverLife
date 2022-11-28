import React from "react";
import "./NotFound.css";
import { useNavigate } from "react-router-dom";
import cryinglogo from "../assets/AnalysisImages/logo-crying.svg";

function NotFound(props) {
  const navigate = useNavigate();
  const goHome = () => {
    navigate("/ranking");
  };
  return (
    <div id="notfound">
      <div className="notfound-logo">
        <img src={cryinglogo} alt="cryinglogo" onClick={goHome} />
        <br />
        <p>데이터가 존재하지 않습니다.</p>
      </div>
    </div>
  );
}

export default NotFound;
