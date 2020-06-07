import React, {Component} from "react";
import {bindActionCreators} from 'redux';
import {withRouter} from "react-router-dom";
import {connect} from "react-redux";
import SideNav from "../../components/sidenavs/Sidenav";
import * as actions from '../../store/actions';


class UpdateInvoice extends Component {

  // props.clearOverlays();

  constructor(props) {
    super(props);
    this.state = {
      pageCount: this.props.pages,
      pageNum: 0,
      validationError: '',
      populated: false
    };
    this.client = React.createRef();
    this.date = React.createRef();
    this.startTime = React.createRef();
    this.endTime = React.createRef();
    this.errorDiv = React.createRef();
    this.updateTimesheet = this.updateTimesheet.bind(this);
    this.hideError = this.hideError.bind(this);
  }



  componentDidMount() {
    // console.log(this.props.match.params.userId);
    this.props.listClients();
    this.props.timesheetDetail(this.props.match.params.timesheetId);
  }

  updateTimesheet(e) {
    e.preventDefault();
    e.stopPropagation();
    let {history} = this.props;

    if(this.client.current.value===""){
      this.setState({
        validationError: 'Client is Required'
      })
      return;
    }

    if(parseInt(this.endTime.current.value.split(":")[0]) < parseInt(this.startTime.current.value.split(":")[0])){
      this.setState({
        validationError: 'End time cannot be less than start time'
      })
      return;
    }

    let req = {
      clientId: this.client.current.value,
      date: this.date.current.value,
      startTime: this.startTime.current.value.length < 8  ? `${this.startTime.current.value}:00` : this.startTime.current.value,
      endTime: this.endTime.current.value.length < 8  ? `${this.endTime.current.value}:00` : this.endTime.current.value,
    };
    // console.log(req);
    this.props.updateTimesheet(this.props.match.params.timesheetId, {...req}, history)

  }

  componentDidUpdate(prevProps, prevState, snapshot) {

    const {timesheet} = this.props;
    const {populated} = this.state;
    if (!populated && timesheet) {
      // console.log(timesheet);
      this.client.current.value = timesheet.client.id;
      this.date.current.value = timesheet.date;
      this.startTime.current.value = timesheet.startTime;
      this.endTime.current.value = timesheet.endTime;
    }


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

    // console.log(this.props.timesheet);
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
              <div className="col s12 m8 l6">
                <div className="row">
                  <div className="col s12">
                    <h5 className='white-text' style={{marginTop: 5, marginLeft: 15}}>Update Timesheet</h5>
                  </div>
                </div>

                <div className="col s12">
                  <div className="card"
                       style={{
                         borderRadius: 10,
                         boxShadow: '0 2px 5px 0 rgba(0,0,0,0.16), 0 2px 10px 0 rgba(0,0,0,0.12)',
                       }}>
                    <div className="card-content" style={{paddingTop: 10, paddingRight: 30, paddingLeft: 30}}>

                      <form className="row" onSubmit={this.updateTimesheet}>
                        <div>
                          <h4 style={{marginLeft: 10}}>Update Timesheet Entry</h4>
                          <p style={{marginLeft: 10, marginBottom: 10}}>
                            Use the form below to update timesheet entries.
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
                              <div className="col s12" style={{marginBottom: 8}}>
                                <label htmlFor="date" style={{fontSize: 16}}>Date</label>
                                <input ref={this.date}
                                       type="date"
                                       required
                                       id='date'
                                       name='date'
                                       className='text-field-green text-input'/>
                              </div>

                              <div className="col s12" style={{marginBottom: 8}}>
                                <label htmlFor="start-time" style={{fontSize: 16}}>Start Time</label>
                                <input ref={this.startTime}
                                       type="time"
                                       required
                                       id='start-time'
                                       name='start-time'
                                       className='text-field-green text-input'/>
                              </div>

                              <div className="col s12" style={{marginBottom: 8}}>
                                <label htmlFor="end-time" style={{fontSize: 16}}>End Time</label>
                                <input ref={this.endTime}
                                       type="time"
                                       name="end-time"
                                       required
                                       id='end-time'
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
    updateTimesheet: actions.updateTimesheet,
    listClients: actions.listClientsByList,
    timesheetDetail: actions.timesheetDetail
  }, dispatch);
}

function mapStateToProps({general, auth, client, timesheet}) {
  return {
    error: auth.error,
    showPreloader: general.showPreloader,
    clients: client.clientsList,
    timesheet: timesheet.timesheet

  }
}

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(UpdateInvoice));