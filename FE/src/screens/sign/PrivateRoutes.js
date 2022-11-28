import authHeader from "./AuthHeader";
import axios from 'axios'



const getUsers = () => {
  return axios.get('https://k7c208.p.ssafy.io:8080/auth/userinfo', { headers: authHeader() });
}

const getProducts = () => {
  return axios.get('http://localhost:8000/products/')
}


const privateRoutes = {
  getUsers,
  getProducts
}

export default privateRoutes