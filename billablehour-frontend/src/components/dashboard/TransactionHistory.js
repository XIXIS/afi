import React, {Component} from "react";
// import * as actions from "../../store/actions";
import {withRouter} from "react-router-dom";
// import {bindActionCreators} from "redux";
import {connect} from "react-redux";
import moment from 'moment';
import PreLoader from "../../components/util/preloader";
import ReactPaginate from 'react-paginate';


class TransactionHistory extends Component {

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


  // componentDidMount() {
  //   if (this.props.user._id !== '') this.props.loadTransactions(this.props.user._id, this, this.state.pageNum);
  // }

  componentDidUpdate(prevProps, prevState, snapshot) {
    // this.setState({
    //   pageCount: this.props.pages
    // })
  }

  handlePageClick(data) {
    let selected = data.selected;
    console.log(selected + 1);
    let offset = Math.ceil(selected * this.perPage);
    console.log(offset);
    this.setState({pageNum: selected + 1}, () => this.props.loadTransactions(this.props.user._id, this, this.state.pageNum));
  };

  render() {

    const {transactions, pages} = this.props;

    return (
      <div className="card"
           style={{
             borderRadius: 10,
             boxShadow: '0 2px 5px 0 rgba(0,0,0,0.16), 0 2px 10px 0 rgba(0,0,0,0.12)',
           }}>
        <div className="card-content" style={{paddingTop: 30}}>
          <div className="row" style={{marginBottom: 0}}>
            <div className="col s12">
              <p className='body-text' style={{marginBottom: 10, fontSize: 16, fontWeight: 600}}>
                Wallet Transaction History
              </p>
              {
                transactions.length === 0 ?
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
                transactions.length === 0 ?
                  <p className='body-text' style={{fontSize: 16}}>No transactions to display</p>
                  :

                  <table>
                    <thead>
                    <tr>
                      <th>Description</th>
                      <th className='hide-on-small-only'>Mode of Payment</th>
                      <th className='hide-on-small-only'>Amount</th>
                      <th className='hide-on-small-only'>Date</th>
                    </tr>
                    </thead>
                    <tbody>
                    {
                      transactions.map(transaction => (
                        <tr key={transaction._id}>
                          <td>
                            {transaction.description}
                            <span className='hide-on-med-and-up' style={{fontSize: 13, fontWeight: '600'}}>
                                &nbsp;({transaction.currency.symbol}&nbsp;
                                {Math.floor(transaction.amount).toLocaleString()}.
                                {(transaction.amount.toFixed(2) + "").split('.')[1]})
                            </span>
                          </td>
                          <td
                            className='hide-on-small-only'>{transaction.payment ? transaction.payment.modeOfPayment : ''}</td>
                          <td className='hide-on-small-only'>
                            {transaction.currency.symbol}&nbsp;
                            {Math.floor(transaction.amount).toLocaleString()}.
                            {(transaction.amount.toFixed(2) + "").split('.')[1]}
                          </td>
                          <td className='hide-on-small-only'>{moment(transaction.createdAt).fromNow()}</td>
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
                    pageCount={pages}
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

    );
  }

}

//
// function mapDispatchToProps(dispatch) {
//   return bindActionCreators({
//     getActiveContribution: actions.getActiveContributions,
//     loadTransactions: actions.loadTransactions
//   }, dispatch);
// }

function mapStateToProps({auth, transaction}) {
  return {
    isLoggedIn: auth.isLoggedIn,
    user: auth.data,
    transactions: transaction.data,
    pages: transaction.pages
  }
}

export default withRouter(connect(mapStateToProps, {})(TransactionHistory));