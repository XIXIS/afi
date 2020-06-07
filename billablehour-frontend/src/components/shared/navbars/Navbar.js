import React, {Component} from 'react';
import {Link, withRouter} from 'react-router-dom';
import {connect} from "react-redux";
import {bindActionCreators} from 'redux';
import M from 'materialize-css';
import * as actions from "../../../store/actions";

// import MobileSideNavAuthenticated from "./MobileSideNavAuthenticated";

class Navbar extends Component {

  constructor(props) {
    super(props);
    this.dropdown = React.createRef();
    this.sidenav = React.createRef();
    this.logout = this.logout.bind(this)
  }

  componentDidMount() {
    M.Dropdown.init(this.dropdown.current, {
      hover: true,
      coverTrigger: false
    });
  }

  logout(){
    this.props.logout();
    this.props.history.push("/signin");
  }


  render() {

    let {user} = this.props;

    return (

      <div>
        <div className="navbar-fixed">
          <nav className='white reg' style={{borderBottom: '1px solid #e0e0e0'}}>
            <div className="nav-wrapper body-padding">
              <div className="container" style={{width: '90%'}}>
                <div className="row">
                  <div className="col s12">
                    <Link to="#"
                          data-target="mobile-screen"
                          className={`sidenav-trigger show-on-small`}>
                      <i className={`waves-effect waves-green waves-circle material-icons black-text`}>menu</i>
                    </Link>
                    <Link to='/' className="brand-logo main-green-text">
                      <img src="/images/afi.png" className='left' style={{width: 120, marginTop: 5}} alt=""/>
                      <span className='left hide-on-small-only'
                            style={{fontWeight: 'bold', marginLeft: 10, fontSize: '1.5rem'}}>Billable Hours</span>
                    </Link>

                    <ul className="right">
                      <li>
                        {/*eslint-disable-next-line*/}
                        <a className='black-text dropdown-trigger'
                           ref={this.dropdown}
                           style={{paddingTop: 8, height: 64}}
                           data-target='dropdown1'>
                          <img src="/images/user-black.svg" alt=""
                               style={{display: 'inline-block', marginRight: 10}}/>
                          <span style={{lineHeight: '1.7', display: 'inline-block', marginRight: 10}}>
                              <span style={{display: 'block', fontSize: 12}}>Hello,</span>
                              <span style={{display: 'block', fontSize: 12}}>{user ? user.firstName: ''}</span>
                            </span>
                          <img src="/images/down-arrow.svg" alt=""
                               style={{display: 'inline-block', width: 15}}/>
                        </a>
                      </li>
                    </ul>

                  </div>
                </div>
              </div>
            </div>
          </nav>
        </div>

        <ul id="dropdown1"
            className="dropdown-content">
          <li><Link to="/app/dashboard" className='grey-text' style={{fontWeight: 300}}>My Dashboard</Link></li>
          <li onClick={this.logout}><span className='grey-text' style={{fontWeight: 300}}>Logout</span></li>
        </ul>


        {/*<MobileSideNavAuthenticated/>*/}


      </div>

    );

  }

}

function mapDispatchToProps(dispatch) {
  return bindActionCreators({
    logout: actions.logout
  }, dispatch);
}

function mapStateToProps({auth}) {
  let user = auth.user
  if (typeof user !== "object") user = user ? JSON.parse(user) : null;
  // console.log(user)
  return {user}
}

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Navbar));