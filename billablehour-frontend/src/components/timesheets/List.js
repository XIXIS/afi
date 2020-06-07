import React, {Component} from "react";
import {bindActionCreators} from 'redux';
import {Link, withRouter} from "react-router-dom";
import {connect} from "react-redux";
import SideNav from "../../components/sidenavs/Sidenav";
import * as actions from '../../store/actions';
import PreLoader from "../util/preloader";
import ReactPaginate from "react-paginate";


class Timesheets extends Component {

  perPage = 10;

  constructor(props) {
    super(props);
    this.state = {
      pageCount: this.props.pages,
      pageNum: 0,
    };
    this.handlePageClick = this.handlePageClick.bind(this);
  }

  componentDidMount() {
    this.props.listTimesheets(this.state.pageNum);
  }

  handlePageClick(data) {
    let selected = data.selected;
    let offset = Math.ceil(selected * this.perPage);
    console.log(offset);
    this.setState({pageNum: selected + 1}, () => this.props.listTimesheets(this.state.pageNum));
  };

  render() {

    const {timesheets, showPreloader, user} = this.props;
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
              <div className="col s12 m4 l3 hide-on-med-and-down">
                <SideNav/>
              </div>
              <div className="col s12 l9">
                <div className="row">
                  <div className="col s12">
                    <h5 className='white-text' style={{marginTop: 5}}>Timesheets</h5>
                  </div>

                  <div className="col s12">
                    <div className="card"
                         style={{
                           borderRadius: 10,
                           boxShadow: '0 2px 5px 0 rgba(0,0,0,0.16), 0 2px 10px 0 rgba(0,0,0,0.12)',
                         }}>
                      <div className="card-content" style={{paddingTop: 30}}>
                        <div className="row" style={{marginBottom: 0}}>
                          <div className="col s12">
                            {
                              timesheets.data === 0 ?
                                <div className='grey lighten-3' style={{height: 1}}/> : ''
                            }
                          </div>
                          {
                            (user && user.userType.name === "LAWYER") &&
                            <div className="col s12">
                              <Link to="/app/timesheets/create">
                                <button data-target='create'
                                        className="btn modal-trigger waves-effect waves-light main-green white-text center"
                                        style={btnStyle}>
                                  Create Timesheet
                                </button>
                              </Link>
                            </div>
                          }
                          {
                            showPreloader ?
                              <div className="col s12">
                                <div className='center'>
                                  <PreLoader size='small'/>
                                </div>
                              </div> : ''
                          }
                          <div className="col s12">
                            <p>&nbsp;</p>
                            {
                              timesheets.data === 0 ?
                                <p className='body-text' style={{fontSize: 16}}>No timsheets to display</p>
                                :

                                <table>
                                  <thead>
                                  <tr>
                                    <th>Employer</th>
                                    <th className='hide-on-small-only'>Billable Rate</th>
                                    <th className='hide-on-small-only'>Project</th>
                                    <th className='hide-on-small-only'>Date</th>
                                    <th className='hide-on-small-only'>Start Time</th>
                                    <th className='hide-on-small-only'>End Time</th>
                                  </tr>
                                  </thead>
                                  <tbody>
                                  {
                                    timesheets.data.map(timesheet => (
                                      <tr key={timesheet.id}>
                                        <td>
                                          {timesheet.user.firstName} {timesheet.user.lastName}<br/>
                                          <small className="grey-text">{timesheet.client.phone}</small>
                                        </td>
                                        <td>
                                          GHS {timesheet.rate}<br/>
                                          <small className={timesheet.invoiced ? 'green-text': 'red-text'}>
                                            <strong>{timesheet.invoiced ? 'INVOICED': 'NOT INVOICED'}</strong>
                                          </small>
                                        </td>
                                        <td>
                                          {timesheet.client.name}<br/>
                                          <small className="grey-text">{timesheet.client.phone}</small>
                                        </td>
                                        <td>{timesheet.date}</td>
                                        <td>{timesheet.startTime}</td>
                                        <td>{timesheet.endTime}</td>
                                        {
                                          (user && user.userType.name === "LAWYER" && !timesheet.invoiced) &&
                                          <td>
                                            <Link to={`/app/timesheets/update/${timesheet.id}`}>
                                              <i className="material-icons main-green-text">edit</i>
                                            </Link>
                                          </td>
                                        }
                                      </tr>
                                    ))
                                  }
                                  </tbody>
                                </table>
                            }
                            <p>&nbsp;</p>
                            <p>&nbsp;</p>
                            <p>&nbsp;</p>
                            <div className="col s12" style={{paddingLeft: 0, paddingRight: 0}}>
                              <div className="center">
                                <ReactPaginate
                                  previousLabel={<i className='material-icons'>chevron_left</i>}
                                  nextLabel={<i className='material-icons'>chevron_right</i>}
                                  breakLabel={<span>...</span>}
                                  breakClassName={"break-me"}
                                  pageCount={timesheets.pages}
                                  marginPagesDisplayed={2}
                                  pageRangeDisplayed={3}
                                  onPageChange={this.handlePageClick.bind(this)}
                                  containerClassName={"pagination"}
                                  subContainerClassName={"pages pagination"}
                                  activeClassName={"active"}/>
                              </div>

                            </div>

                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

            </div>

          </div>
        </div>

      </div>
    );
  }

}


function mapDispatchToProps(dispatch) {
  return bindActionCreators({
    listTimesheets: actions.listTimesheets
  }, dispatch);
}

function mapStateToProps({timesheet, general, auth}) {

  let user = auth.user;
  if (typeof user !== "object") user = user ? JSON.parse(user) : null
  return {
    timesheets: timesheet.timesheets,
    showPreloader: general.showPreloader,
    user
  }
}

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Timesheets));