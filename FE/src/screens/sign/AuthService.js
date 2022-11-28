import axios from 'axios';


const signin = (id, password) => {
  return axios
    .post("https://k7c208.p.ssafy.io/api/auth/sign-in", {
      id, password
    })
    .then((response) => {
      if (response.data.accessToken !== "") {
        localStorage.setItem('user', JSON.stringify(response.data))
        // window.location.reload()
      }
      return response.data
    })
}


const signup = (id, nickname, password, phoneNumber, authNumber) => {

  return axios
    .post("https://k7c208.p.ssafy.io/api/auth/sign-up", {
      id, nickname, password, phoneNumber, authNumber
    })
    .then((response) => {
      if (response.data.token) {
        localStorage.setItem('user', JSON.stringify(response.data))
        // console.log(response.data.token)
      }
      return response.data
    })
}

const getAuthNumber = (phoneNumber) => {

  return axios
    .get(`https://k7c208.p.ssafy.io/api/auth/check/${phoneNumber}}`)
    .then((response) => {
      if (response.data.token) {
        localStorage.setItem('user', JSON.stringify(response.data))
      }
      return response.data
    })
}

const getCurrentUser = () => {
  return JSON.parse(localStorage.getItem('user'))
}

const putPhoneNumber = (id, newPhoneNumber, authNumber) => {

  return axios
    .put("https://k7c208.p.ssafy.io/api/user/profile/phonenumber", {
      id, newPhoneNumber, authNumber
    })
    .then((response) => {
      if (response.data.token) {
        localStorage.setItem('user', JSON.stringify(response.data))
      }
      return response.data
    })
}

const authService = {
  signin,
  signup,
  getCurrentUser,
  getAuthNumber,
  putPhoneNumber
}

export default authService