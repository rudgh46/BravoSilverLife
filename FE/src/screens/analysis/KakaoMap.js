import React, { useEffect, useState } from 'react'
import axios from 'axios'
import './KakaoMap.css' // 지도 CSS

const KakaoMap = ({ optionDataList, setDongName, setMapAreaYX, setClusterId, setClusterMaxPage }) => { // searchPlace = 검색할 장소를 나타냄
  const kakao = window['kakao']
  const [centerYX, setCenterYX] = useState([0, 0])

  const creatMap = () => {
    /// 지도를 생성하기 위한 기본 세팅 ///
    const container = document.getElementById('map')
    const center = (centerYX[0] === 0 && centerYX[1] === 0) ? new kakao.maps.LatLng(37.5518, 126.9881) : new kakao.maps.LatLng(centerYX[0], centerYX[1]);
    const options = {
      center,
      level: 5,
      draggable: true,
    }
    const map = new kakao.maps.Map(container, options)
    // 최대 축소 레벨 제한
    map.setMaxLevel(5)
    // 지도 오른쪽 위에 확대 스크롤바 추가
    const zoomControl = new kakao.maps.ZoomControl()
    map.addControl(zoomControl, kakao.maps.ControlPosition.RIGHT)
    // 지도 오른쪽 위에 스카이뷰로 전환하는 버튼 추가
    const mapTypeControl = new kakao.maps.MapTypeControl()
    map.addControl(mapTypeControl, kakao.maps.ControlPosition.TOPRIGHT)
    /// 기본 세팅 완료 ///


    /// 사용하는 함수 정의 구간 시작 ///
    const displayMarker = (posData) => {
      const content = `
        <div class ="label">
          <span class="left"></span>
          <span class="center"
            style=${posData.count > 25 ? "font-size:20px;font-weight:bold" : "font-size:15px;font-weight:bold"}
          >
            ${posData.count}
          </span>
          <span class="right"></span>
        </div>
      `

      const circle = new kakao.maps.Circle({
        center: new kakao.maps.LatLng(posData.latitude, posData.longitude), // 원의 중심좌표입니다
        radius: (posData.count > 60 ? 100 :
          posData.count > 40 ? 75 :
            posData.count > 20 ? 50 : 25), // 원의 반지름입니다 m 단위 이며 선 객체를 이용해서 얻어옵니다
        strokeWeight: 1, // 선의 두께입니다
        strokeColor: (posData.count > 60 ? '#FF0000' :
          posData.count > 40 ? '#FF7F00' :
            posData.count > 20 ? '#FFFF00' : '#008000'), // 선의 색깔입니다
        strokeOpacity: 1, // 선의 불투명도입니다 0에서 1 사이값이며 0에 가까울수록 투명합니다
        strokeStyle: 'solid', // 선의 스타일입니다
        fillColor: (posData.count > 60 ? '#FF3333' :
          posData.count > 40 ? '#FFA333' :
            posData.count > 20 ? '#FFFF59' : '#36FF33'), // 채우기 색깔입니다
        fillOpacity: 0.4  // 채우기 불투명도입니다
      })

      const customOverlay = new kakao.maps.CustomOverlay({
        map: map,
        position: new kakao.maps.LatLng(posData.latitude, posData.longitude),
        content: content
      })

      circle.setMap(map)
      customOverlay.setMap(map)

      kakao.maps.event.addListener(circle, 'click', function () {
        setClusterId(posData.markerId)
        setClusterMaxPage((posData.count / 20) < 1 ? 1 : (posData.count % 20) === 0 ? posData.count / 20 : (posData.count / 20) + 1)
      })
    }


    async function getClusterDataList(name, posYX) {
      const clustersURL = `https://k7c208.p.ssafy.io/api/v1/estate/clusters?dongName=${name}&rentPriceMin=${optionDataList.monthly[0] * 10}&rentPriceMax=${optionDataList.monthly[1] * 10}&priceMin=${optionDataList.sale[0] * 100}&priceMax=${optionDataList.sale[1] * 100}&areaMin=${optionDataList.room[0] * 4}&areaMax=${optionDataList.room[1] * 4}&leftLon=${posYX[0]}&rightLon=${posYX[1]}&topLat=${posYX[2]}&bottomLat=${posYX[3]}`
      const response = await axios.get(clustersURL)

      for (let i = 0; i < response.data.length; i++) {
        displayMarker(response.data[i])
      }
    }

    const getExactlyDongName = async (posYX) => {
      // 정확한 동이름을 얻어오기 위한 API
      const getDongNameURL = `https://dapi.kakao.com/v2/local/geo/coord2regioncode.json?x=${posYX[1]}&y=${posYX[0]}`
      const myAppKey = '6166cc0d53447a16f521f4fbe7c3422c'

      const response = await axios.get(getDongNameURL, {
        headers: { Authorization: `KakaoAK ${myAppKey}` }
      })

      setDongName(response.data.documents[0].region_3depth_name)
      return response.data.documents[0].region_3depth_name
    }

    async function placesSearchCB(data, status, pagination) {
      if (status === kakao.maps.services.Status.OK) { // 검색 결과가 존재하는 경우
        const bounds = new kakao.maps.LatLngBounds()

        for (let i = 0; i < data.length; i++) {
          // displayMarker(data[i]);
          bounds.extend(new kakao.maps.LatLng(data[i].y, data[i].x))
        }

        map.setBounds(bounds) // 검색된 지역을 지도의 중심으로 설정

        const boundsCenter = [(bounds.qa + bounds.pa) / 2, (bounds.ha + bounds.oa) / 2]

        setCenterYX(boundsCenter)
        setMapAreaYX([bounds.ha, bounds.oa, bounds.pa, bounds.qa])

        const dongNameData = await getExactlyDongName(boundsCenter)

        getClusterDataList(dongNameData, [bounds.ha, bounds.oa, bounds.pa, bounds.qa])
      }
    }

    /// 함수 정의 구간 끝 ///

    if (optionDataList.place !== '') {
      const ps = new kakao.maps.services.Places()
      ps.keywordSearch(optionDataList.place, placesSearchCB)
    }
  }

  useEffect(() => {
    creatMap()
  }, [optionDataList])

  return (
    <div id="map" />
  )
}

export default KakaoMap