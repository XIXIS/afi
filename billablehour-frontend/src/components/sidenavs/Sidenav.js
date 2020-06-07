import React, {Component} from 'react';
import {Link, withRouter} from 'react-router-dom';
import {connect} from "react-redux";
import {bindActionCreators} from 'redux';
import M from 'materialize-css';
import * as actions from "../../store/actions";

class SideNav extends Component {

  constructor(props) {
    super(props);
    this.dropdown = React.createRef();
  }

  componentDidMount() {
    M.Dropdown.init(this.dropdown.current, {
      hover: true,
      coverTrigger: false
    });
  }


  render() {

    const {user, location} = this.props;

    return (

      <div style={{width: '100%'}}>
        <div className="card sidenav-grey"
             style={{borderRadius: 10, padding: '12px 40px'}}>
          <div className='black circle valign-wrapper'
               style={{
                 width: '100%',
                 height: 220,
                 margin: '30px auto 10px',
               }}>
            <img src="/images/user-white.svg" alt=""
                 style={{
                   width: '60%',
                   margin: 'auto'
                 }}
            />
          </div>
          <p className='center-align white-text'
             style={{fontWeight: 300}}>{`${user ? `${user.firstName} ${user.lastName}` : ''}`}</p>

          <div style={{margin: '40px auto', height: 1,}} className='grey darken-2'/>

          <ul>
            <li style={{height: 50}}>
              <Link to={'/app/dashboard'}
                    className={`${location.pathname.includes('dashboard') ? 'sidenav-green-text' : 'white-text'}`}>
                <img src="/images/dashboard-02.svg" style={{width: 20, marginRight: 10}} alt="" className='left'/>
                <span style={{fontWeight: 300}}>Dashboard</span>
              </Link>
            </li>
            {
              user.userType.name === "ADMIN" &&
              <>
                <li style={{height: 50}}>
                  <Link to={'/app/users'}
                        className={`${location.pathname.includes('users') ? 'sidenav-green-text' : 'white-text'}`}>
                    <img src="/images/settings.svg" style={{width: 20, marginRight: 10}} alt="" className='left'/>
                    Users
                  </Link>
                </li>

                <li style={{height: 50}}>
                  <Link to={'/app/grades'}
                        className={`${location.pathname.includes('grades') ? 'sidenav-green-text' : 'white-text'}`}>
                    <img src="/images/settings.svg" style={{width: 20, marginRight: 10}} alt="" className='left'/>
                    Grades
                  </Link>
                </li>
              </>
            }
            <li style={{height: 50}}>
              <Link to={'/app/clients'}
                    className={`${location.pathname.includes('clients') ? 'sidenav-green-text' : 'white-text'}`}>
                <img src="/images/settings.svg" style={{width: 20, marginRight: 10}} alt="" className='left'/>
                Clients
              </Link>
            </li>
            <li style={{height: 50}}>
              <Link to={'/app/timesheets'}
                    className={`${location.pathname.includes('timesheets') ? 'sidenav-green-text' : 'white-text'}`}>
                <img src="/images/menu-wallet.svg" style={{width: 20, marginRight: 10}} alt="" className='left'/>
                Timesheets
              </Link>
            </li>
            {
              user.userType.name === "FINANCE_USER" &&
              <li style={{height: 50}}>
                <Link to={'/app/invoices'}
                      className={`${location.pathname.includes('invoices') ? 'sidenav-green-text' : 'white-text'}`}>
                  <img src="/images/farm.svg" style={{width: 20, marginRight: 10}} alt="" className='left'/>
                  Invoices
                </Link>
              </li>
            }
          </ul>
        </div>
      </div>

    );

  }

}


function mapDispatchToProps(dispatch) {
  return bindActionCreators({
    logout: actions.logout,
  }, dispatch);
}

function mapStateToProps({auth}) {

  let user = auth.user;
  if (typeof user !== "object") user = user ? JSON.parse(user) : null
  return {
    isLoggedIn: auth.isLoggedIn, user
  }
}

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(SideNav));