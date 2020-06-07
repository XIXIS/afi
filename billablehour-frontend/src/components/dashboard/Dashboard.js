import React, {Component} from "react";
import {bindActionCreators} from 'redux';
import {withRouter} from "react-router-dom";
import {connect} from "react-redux";
import SideNav from "../../components/sidenavs/Sidenav";
import * as actions from '../../store/actions';


class Dashboard extends Component {

  // props.clearOverlays();

  constructor(props) {
    super(props);
    this.state = {
      validationError: '',
      passwordShow: false,
      oldPasswordShow: false,
      populated: false
    };
    this.password = React.createRef();
    this.oldPassword = React.createRef();
    this.firstName = React.createRef();
    this.lastName = React.createRef();
    this.email = React.createRef();
    this.phone = React.createRef();
    this.errorDiv = React.createRef();
    this.pErrorDiv = React.createRef();
    this.successDiv = React.createRef();
    this.pSuccessDiv = React.createRef();
    this.updateProfile = this.updateProfile.bind(this);
    this.changePassword = this.changePassword.bind(this);
    this.passwordToggle = this.passwordToggle.bind(this);
    this.oldPasswordToggle = this.oldPasswordToggle.bind(this);
    this.hideError = this.hideError.bind(this);
  }

  componentDidMount() {
    this.props.profile();
  }

  changePassword(e) {
    e.preventDefault();
    e.stopPropagation();

    let user = {
      oldPassword: this.oldPassword.current.value,
      password: this.password.current.value
    };
    // console.log(user);
    this.props.changePassword({...user})
  }

  updateProfile(e) {
    e.preventDefault();
    e.stopPropagation();

    let user = {
      firstName: this.firstName.current.value,
      lastName: this.lastName.current.value,
      email: this.email.current.value,
      phone: this.phone.current.value,
      userTypeId: this.props.user.userType.id
    };
    // console.log(user);
    this.props.updateProfile({...user})

  }

  componentDidUpdate(prevProps, prevState, snapshot) {
    const {user} = this.props;
    const {populated} = this.state;
    if (!populated && user) {
      this.firstName.current.value = user.firstName;
      this.lastName.current.value = user.lastName;
      this.email.current.value = user.email;
      this.phone.current.value = user.phone;
      // M.FormSelect.init(document.getElementsByClassName(".select"), {})
    }
  }

  /**
   * use this method to hide the error message
   */
  hideError() {
    this.errorDiv.current.style.display = 'none'
  }

  hidePError() {
    this.pErrorDiv.current.style.display = 'none'
  }

  /**
   * use this method to hide the error message
   */
  hideSuccess() {
    this.successDiv.current.style.display = 'none'
  }

  hidePSuccess() {
    this.successDiv.current.style.display = 'none'
  }

  passwordToggle() {
    this.setState({
      passwordShow: !this.state.passwordShow
    })
  }


  oldPasswordToggle() {
    this.setState({
      oldPasswordShow: !this.state.oldPasswordShow
    })
  }


  render() {

    const {error, pcErrorMsg, pcSuccessMsg, success} = this.props;
    const {validationError} = this.state;

    const btnStyle = {
      backgroundColor: '#00d8ff',
      width: '100',
      borderRadius: 5,
      textTransform: 'none',
      fontWeight: 'bold'
    };


    return (
      <div>
        <div className="sidenav-grey"
             style={{zIndex: -999, height: 100, position: 'absolute', top: 60, left: 0, right: 0}}/>
        <div style={{position: 'relative'}}>
          <div className="container" style={{width: '90%'}}>
            <div className='row' style={{marginTop: 20}}>
              <div className="col s12 m4 l3">
                <SideNav/>
              </div>
              <div className="col s12 l9">
                <div className="row">
                  <div className="col s12">
                    <h5 className='white-text' style={{marginTop: 5, marginLeft: 15}}>Profile</h5>
                  </div>
                </div>

                <div className="col s12 l6">
                  <div className="card"
                       style={{
                         borderRadius: 10,
                         boxShadow: '0 2px 5px 0 rgba(0,0,0,0.16), 0 2px 10px 0 rgba(0,0,0,0.12)',
                       }}>
                    <div className="card-content" style={{paddingTop: 10, paddingRight: 30, paddingLeft: 30}}>

                      <form className="row" onSubmit={this.changePassword}>
                        <div>
                          <h4 style={{marginLeft: 10}}>Change Password</h4>
                          <p style={{marginLeft: 13, marginBottom: 10}}>
                            Use the form below to change your password.
                          </p>
                          {
                            (pcErrorMsg || validationError) &&
                            <div className="col s12" ref={this.pErrorDiv} style={{marginBottom: 20}}>
                              <p className='red lighten-4 white-text'
                                 style={{
                                   padding: '10px 20px',
                                   borderTopRightRadius: 20,
                                   borderBottomLeftRadius: 20
                                 }}>
                                <i className="material-icons left"
                                   style={{
                                     cursor: 'pointer',
                                     height: 50
                                   }}
                                   onClick={this.hidePError.bind(this)}>cancel</i>
                                <span className='red-text'>{pcErrorMsg || validationError}</span>
                              </p>
                            </div>
                          }

                          {
                            pcSuccessMsg &&
                            <div className="col s12" ref={this.pSuccessDiv} style={{marginBottom: 20}}>
                              <p className='green lighten-4 white-text'
                                 style={{
                                   padding: '10px 20px',
                                   borderTopRightRadius: 20,
                                   borderBottomLeftRadius: 20
                                 }}>
                                <i className="material-icons left"
                                   style={{
                                     cursor: 'pointer',
                                     height: 50
                                   }}
                                   onClick={this.hidePSuccess.bind(this)}>cancel</i>
                                <span className='grey-text text-darken-4'>{pcSuccessMsg}</span>
                              </p>
                            </div>
                          }

                          <div className="col s12" style={{marginBottom: 27}}>
                            <label htmlFor="old-password" style={{fontSize: 16}}>Old Password</label>
                            <div className='text-field-green div-text-input-password' style={{borderRadius: 5}}>
                              <input ref={this.oldPassword}
                                     type={this.state.oldPasswordShow ? 'text' : 'password'}
                                     required
                                     pattern=".{6,}"
                                     id='old-password'
                                     placeholder='6+ characters long'
                                     className='text-field-green text-input-password'
                                     style={{marginBottom: 0}}/>
                              <img src='/images/view.svg'
                                   alt='' className='right password-toggle-img'
                                   onClick={this.oldPasswordToggle}/>
                            </div>

                          </div>

                          <div className="col s12" style={{marginBottom: 27}}>
                            <label htmlFor="password" style={{fontSize: 16}}>New Password</label>
                            <div className='text-field-green div-text-input-password' style={{borderRadius: 5}}>
                              <input ref={this.password}
                                     type={this.state.passwordShow ? 'text' : 'password'}
                                     required
                                     pattern=".{6,}"
                                     id='password'
                                     placeholder='6+ characters long'
                                     className='text-field-green text-input-password'
                                     style={{marginBottom: 0}}/>
                              <img src='/images/view.svg'
                                   alt='' className='right password-toggle-img'
                                   onClick={this.passwordToggle}/>
                            </div>

                          </div>


                        </div>

                        <div className="col s12" style={{marginBottom: 8}}>
                          <a href="#!" style={{ float: 'right'}}>
                            <button type='submit'
                                    className="z-depth-0 waves-effect waves-light main-green white-text btn login-btn"
                                    style={{...btnStyle}}> Change&nbsp;
                            </button>
                          </a>
                        </div>


                      </form>
                    </div>
                  </div>
                </div>

                <div className="col s12 l6">
                  <div className="card"
                       style={{
                         borderRadius: 10,
                         boxShadow: '0 2px 5px 0 rgba(0,0,0,0.16), 0 2px 10px 0 rgba(0,0,0,0.12)',
                       }}>
                    <div className="card-content" style={{paddingTop: 10, paddingRight: 30, paddingLeft: 30}}>

                      <form className="row" onSubmit={this.updateProfile}>
                        <div>
                          <h4 style={{marginLeft: 10}}>Update Profile</h4>
                          <p style={{marginLeft: 10, marginBottom: 10}}>
                            Use the form below to update your profile.
                          </p>
                          {
                            (error || validationError) &&
                            <div className="col s12" ref={this.errorDiv} style={{marginBottom: 20}}>
                              <p className='red lighten-4 white-text'
                                 style={{
                                   padding: '10px 20px',
                                   borderTopRightRadius: 20,
                                   borderBottomLeftRadius: 20
                                 }}>
                                <i className="material-icons left"
                                   style={{
                                     cursor: 'pointer',
                                     height: 50
                                   }}
                                   onClick={this.hideError.bind(this)}>cancel</i>
                                <span className='red-text'>{error || validationError}</span>
                              </p>
                            </div>
                          }

                          {
                            success &&
                            <div className="col s12" ref={this.successDiv} style={{marginBottom: 20}}>
                              <p className='green lighten-4 white-text'
                                 style={{
                                   padding: '10px 20px',
                                   borderTopRightRadius: 20,
                                   borderBottomLeftRadius: 20
                                 }}>
                                <i className="material-icons left"
                                   style={{
                                     cursor: 'pointer',
                                     height: 50
                                   }}
                                   onClick={this.hideSuccess.bind(this)}>cancel</i>
                                <span className='grey-text text-darken-4'>{success}</span>
                              </p>
                            </div>
                          }

                          <div className="col s12" style={{marginBottom: 8}}>
                            <label htmlFor="email" style={{fontSize: 16}}>First Name</label>
                            <input ref={this.firstName}
                                   type="text"
                                   required
                                   id='first-name'
                                   name='first-name'
                                   placeholder='John'
                                   className='text-field-green text-input'/>
                          </div>

                          <div className="col s12" style={{marginBottom: 8}}>
                            <label htmlFor="email" style={{fontSize: 16}}>Last Name</label>
                            <input ref={this.lastName}
                                   type="text"
                                   name="last-name"
                                   required
                                   id='last-name'
                                   placeholder='Doe'
                                   className='text-field-green text-input'/>
                          </div>

                          <div className="col s12" style={{marginBottom: 8}}>
                            <label htmlFor="email" style={{fontSize: 16}}>Email Address</label>
                            <input ref={this.email}
                                   type="text"
                                   required
                                   id='email'
                                   name='email'
                                   placeholder='johndoe@example.com'
                                   className='text-field-green text-input'/>
                          </div>

                          <div className="col s12" style={{marginBottom: 8}}>
                            <label htmlFor="email" style={{fontSize: 16}}>Phone</label>
                            <input ref={this.phone}
                                   type="text"
                                   required
                                   id='phone'
                                   name='phone'
                                   placeholder='02000000000'
                                   className='text-field-green text-input'/>
                          </div>


                        </div>

                        <div className="col s12" style={{marginBottom: 8}}>
                          <a href="#!" style={{float: 'right'}}>
                            <button type='submit'
                                    className="z-depth-0 waves-effect waves-light main-green white-text btn login-btn"
                                    style={{...btnStyle}}> Update&nbsp;
                            </button>
                          </a>
                        </div>

                      </form>
                    </div>
                  </div>
                </div>

              </div>

            </div>

          </div>
        </div>
      </div>
    );

  };

}


function mapDispatchToProps(dispatch) {
  return bindActionCreators({
    profile: actions.profile,
    changePassword: actions.changePassword,
    updateProfile: actions.updateProfile
  }, dispatch);
}

function mapStateToProps({user, general, auth}) {
  return {
    error: auth.error,
    success: auth.success,
    showPreloader: general.showPreloader,
    user: user.data,
    pcSuccessMsg: user.pcSuccessMsg,
    pcErrorMsg: user.pcErrorMsg
  }
}

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Dashboard));