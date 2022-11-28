import React from "react"
import './SearchPlaceInput.css'

const SearchPlaceInput = ({ place, setPlace, submitData }) => {
  const textChange = (e) => { // 입력한 값이 변경될때마다 작동되는 함수
    setPlace(e.target.value) // 입력할 때마다 변화한 값을 적용
  }

  const pressEnter = (e) => { // Enter를 눌렀을 경우 작동하는 함수
    if (e.key === 'Enter') { // Enter를 눌렀을 경우
      if (place === '') { // 입력값이 없는 경우
        vibration(e.target) // 값을 입력하라는 경고 애니메이션 작동
      } else { // 입력값이 있는 경우
        submitData() // 카카오맵에 넘길 데이터 리스트 전송 함수
      }
    }
  }

  const vibration = (target) => { // 입력한 값이 없는 경우 경고하는 애니메이션 함수
    target.classList.add('vibration') // 클래스 추가

    setTimeout(function () { // 500ms 후 클래스 제거
      target.classList.remove('vibration')
    }, 500)
  }

  return (
    <div className="input_place_wrap">
      {/* 검색창 */}
      <input className='text_input_wrap' placeholder="장소 및 역이름 검색해주세요."
        type="text" value={place}
        onChange={textChange} onKeyPress={pressEnter}
      />
    </div>
  )
}

export default SearchPlaceInput