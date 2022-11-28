import React, { useState, useEffect } from "react";
import "./Ranking.css";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
// import ranking from "../assets/images/ranking-side.jpg";
import { Link } from "react-router-dom";
import BrandCard from "../components/BrandCard.js";
import Logo from "../assets/AnalysisImages/BSL_Logo.png";
import rankingLeft from "../assets/images/ranking-coffee.jpg";
import chicken from "../assets/images/chicken.png";
import pizza from "../assets/images/pizza.png";
import boonsik from "../assets/images/boonsik.png";
import fastfood from "../assets/images/fastfood.png";
import bakery from "../assets/images/bakery.png";
import dessert from "../assets/images/dessert.png";
import hansik from "../assets/images/hansik.png";
import ilsik from "../assets/images/ilsik.png";
import joongsik from "../assets/images/joongsik.png";
import yangsik from "../assets/images/yangsik.png";
import drink from "../assets/images/drink.png";
import fusion from "../assets/images/coffee.png";
import RankingDetail from "./RankingDetail.js";

function Ranking(props) {
  const [tagType, setTagType] = useState("popular");
  const [category, setCategory] = useState("치킨");
  const eventHandler = () => {
    setCategory({ ...category });
  };
  const menu = [
    "치킨",
    "피자",
    "분식",
    "패스트푸드",
    "제과제빵",
    "디저트",
    "한식",
    "중식",
    "일식",
    "양식",
    "퓨전",
    "주점"
  ];
  const menuImg = [
    chicken,
    pizza,
    boonsik,
    fastfood,
    bakery,
    dessert,
    hansik,
    joongsik,
    ilsik,
    yangsik,
    fusion,
    drink
  ];
  return (
    <div id="ranking">
      <div className="ranking-container">
        <div className="container-left">
          <Link to="/" className="home-btn__link">
            <div className="home-btn">
              <img src={Logo} alt="logo" />
            </div>
          </Link>
          <div className="select-category">창업 품목 선택하기</div>
          <div className="imgs-wrapper">
            <Row xs={1} sm={2} md={3}>
              {[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11].map(idx => (
                <div
                  key={idx}
                  className={`${category === menu[idx] ? "active" : null}`}
                >
                  <img
                    onClick={() => setCategory(menu[idx])}
                    src={menuImg[idx]}
                    alt={menu[idx]}
                    style={{ cursor: "pointer" }}
                  />
                  <p
                    className={`titless ${
                      category === menu[idx] ? "active" : null
                    }`}
                  >
                    {menu[idx]}
                  </p>
                </div>
              ))}
            </Row>
          </div>
        </div>
        <div className="container-right">
          <hr style={{ marginTop: "20px" }} />
          <div className="container-right__menu">
            <div className="menu-btn">
              <Link
                key="1"
                onClick={() => {
                  setTagType("popular");
                }}
                className={`menu-btn1 ${
                  tagType === "popular" ? "active" : null
                }`}
              >
                인기 순
              </Link>
              <Link
                key="2"
                onClick={() => {
                  setTagType("count");
                }}
                className={`menu-btn2 ${
                  tagType === "popular" ? null : "active"
                }`}
              >
                매장 수 순
              </Link>
            </div>
          </div>
          <hr />
          <div className="container-right__list">
            <BrandCard type={tagType} category={category} />
          </div>
        </div>
      </div>
    </div>
  );
}

export default Ranking;
