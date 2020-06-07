import React, {Component} from "react";
import {bindActionCreators} from 'redux';
import {withRouter} from "react-router-dom";
import {connect} from "react-redux";
import SideNav from "../../components/sidenavs/Sidenav";
import * as actions from '../../store/actions';


class UpdateGrade extends Component {

  // props.clearOverlays();

  constructor(props) {
    super(props);
    this.state = {
      pageCount: this.props.pages,
      pageNum: 0,
      showGrade: 'none',
      validationError: '',
      populated: false
    };
    this.firstName = React.createRef();
    this.lastName = React.createRef();
    this.email = React.createRef();
    this.phone = React.createRef();
    this.userTypeId = React.createRef();
    this.gradeId = React.createRef();
    this.errorDiv = React.createRef();
    this.updateUser = this.updateUser.bind(this);
    this.showGrades = this.showGrades.bind(this);
    this.hideError = this.hideError.bind(this);
  }


  showGrades() {
    let userTypeId = this.userTypeId.current.value;
    let {userTypesObj} = this.props;
    this.setState({
      showGrade: userTypesObj[userTypeId] === 'LAWYER' ? 'block' : 'none'
    })
  }


  componentDidMount() {
    // console.log(this.props.match.params.userId);
    this.props.userDetail(this.props.match.params.userId);
    this.props.listUserTypes();
    this.props.listGrades();
  }

  updateUser(e) {
    e.preventDefault();
    e.stopPropagation();
    let {userTypesObj, history, user, authUser} = this.props;

    if (user.id !== authUser.id) {
      if (this.userTypeId.current.value === "") {
        this.setState({
          validationError: 'User is Required'
        })
        return;
      }
      if (userTypesObj[this.userTypeId.current.value] === 'LAWYER' && this.gradeId.current.value === "") {
        this.setState({
          validationError: 'Grade is required for Lawyers'
        })
        return
      }
    }

    let req = {
      firstName: this.firstName.current.value,
      lastName: this.lastName.current.value,
      email: this.email.current.value,
      phone: this.phone.current.value,
      userTypeId: user.id !== authUser.id ? this.userTypeId.current.value : user.userType.id
    };
    if (userTypesObj[req.userTypeId] === 'LAWYER') req.gradeId = user.id !== authUser.id ? this.gradeId.current.value : user.grade.id
    this.props.updateUser(this.props.match.params.userId, {...req}, history)

  }

  componentDidUpdate(prevProps, prevState, snapshot) {

    const {user, authUser} = this.props;
    const {populated} = this.state;
    if (!populated && user) {
      this.firstName.current.value = user.firstName;
      this.lastName.current.value = user.lastName;
      this.email.current.value = user.email;
      this.phone.current.value = user.phone;
      if (user.id !== authUser.id && this.userTypeId.current, this.gradeId.current) {
        this.userTypeId.current.value = user.userType.id;
        this.gradeId.current.value = user.grade ? user.grade.id : "";
      }
      // M.FormSelect.init(document.getElementsByClassName(".select"), {})
    }


  }

  /**
   * use this method to hide the error message
   */
  hideError() {
    this.errorDiv.current.style.display = 'none'
  }

  render() {

    const {userTypes, grades, error, authUser, user} = this.props;
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
          <div className="container" style={{width: '80%'}}>
            <div className='row' style={{marginTop: 20}}>
              <div className="col s12 m4 l3 hide-on-med-and-down">
                <SideNav/>
              </div>
              <div className="col s12 l9">
                <div className="row">
                  <div className="col s12">
                    <h5 className='white-text' style={{marginTop: 5, marginLeft: 15}}>Update User</h5>
                  </div>
                </div>

                <div className="col s12">
                  <div className="card"
                       style={{
                         borderRadius: 10,
                         boxShadow: '0 2px 5px 0 rgba(0,0,0,0.16), 0 2px 10px 0 rgba(0,0,0,0.12)',
                       }}>
                    <div className="card-content" style={{paddingTop: 10, paddingRight: 30, paddingLeft: 30}}>

                      <form className="row" onSubmit={this.updateUser}>
                        <div>
                          <h4 style={{marginLeft: 10}}>Update User</h4>
                          <p style={{marginLeft: 10, marginBottom: 10}}>
                            Use the form below to update user details.
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

                          <div className="col s12 l6">
                            <div className="row">
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


                            </div>
                          </div>

                          <div className="col s12 l6">
                            <div className="row">

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

                              {
                                (user && authUser.id !== user.id) &&
                                <>
                                  <div className="col s12" style={{marginBottom: 20, paddingRight: 0}}>
                                    <span style={{fontSize: 16}} className="grey-text">User Type</span>
                                    <br/>
                                    <select ref={this.userTypeId}
                                            id='user-type'
                                            name='user-type'
                                            onChange={this.showGrades}
                                            style={{borderRadius: 5, border: '1px solid #e0e0e0', width: '97%'}}
                                            className='text-field-green browser-default body-text'>
                                      <option value="">Select User Type</option>
                                      {
                                        userTypes.map((userType) => (
                                          <option key={userType.id} value={userType.id}>{userType.name}</option>
                                        ))
                                      }

                                    </select>

                                  </div>

                                  <div className="col s12"
                                       style={{marginBottom: 8, paddingRight: 0, display: this.state.showGrade}}>
                                    <span style={{fontSize: 16}} className="grey-text">Grade</span>
                                    <br/>
                                    <select ref={this.gradeId}
                                            id='user-type'
                                            name='user-type'
                                            style={{borderRadius: 5, border: '1px solid #e0e0e0', width: '97%'}}
                                            className='text-field-green browser-default body-text'>
                                      <option value="">Select Grades</option>
                                      {
                                        grades.map((grade) => (
                                          <option key={grade.id} value={grade.id}>{grade.name}</option>
                                        ))
                                      }

                                    </select>

                                  </div>

                                </>
                              }
                            </div>
                          </div>

                          <div className="col s12" style={{marginBottom: 8}}>
                            <a href="#!" style={{marginTop: -20, float: 'right'}}>
                              <button type='submit'
                                      className="z-depth-0 waves-effect waves-light main-green white-text btn login-btn"
                                      style={{...btnStyle, marginRight: 5}}> Create&nbsp;
                              </button>
                            </a>
                          </div>

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
    updateUser: actions.updateUser,
    listUserTypes: actions.listUserTypes,
    listGrades: actions.listGrades,
    userDetail: actions.userDetail
  }, dispatch);
}

function mapStateToProps({user, general, auth}) {
  let authUser = auth.user;
  if (typeof authUser !== "object") authUser = authUser ? JSON.parse(authUser) : null
  return {
    error: auth.error,
    userTypes: user.userTypes,
    userTypesObj: user.userTypesObj,
    grades: user.gradesList,
    showPreloader: general.showPreloader,
    user: user.user,
    authUser

  }
}

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(UpdateGrade));