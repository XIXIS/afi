import React, {Component} from "react";
import {Link, withRouter} from 'react-router-dom';
import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import * as actions from "../../store/actions";


class Error404 extends Component {

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
  }

  componentDidMount() {
    //Hide sidenav overlay due to react router not loading whole page
    this.props.clearOverlays();
  }

  componentDidUpdate(prevProps, prevState, snapshot) {
    //Hide sidenav overlay due to react router not loading whole page
    this.props.clearOverlays();
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

    let {validSession} = this.props;
    return (


        validSession ?
          (
            <div className='row valign-wrapper' style={{height: '90vh'}}>
              <div style={{margin: 'auto'}}>
                <div className="progress" style={{width: 300}}>
                  <div className="indeterminate"/>
                </div>

              </div>
              <img src="/images/leaves-02.svg" alt="" className='leaf'/>
            </div>
          ) :
          (
            <div className='row'>
              <div className="container valign-wrapper" style={containerStyle}>
                <div className='row'>
                  <div className="col s12">
                    <div className="card z-depth-0 auth-card">
                      <div className="card-content">
                        <div className="row">

                          <div className="col s12 center" style={{marginBottom: 8}}>
                            <h1 className='main-green-text center' style={{fontSize: '13rem'}}>404</h1>
                            <h5 className='center'>Page Not Found</h5>
                          </div>


                          <div className="col s12 center">
                            <Link to='/dashboard' className="btn-large waves-effect waves-light main-green white-text center z-depth-0 login-btn"
                                  style={btnStyle}> Go to Dashboard&nbsp;
                            </Link>

                            </div>

                          <div className="col s12 hide-on-large-only" style={{marginTop: 30}}>
                            <span className='black-text' style={{marginRight: 10}}>Not a member yet?</span>
                            <Link to={'/signup'}
                                  className='btn-large z-depth-0 white main-green-text'
                                  style={{
                                    border: '2px solid #00bc5c',
                                    borderRadius: 5,
                                    textTransform: 'unset',
                                    fontWeight: 'bold',
                                    fontSize: 16
                                  }}>Sign Up</Link>
                          </div>
                        </div>
                      </div>

                    </div>
                  </div>
                </div>
              </div>
              <img src="/images/leaves-02.svg" alt="" className='leaf'/>
            </div>
          )


    );

  }

}

function mapDispatchToProps(dispatch) {
  return bindActionCreators({
    clearOverlays: actions.clearOverlays
  }, dispatch);
}

function mapStateToProps({auth}) {
  return {
    validSession: auth.validSession
  }
}


export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Error404));
