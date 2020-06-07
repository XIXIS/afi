import React, {Component} from "react";
import {bindActionCreators} from 'redux';
import {Link, withRouter} from "react-router-dom";
import {connect} from "react-redux";
import SideNav from "../../components/sidenavs/Sidenav";
import * as actions from '../../store/actions';
import PreLoader from "../util/preloader";
import ReactPaginate from "react-paginate";


class Invoices extends Component {

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
    this.props.listInvoices(this.state.pageNum);
  }

  handlePageClick(data) {
    let selected = data.selected;
    let offset = Math.ceil(selected * this.perPage);
    console.log(offset);
    this.setState({pageNum: selected + 1}, () => this.props.listInvoices(this.state.pageNum));
  };

  render() {

    const {invoices, showPreloader, user} = this.props;
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
                    <h5 className='white-text' style={{marginTop: 5}}>Invoices</h5>
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
                              invoices.data === 0 ?
                                <div className='grey lighten-3' style={{height: 1}}/> : ''
                            }
                          </div>
                          <div className="col s12">
                            <Link to="/app/invoices/create">
                              <button data-target='create'
                                      className="btn modal-trigger waves-effect waves-light main-green white-text center"
                                      style={btnStyle}>
                                Create Invoice
                              </button>
                            </Link>
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
                              invoices.data === 0 ?
                                <p className='body-text' style={{fontSize: 16}}>No invoices to display</p>
                                :

                                <table>
                                  <thead>
                                  <tr>
                                    <th>Client</th>
                                    <th>Items</th>
                                    <th>Total Cost</th>
                                    <th className="center">Actions</th>
                                  </tr>
                                  </thead>
                                  <tbody>
                                  {
                                    invoices.data.map(invoice => (
                                      <tr key={invoice.id}>
                                        <td>
                                          {invoice.client.name}<br/>
                                          <small className="grey-text">{invoice.client.phone}</small>
                                        </td>
                                        <td>{invoice.timesheets.length}</td>
                                        <td> GHS {invoice.totalCost.toFixed(2)}</td>
                                        <td className="center">
                                          <Link to={`/app/invoices/detail/${invoice.id}`}>
                                            <i className="material-icons main-green-text">visibility</i>
                                          </Link>
                                        </td>

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
                                  pageCount={invoices.pages}
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
    listInvoices: actions.listInvoices
  }, dispatch);
}

function mapStateToProps({invoice, general, auth}) {

  let user = auth.user;
  if (typeof user !== "object") user = user ? JSON.parse(user) : null
  return {
    invoices: invoice.invoices,
    showPreloader: general.showPreloader,
    user
  }
}

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Invoices));