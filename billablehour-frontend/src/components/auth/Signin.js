import React, {Component} from "react";
import {Redirect, withRouter} from 'react-router-dom';
import {bindActionCreators} from 'redux';
import {connect} from "react-redux";
import PreLoader from '../../components/util/preloader';
import * as actions from "../../store/actions";


class Signin extends Component {

  constructor(props) {
    super(props);
    this.state = {
      preLoaderDisplay: 'none',
      redirect: false,
      passwordShow: false,
      goTo: ''
    };
    this.email = React.createRef();
    this.password = React.createRef();
    this.successDiv = React.createRef();
    this.errorDiv = React.createRef();
    this.hideError = this.hideError.bind(this);
    this.hideSuccess = this.hideSuccess.bind(this);
    this.loginUser = this.loginUser.bind(this);
    this.passwordToggle = this.passwordToggle.bind(this);
  }

  componentDidMount() {
    if (this.email.current !== null) {
      if (!this.props.isLoggedIn) this.email.current.focus();
    }
    // console.log(this.props.history.location.from);
    // if (this.props.history.location.from === undefined && this.props.history.location.from !== "/signin") {
    //   this.props.history.push({pathname: this.props.history.location.from})
    // }
    //

  }


  loginUser(e) {

    e.preventDefault();
    this.props.login({
      email: this.email.current.value,
      password: this.password.current.value
    });

  }

  /**
   * use this method to hide the error message
   */
  hideError() {
    this.errorDiv.current.style.display = 'none'
  }

  /**
   * use this method to hide the error message
   */
  hideSuccess() {
    this.successDiv.current.style.display = 'none'
  }

  passwordToggle() {
    this.setState({
      passwordShow: !this.state.passwordShow
    })
  }

  componentDidUpdate(prevProps, prevState, snapshot) {
    console.log(this.props.isLoggedIn);
    if(this.props.isLoggedIn){
      this.props.history.push({pathname: "/app/dashboard"})
    }
  }


  render() {

    const containerStyle = {height: '80vh'};
    const btnStyle = {
      backgroundColor: '#00d8ff',
      width: '100%',
      borderRadius: 5,
      textTransform: 'none',
      fontWeight: 'bold'
    };

    // console.log(this.props.authToken);
    if(this.props.authToken){
      return (<Redirect to={'/app/dashboard'}/>);
    }

    let {error} = this.props;
    // console.log(goTo)
    return (

          <div className='row'>
            <div className="container valign-wrapper" style={containerStyle}>
              <div className='row'>
                <div className="col s12">
                  <div className="card z-depth-0 auth-card">
                    <div className="card-content">
                      <div className="row">
                        <div className="col s12" style={{marginBottom: 20}}>
                          <img src="/images/afi.png" style={{width: '90%'}} alt="AFI"/>
                        </div>
                        <div className="col s12">
                              <span className="card-title body-text"
                                    style={{fontWeight: 'bold'}}>Sign in to your portal</span>
                        </div>
                        <p>&nbsp;</p>
                        {
                          this.props.showPreloader ?
                          <div className="col s12">
                            <div className='center'>
                              <PreLoader size='small'/>
                            </div>
                          </div> : ''
                        }
                        {
                          error !== '' ?
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
                                <span className='red-text'>{error}</span>
                              </p>
                            </div> : ''
                        }


                        <form onSubmit={this.loginUser}>
                          <div className="col s12" style={{marginBottom: 8}}>
                            <label htmlFor="email" style={{fontSize: 16}}>Email Address</label>
                            <input ref={this.email}
                                   type="text"
                                   required
                                   id='email'
                                   placeholder='johndoe@example.com'
                                   className='text-field-green text-input'/>
                          </div>

                          <div className="col s12" style={{marginBottom: 27}}>
                            <label htmlFor="password" style={{fontSize: 16}}>Password</label>
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


                          <div className="col s12">
                            <button type='submit'
                                    className="btn-large waves-effect waves-light main-green white-text center z-depth-0 login-btn"
                                    style={btnStyle}> Sign in&nbsp;
                            </button>

                          </div>
                        </form>

                      </div>
                    </div>

                  </div>
                </div>
              </div>
            </div>
            <img src="/images/leaves-02.svg" alt="" className='leaf'/>
          </div>


    );

  }

}


function mapDispatchToProps(dispatch) {
  return bindActionCreators({
    logout: actions.logout,
    login: actions.login,
    clearOverlays: actions.clearOverlays,
    showOrHidePreloader: actions.showOrHidePreloader
  }, dispatch);
}

function mapStateToProps({auth, general}) {
  return {
    isLoggedIn: auth.isLoggedIn,
    error: auth.error,
    success: auth.success,
    goTo: general.goTo,
    validSession: auth.validSession,
    showPreloader: general.showPreloader,
    authToken: auth.authToken
  }
}

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Signin));
