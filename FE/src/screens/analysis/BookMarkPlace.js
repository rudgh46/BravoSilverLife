import React, { useEffect, useState } from "react"
import './BookMarkPlace.css'

import need_login from '../../assets/AnalysisImages/logo-crying.svg'
import axios from "axios"

import empty_logo from '../../assets/AnalysisImages/logo-crying.svg'
import close_img from '../../assets/AnalysisImages/close_button_img.png'
import build_type from '../../assets/AnalysisImages/building.png'
import room_size_img from '../../assets/AnalysisImages/plans.png'
import floor_img from '../../assets/AnalysisImages/stairs.png'
import subway_img from '../../assets/AnalysisImages/subway.png'
import { BsFillBookmarkStarFill, BsLink45Deg } from "react-icons/bs"

import Rating from '@mui/material/Rating'

import { Chart } from "react-google-charts"
import person from '../../assets/AnalysisImages/person.png'

const BookMarkPlace = ({ optionDataList, userId, userBookMark }) => {
  const [itemDetailList, setItemDetailList] = useState([])
  const [itemDetailListSave, setItemDetailListSave] = useState([])

  const [isDetailOpen, setIsDetailOpen] = useState(-1)
  const [itemDetailData, setItemDetailData] = useState({})
  const [nearestStation, setNearestStation] = useState({})

  const [populationData, setPopulationData] = useState([])
  const [dayData, setDayData] = useState([])
  const [dayAvgData, setDayAvgData] = useState(0)
  const [perHourPopulationData, setPerHourPopulationData] = useState([])

  const [itemNearStoreScore, setItemNearStoreScore] = useState(0)
  const [itemNearStationScore, setItemNearStationScore] = useState(0)
  const [itemNearPopulationScore, setItemNearPopulationScore] = useState(0)

  const getItemNearStoreScore = async (place) => {
    const getNearStoreNumURL = `https://k7c208.p.ssafy.io/api/v1/store/stores?dong=${place}&category=${optionDataList.sector}`
    const nearStoreResponse = await axios.get(getNearStoreNumURL)

    if (nearStoreResponse.data.length >= 50) {
      setItemNearStoreScore(0.5)
    } else {
      setItemNearStoreScore(5 - ((Math.trunc(nearStoreResponse.data.length / 5)) * 0.5))
    }
  }

  const getNearStation = async (lng, lat) => {
    const getNearStationURL = 'https://dapi.kakao.com/v2/local/search/category.json'
    const myAppKey = '6166cc0d53447a16f521f4fbe7c3422c'

    const stationResponse = await axios.get(getNearStationURL, {
      headers: { Authorization: `KakaoAK ${myAppKey}` },
      params: {
        category_group_code: "SW8",
        x: lng,
        y: lat,
        radius: 10000,
        size: 1,
        sort: "distance",
      }
    })

    setNearestStation(stationResponse.data.documents[0])
    if (stationResponse.data.documents[0].distance >= 1000) {
      setItemNearStationScore(0.5)
    } else {
      setItemNearStationScore(5 - ((Math.trunc(stationResponse.data.documents[0].distance / 100)) * 0.5))
    }
  }

  const getExactlyDongName = async (lng, lat) => {
    const getDongNameURL = `https://dapi.kakao.com/v2/local/geo/coord2regioncode.json?x=${lng}&y=${lat}`
    const myAppKey = '6166cc0d53447a16f521f4fbe7c3422c'

    const response = await axios.get(getDongNameURL, {
      headers: { Authorization: `KakaoAK ${myAppKey}` }
    })

    getFloatPopulationData(response.data.documents[1].region_3depth_name)
    getItemNearStoreScore(response.data.documents[0].region_3depth_name)
  }

  const getFloatPopulationData = async (place) => {
    const getFloatPopulationURL = `https://k7c208.p.ssafy.io/api/v1/infra/popular?name=${place}`

    const response = await axios.get(getFloatPopulationURL)

    setPopulationData([
      ["Task", "Hours per Day"],
      ["주중", response.data.day],
      ["주말", response.data.weekend],
    ])

    setDayData([
      response.data.mon, response.data.tues, response.data.wed, response.data.thur, response.data.fri, response.data.sat, response.data.sun
    ])

    setDayAvgData(response.data.dayAvg)

    if (response.data.dayAvg < 500) {
      setItemNearPopulationScore(0.5)
    } if (response.data.dayAvg >= 5000) {
      setItemNearPopulationScore(5)
    } else {
      setItemNearPopulationScore((Math.trunc(response.data.dayAvg / 500)) * 0.5)
    }

    setPerHourPopulationData([
      ["Hour", "유동인구수", { role: "style" }],
      ["0~3", response.data.firstHour, colors[1]],
      ["4~7", response.data.secondHour, colors[2]],
      ["8~11", response.data.thirdHour, colors[3]],
      ["12~15", response.data.fourthHour, colors[4]],
      ["16~19", response.data.fifthHour, colors[5]],
      ["20~23", response.data.sixthHour, colors[6]]
    ])
  }

  const getDetailitem = async (itemNo, list) => {
    const getBookMarkItemDetailURL = `https://k7c208.p.ssafy.io/api/v1/estate/article-detail?articleNo=${itemNo}`
    const response = await axios.get(getBookMarkItemDetailURL)

    list.push(response.data)
  }

  useEffect(() => {
    let list = []

    if (userBookMark.length !== 0) {
      for (let i = 0; i < userBookMark.length;) {
        getDetailitem(userBookMark[i].articleNo, list)
        i += 1
      }
      setItemDetailList(list)
    } else;

    setItemDetailListSave(itemDetailList)
  }, [userBookMark])

  const getDetailInfo = async (e, index) => {
    setItemDetailData(itemDetailListSave[index])
    setIsDetailOpen(index)
    getNearStation(itemDetailListSave[index].longitude, itemDetailListSave[index].latitude)
    getExactlyDongName(itemDetailListSave[index].longitude, itemDetailListSave[index].latitude)
  }

  const deleteBookmarkData = async (e, userID, itemNo) => {
    if (userID === '') {
      alert('로그인이 필요한 서비스입니다.')
    } else {
      const deleteBookMarkURL = `https://k7c208.p.ssafy.io/api/v1/estate/article-bookmark?id=${userID}&articleNo=${itemNo}`
      await axios.delete(deleteBookMarkURL)
      setIsDetailOpen(-1)
      alert('북마크가 삭제되었습니다.')
    }
  }

  return (
    <div className="bookmark_wrap">
      {userId === '' && <div className="need_login_page_wrap">
        <img src={need_login} alt="needLoginWarnImg" />
        <div className="warn_message_wrap">로그인이 필요한 서비스입니다.</div>
      </div>}
      {userId !== '' && <div>
        {itemDetailListSave.length === 0 && <div className="empty_bookmark_wrap">
          <img src={empty_logo} className='empty_bookmark_img' />
          <div className="empty_bookmark_warning_message">북마크한 매물이 없습니다.</div>
        </div>}
        {itemDetailListSave.length !== 0 && <div>
          {
            itemDetailListSave.map((bookMarkItem, index) => {
              return (
                <div className='item_wrap' key={bookMarkItem.articleNo}
                  onClick={(e) => getDetailInfo(e, index)}
                >
                  <div id='mainTitle'>
                    {bookMarkItem.buildingTypeName} ({bookMarkItem.floor !== null ? bookMarkItem.floor : 1}층)
                  </div>
                  <div id='price'>
                    월세/보증금 (만원): {bookMarkItem.rentPrice !== null ? `${bookMarkItem.rentPrice}/${bookMarkItem.warrantPrice}` : `${bookMarkItem.warrantPrice} (매매)`}
                  </div>
                  <div id='floor'>
                    해당층/총층: {bookMarkItem.floor}/{bookMarkItem.maxFloor}층
                  </div>
                  <div id='size'>
                    계약/전용 면적 : {bookMarkItem.area1}㎡/{bookMarkItem.area2}㎡
                  </div>
                </div>
              )
            })
          }
        </div>}

        {isDetailOpen !== -1 && <div className="item_detail_wrap">
          <img src={close_img} className="close_button_img" onClick={() => setIsDetailOpen(-1)} />
          <div className="title_wrap">
            <div id="price">{itemDetailListSave[isDetailOpen].rentPrice !== null ? `월세 ${itemDetailListSave[isDetailOpen].rentPrice} / 보증금 ${itemDetailListSave[isDetailOpen].warrantPrice}` : `매매 ${itemDetailListSave[isDetailOpen].warrantPrice}`}</div>
            <div className="near_station_wrap">
              <img src={subway_img} />
              <div id="station">{nearestStation.place_name}, {nearestStation.distance}m</div>
            </div>
          </div>
          <div className="item_intro_wrap">
            <div className="building_type_wrap">
              <img src={build_type}
                className="building_img_wrap" />
              <div id="title">건물 형태</div>
              <div id="data"> {itemDetailData.buildingTypeName}</div>
            </div>
            <div className="building_type_wrap">
              <img src={floor_img}
                className="building_img_wrap" />
              <div id="title">층수</div>
              <div id="data"> {itemDetailData.floor} / {itemDetailData.maxFloor} 층</div>
            </div>
            <div className="building_type_wrap">
              <img src={room_size_img}
                className="building_img_wrap" />
              <div id="title">면적</div>
              <div id="data"> {itemDetailData.area1} ㎡</div>
            </div>
          </div>
          <div className="detail_icon_wrap">
            <BsFillBookmarkStarFill className="bookmark_icon_wrap" size="24" color="red"
              onClick={(e) => deleteBookmarkData(e, userId, itemDetailData.articleNo)}
            />
            <BsLink45Deg className="link_icon_wrap" size="24" color="black"
              onClick={() => window.open(`${itemDetailData.cpPcArticleUrl}`)} />
          </div>
          <div className="detail_border_wrap">
            <hr />
          </div>
          <div className="chart_title">
            <img src={person} className='person_img' />
            일일평균 유동인구는 {dayAvgData} 명 입니다.
          </div>
          <div className="chart_wrap">
            <Chart
              chartType="PieChart"
              data={populationData}
              options={options}
              width={"200px"}
              height={"200px"}
            />
            <ul className="daydata_wrap">
              <li>ㆍ월요일 : {dayData[0]} %</li>
              <li>ㆍ화요일 : {dayData[1]} %</li>
              <li>ㆍ수요일 : {dayData[2]} %</li>
              <li>ㆍ목요일 : {dayData[3]} %</li>
              <li>ㆍ금요일 : {dayData[4]} %</li>
              <li>ㆍ토요일 : {dayData[5]} %</li>
              <li>ㆍ일요일 : {dayData[6]} %</li>
            </ul>
          </div>
          <div className="chart_perhour_wrap">
            <div className="perhour_title">시간대별 유동인구</div>
            <Chart chartType="ColumnChart" width="300px" height="300px" data={perHourPopulationData}
              options={hourOptions} />
          </div>
          <div className="detail_border_wrap">
            <hr />
          </div>
          <div className="item_score_wrap">
            <div className="item_score_main_title">평가지수</div>
            <div className="all_score_wrap">
              <div className="compete_score_wrap">
                <div className="sub_title">역세권</div>
                <Rating name="read-only" sx={{
                  "& .MuiRating-iconFilled": { color: "#B48158" }
                }} value={itemNearStationScore} precision={0.5} readOnly />
              </div>
              <div className="compete_score_wrap">
                <div className="sub_title">유동인구</div>
                <Rating name="read-only" sx={{
                  "& .MuiRating-iconFilled": { color: "#B48158" }
                }} value={itemNearPopulationScore} precision={0.5} readOnly />
              </div>
              <div className="compete_score_wrap">
                <div className="sub_title">근처점포</div>
                <Rating name="read-only" sx={{
                  "& .MuiRating-iconFilled": { color: "#B48158" }
                }} value={itemNearStoreScore} precision={0.5} readOnly />
              </div>
            </div>
          </div>
        </div>}

      </div>}
    </div>
  )
}

const hourOptions = {
  legend: "none",
}

const options = {
  legend: "none",
  pieSliceText: "label",
  pieStartAngle: 0,
  is3D: false,
  slices: {
    0: { color: "#915B37" },
    1: { color: "#D7AB7F" },
  },
}

const colors = [
  '#FEFAEF', '#FEF4DF', '#FDECCF', '#FCE4C3', '#FBD8AF', '#D7AB7F', '#B48158',
]

export default BookMarkPlace