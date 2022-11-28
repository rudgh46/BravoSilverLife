import React from 'react';
import './spinner.css'
import logo from '../assets/images/mainlogo.svg'

function spinner(props) {
  return (
    <div className='spinner'>
      <img src={logo} alt='logo'/>
    </div>
  );
}

export default spinner;