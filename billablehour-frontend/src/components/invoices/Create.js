import React, {Component} from "react";
import {bindActionCreators} from 'redux';
import {withRouter} from "react-router-dom";
import {connect} from "react-redux";
import SideNav from "../../components/sidenavs/Sidenav";
import * as actions from '../../store/actions';


class CreateInvoice extends Component {

  // props.clearOverlays();

  constructor(props) {
    super(props);
    this.state = {
      validationError: ''
    };
    this.client = React.createRef();
    this.errorDiv = React.createRef();
    this.createInvoice = this.createInvoice.bind(this);
    this.hideError = this.hideError.bind(this);
  }

  componentDidMount() {
    this.props.listClients();
  }

  createInvoice(e) {
    e.preventDefault();
    e.stopPropagation();
    const {history} = this.props;
    if(this.client.current.value===""){
      this.setState({
        validationError: 'Client is Required'
      })
      return;
    }

    let invoice = {
      clientId: this.client.current.value,
    };
    // console.log(timesheet)
    this.props.createInvoice({...invoice}, history)

  }

  /**
   * use this method to hide the error message
   */
  hideError() {
    this.errorDiv.current.style.display = 'none'
  }

  render() {

    const {error, clients} = this.props;
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
                    <h5 className='white-text' style={{marginTop: 5, marginLeft: 15}}>Create Invoice</h5>
                  </div>
                </div>

                <div className="col s12">
                  <div className="card"
                       style={{
                         borderRadius: 10,
                         boxShadow: '0 2px 5px 0 rgba(0,0,0,0.16), 0 2px 10px 0 rgba(0,0,0,0.12)',
                       }}>
                    <div className="card-content" style={{paddingTop: 10, paddingRight: 30, paddingLeft: 30}}>

                      <form className="row" onSubmit={this.createInvoice}>
                        <div>
                          <h4 style={{marginLeft: 10}}>Generate Invoice Summary</h4>
                          <p style={{marginLeft: 10, marginBottom: 10}}>
                            Use the form below to generate a new invoice.
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
                              <div className="col s12" style={{marginBottom: 20, paddingRight: 0}}>
                                <span style={{fontSize: 16}} className="grey-text">Client</span>
                                <br/>
                                <select ref={this.client}
                                        id='user-type'
                                        name='user-type'
                                        style={{borderRadius: 5, border: '1px solid #e0e0e0', width: '97%'}}
                                        className='text-field-green browser-default body-text'>
                                  <option value="">Select Client</option>
                                  {
                                    clients.map((client) => (
                                      <option key={client.id} value={client.id}>{client.name}</option>
                                    ))
                                  }

                                </select>

                              </div>
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
    createInvoice: actions.createInvoice,
    listClients: actions.listClientsByList
  }, dispatch);
}

function mapStateToProps({general, auth, client}) {
  return {
    error: auth.error,
    showPreloader: general.showPreloader,
    clients: client.clientsList
  }
}

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(CreateInvoice));