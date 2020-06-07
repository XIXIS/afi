import React from 'react';
import {Link, withRouter} from "react-router-dom";
import M from 'materialize-css';
// import {bindActionCreators} from "redux";
// import * as actions from "../../store/actions";
import {connect} from "react-redux";


class MobileSideNavAuthenticated extends React.Component {

  sidenav = React.createRef();

  componentDidMount() {
    this.props.loadFarms();
    M.Sidenav.init(this.sidenav.current, {});
  }


  render() {

    // let midMenuStyle = {fontSize: 16, color: '#00bc5c'};
    let {location, user} = this.props;

    return (
      <ul className="sidenav sidenav-grey" id="mobile-screen" ref={this.sidenav}>
        <li><div style={{marginTop: 40, marginBottom: 40}}/></li>
        <li className=''>
          <Link to='#!'>
            <div style={{
              width: 120,
              height: 120,
              marginTop: 30,
            }} className='black circle'>
              <img src="/images/user-white.svg" alt=""
                   style={{width: 70, marginLeft: 25, marginTop: 20}}/>
            </div>
            <span className='white-text' style={{marginLeft: 10}}>{user.fullName}</span>
          </Link>
        </li>
        <li>
          <div style={{marginTop: 140, marginBottom: 30, height: 0.5, width: 235, marginLeft: 33}} className='grey lighten-2'/>
        </li>
        <li>
          <Link to="/dashboard" style={{color: '#00bc5c', fontSize: '1.1rem'}}>
            <img src="/images/dashboard-02.svg" style={{width: 20, marginRight: 10, marginTop: 13}} alt=""
                 className='left'/>
            <span className={`${location.pathname.includes('dashboard') ? 
              'sidenav-green-text' : 'white-text'} left`}>My Dashboard</span>
          </Link>
        </li>
        <li>
          <Link to="/account-settings" style={{color: '#00bc5c', fontSize: '1.1rem'}}>
            <img src="/images/settings.svg" style={{width: 20, marginRight: 10, marginTop: 13}} alt=""
                 className='left'/>
            <span className={`${location.pathname.includes('account-settings') ? 
              'sidenav-green-text' : 'white-text'} left`}>Account Settings</span>
          </Link>
        </li>
        <li>
          <Link to="/wallet" style={{color: '#00bc5c', fontSize: '1.1rem'}}>
            <img src="/images/menu-wallet.svg" style={{width: 20, marginRight: 10, marginTop: 13}} alt=""
                 className='left'/>
            <span
              className={`${location.pathname.includes('wallet') ? 
                'sidenav-green-text' : 'white-text'} left`}>Wallet</span>
          </Link>
        </li>
        <li>
          <Link to="/farms" style={{color: '#00bc5c', fontSize: '1.1rem'}}>
            <img src="/images/farm.svg" style={{width: 20, marginRight: 10, marginTop: 13}} alt="" className='left'/>
            <span
              className={`${location.pathname.includes('farms') ? 
                'sidenav-green-text' : 'white-text'} left`}>Farms</span>
          </Link>
        </li>
        <li><div style={{marginTop: 15, marginBottom: 15, height: 1, width: 235, marginLeft: 33}} className='grey lighten-2'/></li>
        <li>
          <Link className='black-text badge2'
                style={{paddingTop: 8, height: 64}}
                data-badge={this.props.cart.length}
                to="/cart">
            <img src={`/images/shopping-cart-white.svg`}
                 alt=""
                 style={{display: 'inline-block'}}/>
          </Link>
        </li>
        <li onClick={this.props.logout}>
          <Link to='#!' style={{color: '#00bc5c', fontSize: '1.1rem'}}>
            <img src="/images/logout.svg" style={{width: 20, marginRight: 10, marginTop: 13}} alt="" className='left'/>
            <span className='left'>Logout</span>
          </Link>
        </li>

      </ul>
    )
  }

}


// function mapDispatchToProps(dispatch) {
//   return bindActionCreators({
//     loadFarms: actions.loadFarms,
//     logout: actions.logout
//   }, dispatch);
// }

function mapStateToProps({auth, cart, farm}) {
  return {
    isLoggedIn: auth.isLoggedIn,
    farms: farm.data,
    cart: cart.data,
    user: auth.data,
  }
}

export default withRouter(connect(mapStateToProps, {})(MobileSideNavAuthenticated));