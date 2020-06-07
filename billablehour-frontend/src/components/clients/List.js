import React, {Component} from "react";
import {bindActionCreators} from 'redux';
import {Link, withRouter} from "react-router-dom";
import {connect} from "react-redux";
import SideNav from "../../components/sidenavs/Sidenav";
import * as actions from '../../store/actions';
import PreLoader from "../util/preloader";
import ReactPaginate from "react-paginate";
import M from 'materialize-css';


class Clients extends Component {

  perPage = 10;

  constructor(props) {
    super(props);
    this.state = {
      pageCount: this.props.pages,
      pageNum: 0,
    };
    this.handlePageClick = this.handlePageClick.bind(this);
    this.createModal = React.createRef();
  }

  componentDidMount() {
    this.props.listClients(this.state.pageNum);
  }

  handlePageClick(data) {
    let selected = data.selected;
    let offset = Math.ceil(selected * this.perPage);
    console.log(offset);
    this.setState({pageNum: selected + 1}, () => this.props.listClients(this.state.pageNum));
  };

  render() {

    const {clients, showPreloader} = this.props;
    // console.log(clients);
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
                    <h5 className='white-text' style={{marginTop: 5}}>Clients</h5>
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
                              clients.data === 0 ?
                                <div className='grey lighten-3' style={{height: 1}}/> : ''
                            }
                          </div>
                          <div className="col s12">
                            <Link to="/app/clients/create">
                              <button data-target='create'
                                      className="btn modal-trigger waves-effect waves-light main-green white-text center"
                                      style={btnStyle}>
                                Create Client
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
                              clients.data === 0 ?
                                <p className='body-text' style={{fontSize: 16}}>No transactions to display</p>
                                :

                                <table>
                                  <thead>
                                  <tr>
                                    <th>Name</th>
                                    <th className='hide-on-small-only'>Email/Phone</th>
                                    <th className='hide-on-small-only'>Address</th>
                                    <th className='hide-on-small-only'>Action</th>
                                  </tr>
                                  </thead>
                                  <tbody>
                                  {
                                    clients.data.map(client => (
                                      <tr key={client.id}>
                                        <td>{client.name}</td>
                                        <td>
                                          {client.email}<br/>{client.phone}
                                        </td>
                                        <td>{client.address}</td>
                                        <td>
                                          <Link to={`/app/clients/update/${client.id}`}>
                                            <i className="material-icons main-green-text">edit</i>
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
                                  pageCount={clients.pages}
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

        <div id="create" className="modal" ref={this.createModal}>
          <div className="modal-content">
            <h4>Modal Header</h4>
            <p>A bunch of text</p>
          </div>
          <div className="modal-footer">
            <a href="#!" className="modal-close waves-effect waves-green btn-flat">Agree</a>
          </div>
        </div>
      </div>
    );
  }

}


function mapDispatchToProps(dispatch) {
  return bindActionCreators({
    listClients: actions.listClients
  }, dispatch);
}

function mapStateToProps({client, general}) {
  return {
    clients: client.clients,
    showPreloader: general.showPreloader
  }
}

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Clients));