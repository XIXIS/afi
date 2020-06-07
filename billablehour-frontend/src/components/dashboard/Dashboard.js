import React, {Component} from "react";
import {withRouter} from "react-router-dom";
import SideNav from "../../components/sidenavs/Sidenav";


class Dashboard extends Component {

  render() {

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
                    <h5 className='white-text' style={{marginTop: 5}}>Your Dashboard</h5>
                  </div>
                </div>
              </div>

            </div>

          </div>
        </div>
      </div>
    );

  };

}


export default withRouter(Dashboard);