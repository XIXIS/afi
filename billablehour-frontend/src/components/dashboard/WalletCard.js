import React, {Component} from "react";
import {Link, withRouter} from "react-router-dom";
import {connect} from "react-redux";
// import {bindActionCreators} from "redux";
// import * as actions from "../../store/actions";


class WalletCard extends Component {

  // componentDidMount() {
  //   if (this.props.user._id !== '')
  //     this.props.loadWalletDetail(this.props.user._id);
  // }

  render() {

    let {wallet} = this.props;

    return (
      <Link to={'/wallet'}>
        <div className="card"
             style={{
               borderRadius: 10,
               height: 185,
               boxShadow: '0 2px 5px 0 rgba(0,0,0,0.16), 0 2px 10px 0 rgba(0,0,0,0.12)',
             }}>
          <div className="card-content" style={{paddingTop: 30}}>
            <div className="row" style={{marginBottom: 0}}>
              <div className="col s3">
                <img src="/images/Wallet.svg" alt=""/>
              </div>
              <div className="col s9" style={{paddingTop: 7, color: '#000000'}}>
                <span style={{fontWeight: 600, fontSize: 16}}>Wallet<br/> <span
                  style={{fontSize: 12}}>#{wallet.number}</span></span>
              </div>
            </div>
            <div className="row" style={{marginBottom: 0}}>
              <div className="col s12 right">
                <span style={{fontSize: 31, color: '#2a2a2a', marginTop: 10}}
                      className='right'>GHS {Number(wallet.balance).toFixed(2)}</span>
              </div>
            </div>
          </div>
        </div>
      </Link>

    );
  }

}

// function mapDispatchToProps(dispatch) {
//   return bindActionCreators({
//     loadWalletDetail: actions.loadWalletDetail
//   }, dispatch);
// }

function mapStateToProps({auth, wallet}) {
  return {
    isLoggedIn: auth.isLoggedIn,
    user: auth.data,
    wallet: wallet.data
  }
}


export default withRouter(connect(mapStateToProps, {})(WalletCard));