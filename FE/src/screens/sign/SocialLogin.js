import { useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import axios from "axios";

function OnSocialLogin() {
  const params = useParams();
  const navigate = useNavigate();
  const getuserdata = refreshtoken => {
    return axios
      .get("https://k7c208.p.ssafy.io/api/auth/userinfo", {
        headers: {
          RefreshToken: refreshtoken
        }
      })
      .then(response => {
        localStorage.setItem("userdata", JSON.stringify(response.data) || "");
      });
  };

  useEffect(() => {
    const tokens = params.params;
    const refresh_start = tokens?.search("refreshtoken=");
    const access_start = tokens?.search("accesstoken=");

    if (refresh_start === 0 && access_start) {
      const refreshtoken = tokens?.slice(refresh_start + 13, access_start - 1);
      const accesstoken = tokens?.slice(access_start + 12, tokens.length);
      const user = JSON.stringify({
        success: true,
        code: 0,
        msg: "Success",
        accessToken: accesstoken,
        refreshToken: refreshtoken
      });
      localStorage.setItem("user", user || "");
      getuserdata(refreshtoken);
      navigate("/mypage");
    } else {
      navigate("/signin");
    }
  }, [navigate]);

  return <div></div>;
}

export default OnSocialLogin;
