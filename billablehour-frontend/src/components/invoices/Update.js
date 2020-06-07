import React from "react";
import {bindActionCreators} from 'redux';
import {withRouter} from "react-router-dom";
import {connect} from "react-redux";
import SideNav from "../../components/sidenavs/Sidenav";
import * as actions from '../../store/actions';



const Dashboard = (props) => {

  // props.clearOverlays();

  return (
    <div>
      <div className="sidenav-grey" style={{zIndex: -999, height: 100, position: 'absolute',top: 60, left: 0, right: 0}}/>
      <div style={{position: 'relative'}}>
        <div className="container" style={{width: '80%'}}>
        <div className='row' style={{marginTop: 20}}>
          <div className="col s12 m4 l3 hide-on-med-and-down">
            <SideNav/>
          </div>
          <div className="col s12 l9">
            <div className="row">
              <div className="col s12">
                <h5 className='white-text' style={{marginTop: 5}}>Your Dashboard</h5>
              </div>
            </div>
            {/*<div className="row">*/}
            {/*  <div className="col s12 m6 l4">*/}
            {/*    <ActiveContributionsCard/>*/}
            {/*  </div>*/}
            {/*  <div className="col s12 m6 l4">*/}
            {/*    <MaturedContributionsCard/>*/}
            {/*  </div>*/}
            {/*  <div className="col s12 m6 l4">*/}
            {/*    <WalletCard/>*/}
            {/*  </div>*/}
            {/*</div>*/}
            {/*<div className="row">*/}
            {/*  <div className="col s12">*/}
            {/*    <TransactionHistory/>*/}
            {/*  </div>*/}
            {/*</div>*/}
          </div>

        </div>

      </div>
      </div>
    </div>
  );

};



function mapDispatchToProps(dispatch) {
  return bindActionCreators({
    clearOverlays: actions.clearOverlays
  }, dispatch);
}

function mapStateToProps({auth}) {
  return {
    isLoggedIn  : auth.isLoggedIn,
  }
}
export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Dashboard));