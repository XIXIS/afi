import React, {Component} from "react";
// import {bindActionCreators} from "redux";
import {withRouter, Link} from "react-router-dom";
import {connect} from "react-redux";
// import * as actions from "../../store/actions";


class ActiveContributionsCard extends Component {

  // componentDidMount() {
  //   if (this.props.user._id!=='')
  //     this.props.getActiveContributions(this.props.user._id, 1);
  // }

  render() {

    let {totalActive} = this.props;

    return (

      <Link to={'/contributions/active'}>
        <div className="card"
             style={{
               borderRadius: 10,
               height: 185,
               boxShadow: '0 2px 5px 0 rgba(0,0,0,0.16), 0 2px 10px 0 rgba(0,0,0,0.12)',
             }}>
          <div className="card-content" style={{paddingTop: 30}}>
            <div className="row" style={{marginBottom: 0}}>
              <div className="col s3">
                <img src="/images/Active_contribution.svg" alt=""/>
              </div>
              <div className="col s9" style={{paddingTop: 7}}>
                <span className='black-text' style={{fontWeight: 600, fontSize: 16}}>Active<br/> Contributions</span>
              </div>
            </div>
            <div className="row" style={{marginBottom: 0}}>
              <div className="col s12 m4 right">
                <span style={{fontWeight: 900, fontSize: 48, color: '#FF2954'}} className='right'>
                  {totalActive}
                </span>
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
//     getActiveContributions: actions.getActiveContributions
//   }, dispatch);
// }

function mapStateToProps({auth, farm}) {
  return {
    isLoggedIn: auth.isLoggedIn,
    user: auth.data,
    totalActive: farm.totalActive
  }
}

export default withRouter(connect(mapStateToProps, {})(ActiveContributionsCard));