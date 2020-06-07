import React, {Component} from "react";
import {bindActionCreators} from 'redux';
import {withRouter} from "react-router-dom";
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
      preloaderDisplay: 'block',
      pageCount: this.props.pages,
      pageNum: 1,
    };
    this.handlePageClick = this.handlePageClick.bind(this);
  }

  componentDidMount() {
    this.props.listUsers(1)
  }

  handlePageClick(data) {
    let selected = data.selected;
    let offset = Math.ceil(selected * this.perPage);
    console.log(offset);
    this.setState({pageNum: selected + 1}, () => this.props.loadUsers(this.state.pageNum));
  };

  render() {

    const {users} = this.props;

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
                    <h5 className='white-text' style={{marginTop: 5}}>Users</h5>
                  </div>
                </div>
              </div>

              <div className="row">
                <div className="col s12 m10">
                  <div className="card"
                       style={{
                         borderRadius: 10,
                         boxShadow: '0 2px 5px 0 rgba(0,0,0,0.16), 0 2px 10px 0 rgba(0,0,0,0.12)',
                       }}>
                    <div className="card-content" style={{paddingTop: 30}}>
                      <div className="row" style={{marginBottom: 0}}>
                        <div className="col s12">
                          <p className='body-text' style={{marginBottom: 10, fontSize: 16, fontWeight: 600}}>
                            Users List
                          </p>
                          {
                            users.data === 0 ?
                              <div className='grey lighten-3' style={{height: 1}}/> : ''
                          }
                        </div>
                        <div className="col s12">
                          <div className='center' style={{display: this.state.preloaderDisplay}}>
                            <PreLoader size='small'/>
                          </div>
                        </div>
                        <div className="col s12">
                          <p>&nbsp;</p>
                          {
                            users.data === 0 ?
                              <p className='body-text' style={{fontSize: 16}}>No transactions to display</p>
                              :

                              <table>
                                <thead>
                                <tr>
                                  <th>Description</th>
                                  <th className='hide-on-small-only'>Name</th>
                                  <th className='hide-on-small-only'>Email</th>
                                  <th className='hide-on-small-only'>Phone</th>
                                </tr>
                                </thead>
                                <tbody>
                                {
                                  users.data.map(user => (
                                    <tr key={user.id}>
                                      <td>{user.firstName} {user.lastName}</td>
                                      <td>{user.email}</td>
                                      <td >{user.phone}</td>
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
                                pageCount={users.pages}
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
    );
  }

}


function mapDispatchToProps(dispatch) {
  return bindActionCreators({
    listUsers: actions.listUsers
  }, dispatch);
}

function mapStateToProps({user}) {
  return {
    users: user.users
  }
}

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Invoices));