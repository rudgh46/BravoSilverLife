import React, { useState, useEffect } from "react";
import "./BrandCard.css";
import axios from "axios";
import { Link } from "react-router-dom";
import Col from "react-bootstrap/Col";
import Row from "react-bootstrap/Row";
import dummy from "../assets/images/mainlogo.svg";

function BrandCard({ type, category }) {
  const [card, setCard] = useState([]);
  useEffect(() => {
    axios({
      url: `https://k7c208.p.ssafy.io/api/v1/franchise/franchise-${type}`,
      method: "get",
      params: { category: category }
    })
      .then(res => {
        setCard(res.data);
      })
      .catch(err => {
        console.log(err);
      });
  }, [category, type]);
  return (
    <div id="brand-card">
      <div className="card-list">
        <Row xs={1} md={2} className="g-4">
          {card.map((data, idx) => (
            <Col key={idx}>
              <Link
                style={{
                  textDecoration: "none",
                  color: "black",
                  fontWeight: "bold"
                }}
                to={`/ranking/detail/${data.registrationNumber}`}
              >
                <div
                  className="card"
                  style={{
                    display: "flex",
                    flexDirection: "row"
                  }}
                >
                  {/* <div className="card-left"> */}
                  <div className="card__num">{idx + 1}</div>
                  <div className="card__img-wrapper">
                    {data.url ? (
                      <img
                        src={data.url}
                        alt="brand-img"
                        className="card__img"
                      />
                    ) : (
                      <img src={dummy} alt="brand-img" className="card__img" />
                    )}
                  </div>
                  {/* </div> */}
                  <div className="card-right">
                    <div className="card__name">{data.name}</div>
                    <div className="card__cnt">매장 {data.count}개</div>
                  </div>
                </div>
              </Link>
            </Col>
          ))}
        </Row>
      </div>
    </div>
  );
}

export default BrandCard;
