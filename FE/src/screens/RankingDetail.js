import React, { useEffect, useState } from "react";
import "./RankingDetail.css";
import axios from "axios";
import { useParams, useNavigate } from "react-router-dom";
import ChartClosureRates from "../components/ChartClosureRates.js";
import ChartFranchiseeCount from "../components/ChartFranchiseeCount";
import ChartCount from "../components/ChartCount";
import NotFound from "./NotFound.js";
import logo from "../assets/images/mainlogo.svg";
import dummy from "../assets/images/dummy-img.png";

function RankingDetail() {
  const [info, setInfo] = useState();
  const { id } = useParams();
  const navigate = useNavigate();
  const goHome = () => {
    navigate("/");
  };
  // const getBrandDetail = async () => {
  //   try {
  //     const { data } = await axios.get(
  //       "https://k7c208.p.ssafy.io/api/v1/franchise/franchise-detail",
  //       { params: { id: id } }
  //     );
  //     setInfo(data);
  //   } catch (error) {
  //     console.log(error);
  //   }
  // };
  useEffect(() => {
    // getBrandDetail();
    axios({
      url: "https://k7c208.p.ssafy.io/api/v1/franchise/franchise-detail",
      method: "get",
      params: { id: id }
    })
      .then(res => {
        setInfo(res.data);
        // console.log(res.data);
      })
      .catch(err => {
        console.log(err);
      });
  }, []);
  return (
    <div id="rankingdetail">
      {!info?.name ? (
        <NotFound />
      ) : (
        <div className="rankingdetail-container">
          <div className="ranking-header">
            <div className="header__circle-in" onClick={goHome}>
              <img src={logo} alt="logo" className="circle-logo" />
            </div>
            <div className="img-wrapper">
              {!info?.logoUrl ? (
                <img src={dummy} alt="dummylogo" />
              ) : (
                <img src={info?.logoUrl} alt="logo" />
              )}
            </div>
            <div className="info-menu">
              <div className="title">브랜드명 : {info?.name}</div>
              <div className="monthly-sale">
                평균 총 매출액 : {info?.monthlySales.toString().substr(0, 4)}
                만원
              </div>
              <div className="total-cost">
                평균 창업 총 비용 :
                {info?.initialCost.total.toString().substr(0, 4)}
                만원
              </div>
            </div>
          </div>
          <hr />
          <div className="rankingdetail-body">
            <div className="chartclosurerates">
              <p>브랜드의 폐점률 최근 5개년 추이</p>
              {<ChartClosureRates info={info} />}
            </div>
            <div className="chartfranchiseecount">
              <p>브랜드의 점포 수 변화</p>
              {<ChartFranchiseeCount info={info} />}
            </div>
            <div className="chartcount">
              <p>브랜드의 개점 수 및 폐점 수 추이</p>
              {<ChartCount info={info} />}
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default RankingDetail;
