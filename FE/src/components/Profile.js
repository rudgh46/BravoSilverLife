import React, { useEffect, useState } from "react";
import "./Profile.css";

function Profile(props) {
  const gangnam = ["강남1", "강남2", "강남3", "강남4"];
  const gangdong = ["강동1", "강동2", "강동3", "강동4"];
  const gangbook = ["강북1", "강북2", "강북3", "강북4"];
  const gangseo = ["강서1", "강서2", "강서3", "강서4"];
  const selectList = ["강남", "강동", "강북", "강서"];
  const [Selected, setSelected] = useState("");
  const [subSelected, setSubSelected] = useState();
  const handleSelect = e => {
    setSelected(e.target.value);
    if (e.target.value === "강남") {
      setSubSelected({ gangnam });
    } else if (e.target.value === "강동") {
      setSubSelected({ gangdong });
    } else if (e.target.value === "강북") {
      setSubSelected({ gangbook });
    } else if (e.target.value === "강서") {
      setSubSelected({ gangseo });
    }
    console.log(e.target.value);
    console.log(subSelected);
  };
  return (
    <div id="profile">
      <div className="profile__info">
        <p>이름 : 고요한</p>
        <p>전화번호 : 010-6637-1840</p>
        <p>이메일 : rdg9074@ssafy.com</p>
        <div className="profile__region">
          <p>관심지역</p>
          <select onChange={handleSelect} value={Selected}>
            {selectList.map((item, idx) => (
              <option value={item} key={idx}>
                {item}
              </option>
            ))}
          </select>
          <select>
            {(Selected === "강북" ? gangbook : gangdong).map(sub => (
              <option key={sub}>{sub}</option>
            ))}
          </select>
          <div className="profile__region-btn"></div>
        </div>
        <div className="profile__budget">
          <p>예산계획</p>
          <div className="profile__budget-btn">
            <button type="button">5천 이내</button>
            <button type="button">5천 ~ 1억</button>
            <button type="button">1억 ~ 1억 5천</button>
            <button type="button">1억 5천 ~ 2억</button>
          </div>
        </div>
        <div className="profile__sector">
          <p>관심업종</p>
          <div className="profile__sector-btn">
            <button type="button">치킨</button>
            <button type="button">피자</button>
            <button type="button">일식</button>
            <button type="button">한식</button>
            <button type="button">분식</button>
            <button type="button">양식</button>
          </div>
        </div>
        <div className="profile__schedule">
          <p>창업기간</p>
          <div className="profile__schedule-btn">
            <button type="button">3개월 이내</button>
            <button type="button">6개월 이내</button>
            <button type="button">1년 이내</button>
            <button type="button">1년 이상</button>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Profile;
