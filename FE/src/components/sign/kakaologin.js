import React from "react";

const KakaoLogin = () => {
  const REST_API_KEY = "본인의 api 키";
  const REDIRECT_URI = "https://localhost:3000/kakao";
  const KAKAO_AUTH_URL = `https://kauth.kakao.com/oauth/authorize?client_id=${REST_API_KEY}&redirect_uri=${REDIRECT_URI}&response_type=code`;

  return (
    <h1>
      <a href={KAKAO_AUTH_URL}>Kakao Login</a>
    </h1>
  );
};

export default KakaoLogin;
