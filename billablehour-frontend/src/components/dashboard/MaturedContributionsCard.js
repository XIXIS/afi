import React, {Component} from "react";
// import * as actions from "../../store/actions";
import {connect} from "react-redux";
// import {bindActionCreators} from "redux";
import {Link, withRouter} from "react-router-dom";


class MaturedContributionsCard extends Component {

  // componentDidMount() {
  //   if (this.props.user._id !== '')
  //     this.props.getMaturedContributions(this.props.user._id, 1);
  // }


  render() {

    let {totalMatured} = this.props;

    return (

      <Link to={'/contributions/matured'}>
        <div className="card"
             style={{
               borderRadius: 10,
               height: 185,
               boxShadow: '0 2px 5px 0 rgba(0,0,0,0.16), 0 2px 10px 0 rgba(0,0,0,0.12)',
             }}>
          <div className="card-content" style={{paddingTop: 30}}>
            <div className="row" style={{marginBottom: 0}}>
              <div className="col s3">
                <img src="/images/Mature_contribution.svg" alt=""/>
              </div>

              <div className="col s9" style={{paddingTop: 7}}>
                <span className='black-text'  style={{fontWeight: 600, fontSize: 16}}>Matured <br/>Contributions</span>
              </div>
            </div>
            <div className="row" style={{marginBottom: 0}}>
              <div className="col s12 m4 right">
              <span style={{fontWeight: 900, fontSize: 48, color: '#00BC5C'}} className='right'>
                {totalMatured}
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
//     getMaturedContributions: actions.getMaturedContributions
//   }, dispatch);
// }

function mapStateToProps({auth, farm}) {
  return {
    isLoggedIn: auth.isLoggedIn,
    user: auth.data,
    totalMatured: farm.totalMatured
  }
}

export default withRouter(connect(mapStateToProps, {})(MaturedContributionsCard));