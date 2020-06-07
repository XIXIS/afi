import React from 'react';
import {Link} from "react-router-dom";
import M from 'materialize-css';


class MobileSideNav extends React.Component {

  sidenav = React.createRef();

  componentDidMount() {
    M.Sidenav.init(this.sidenav.current, {});
  }

  render() {

    let midMenuStyle = {fontSize: 16, color: '#00bc5c'};


    return (
      <ul className="sidenav sidenav-grey" id="mobile-screen" ref={this.sidenav}>
        <li><div style={{marginTop: 40, marginBottom: 40}}/></li>
        <li className=''>
          <Link to='/signin'>
            <span className='white-text'>You are not signed in.</span>
            <button style={{
              border: `2px solid #00bc5c`,
              borderRadius: 5,
              textTransform: 'unset',
              fontSize: 14,
              width: 150,
              color: '#00bc5c'
            }} className='btn z-depth-0 main-green white-text'>Log in/Sign up
            </button>
          </Link>
        </li>
        <li>
          <div style={{marginTop: 80, marginBottom: 30, height: 0.5, width: 235, marginLeft: 33}} className='grey lighten-2'/>
        </li>
        <li><Link className={`white-text`} to="/farms" style={midMenuStyle}>Farms</Link></li>

      </ul>
    )
  }

}

export default MobileSideNav;