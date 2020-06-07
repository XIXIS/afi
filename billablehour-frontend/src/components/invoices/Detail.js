import React, {Component} from "react";
import {bindActionCreators} from 'redux';
import {Link, withRouter} from "react-router-dom";
import {connect} from "react-redux";
import SideNav from "../../components/sidenavs/Sidenav";
import * as actions from '../../store/actions';
import PreLoader from "../util/preloader";
import ReactPaginate from "react-paginate";


class InvoiceDetail extends Component {

  constructor(props) {
    super(props);
    this.state = {
      pageCount: this.props.pages,
      pageNum: 0,
    };
  }

  componentDidMount() {
    this.props.invoiceDetail(this.props.match.params.invoiceId);
  }


  render() {

    const {invoice, showPreloader} = this.props;

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
                    <h5 className='white-text' style={{marginTop: 5}}>Invoice Detail</h5>
                  </div>

                  <div className="col s12">
                    <div className="card"
                         style={{
                           borderRadius: 10,
                           boxShadow: '0 2px 5px 0 rgba(0,0,0,0.16), 0 2px 10px 0 rgba(0,0,0,0.12)',
                         }}>
                      <div className="card-content" style={{paddingTop: 30}}>
                        <div className="row" style={{marginBottom: 0}}>
                          <div className="col s3 m2">
                            <h4>Client</h4>
                          </div>
                          <div className="col s9 m10">
                            {
                             invoice ?
                               <p>
                                 <strong style={{fontSize: 18}}>{ invoice.client.name}</strong>
                                 <span><br/>{invoice.client.email}</span>
                                 <span><br/>{invoice.client.phone}</span>
                                 <span><br/>{invoice.client.address}</span>

                               </p>:
                               <p>No Invoice</p>
                            }

                          </div>
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
                              invoice &&
                                <table>
                                  <thead className="blue white-text">
                                  <tr>
                                    <th>Employer</th>
                                    <th className='hide-on-small-only'>Hours</th>
                                    <th className='hide-on-small-only'>Unit Price</th>
                                    <th className='hide-on-small-only'>Cost</th>
                                  </tr>
                                  </thead>
                                  <tbody>
                                  {
                                    invoice.timesheets.map(timesheet => (
                                      <tr key={timesheet.id}>
                                        <td>
                                          {timesheet.user.firstName} {timesheet.user.lastName}<br/>
                                          <small className="grey-text">{timesheet.client.phone}</small>
                                        </td>
                                        <td>
                                          {`${parseInt(timesheet.hours.split(":")[0]) !== 0 ? parseInt(timesheet.hours.split(":")[0])+' hours' : ''}`}&nbsp;
                                          {`${parseInt(timesheet.hours.split(":")[1]) !== 0 ? parseInt(timesheet.hours.split(":")[1])+' minutes' : ''}`}
                                        </td>
                                        <td>GHS {timesheet.rate}</td>
                                        <td>GHS {timesheet.cost}</td>
                                      </tr>
                                    ))
                                  }
                                  <tr>
                                    <td colSpan={3}>
                                      <p style={{float: 'right', fontSize: 16, fontWeight: 'bold'}}>Total</p>
                                    </td>
                                    <td colSpan={1}>
                                      <p style={{fontSize: 16}}>GHS {invoice.totalCost}</p>
                                    </td>
                                  </tr>
                                  </tbody>
                                </table>
                            }
                            <p>&nbsp;</p>
                            <p>&nbsp;</p>
                            <p>&nbsp;</p>

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
    invoiceDetail: actions.invoiceDetail
  }, dispatch);
}

function mapStateToProps({invoice, general, auth}) {

  let user = auth.user;
  if (typeof user !== "object") user = user ? JSON.parse(user) : null
  return {
    invoice: invoice.invoice,
    showPreloader: general.showPreloader,
    user
  }
}

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(InvoiceDetail));