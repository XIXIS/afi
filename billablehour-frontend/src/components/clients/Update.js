import React, {Component} from "react";
import {bindActionCreators} from 'redux';
import {withRouter} from "react-router-dom";
import {connect} from "react-redux";
import SideNav from "../../components/sidenavs/Sidenav";
import * as actions from '../../store/actions';


class UpdateClient extends Component {

  // props.clearOverlays();

  constructor(props) {
    super(props);
    this.state = {
      pageCount: this.props.pages,
      pageNum: 0,
      validationError: '',
      populated: false
    };
    this.name = React.createRef();
    this.address = React.createRef();
    this.email = React.createRef();
    this.phone = React.createRef();
    this.errorDiv = React.createRef();
    this.updateGrades = this.updateGrades.bind(this);
    this.hideError = this.hideError.bind(this);
  }



  componentDidMount() {
    // console.log(this.props.match.params.userId);
    this.props.clientDetail(this.props.match.params.clientId);
  }

  updateGrades(e) {
    e.preventDefault();
    e.stopPropagation();
    let {history} = this.props;

    let req = {
      name: this.name.current.value,
      address: this.address.current.value,
      email: this.email.current.value,
      phone: this.phone.current.value
    };
    this.props.updateClient(this.props.match.params.clientId, {...req}, history)

  }

  componentDidUpdate(prevProps, prevState, snapshot) {

    const {client} = this.props;
    const {populated} = this.state;
    if (!populated && client) {
      this.name.current.value = client.name;
      this.email.current.value = client.email;
      this.phone.current.value = client.phone;
      this.address.current.value = client.address;
    }


  }

  /**
   * use this method to hide the error message
   */
  hideError() {
    this.errorDiv.current.style.display = 'none'
  }

  render() {

    const {error} = this.props;
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
              <div className="col s12 m8 l6">
                <div className="row">
                  <div className="col s12">
                    <h5 className='white-text' style={{marginTop: 5, marginLeft: 15}}>Update Grade</h5>
                  </div>
                </div>

                <div className="col s12">
                  <div className="card"
                       style={{
                         borderRadius: 10,
                         boxShadow: '0 2px 5px 0 rgba(0,0,0,0.16), 0 2px 10px 0 rgba(0,0,0,0.12)',
                       }}>
                    <div className="card-content" style={{paddingTop: 10, paddingRight: 30, paddingLeft: 30}}>

                      <form className="row" onSubmit={this.updateGrades}>
                        <div>
                          <h4 style={{marginLeft: 10}}>Update Grade</h4>
                          <p style={{marginLeft: 10, marginBottom: 10}}>
                            Use the form below to update grade details.
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

                          <div className="col s12">
                            <div className="row">
                              <div className="col s12" style={{marginBottom: 8}}>
                                <label htmlFor="name" style={{fontSize: 16}}>Name</label>
                                <input ref={this.name}
                                       type="text"
                                       required
                                       id='name'
                                       name='name'
                                       placeholder='Grade D'
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
                                <label htmlFor="phone" style={{fontSize: 16}}>Phone</label>
                                <input ref={this.phone}
                                       type="text"
                                       required
                                       id='phone'
                                       name='phone'
                                       placeholder='02000000000'
                                       className='text-field-green text-input'/>
                              </div>

                              <div className="col s12" style={{marginBottom: 8}}>
                                <label htmlFor="address" style={{fontSize: 16}}>Address</label>
                                <input ref={this.address}
                                       type="text"
                                       step="0.01"
                                       name="address"
                                       required
                                       id='address'
                                       placeholder='Box 1234, Street'
                                       className='text-field-green text-input'/>
                              </div>


                            </div>
                          </div>

                          <div className="col s12" style={{marginBottom: 8}}>
                            <a href="#!" style={{marginTop: -20, float: 'right'}}>
                              <button type='submit'
                                      className="z-depth-0 waves-effect waves-light main-green white-text btn login-btn"
                                      style={{...btnStyle, marginRight: 5}}> Update&nbsp;
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
    updateClient: actions.updateClient,
    clientDetail: actions.clientDetail
  }, dispatch);
}

function mapStateToProps({general, auth, client}) {
  return {
    error: auth.error,
    showPreloader: general.showPreloader,
    client: client.client

  }
}

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(UpdateClient));