import React, {Component} from 'react';
import {Route, Switch, withRouter} from 'react-router-dom';
import Dashboard from "./dashboard/Dashboard";
import Navbar from "./shared/navbars/Navbar";
import Users from "./users/List";
import Clients from "./clients/List";
import Timesheets from "./timesheets/List";
import axios from 'axios';
import CreateUser from "./users/Create";
import UpdateUser from "./users/Update";
import Grades from "./grades/List";
import CreateGrade from "./grades/Create";
import UpdateGrade from "./grades/Update";
import CreateClient from "./clients/Create";
import UpdateClient from "./clients/Update";

class Main extends Component{

  constructor(props) {
    super(props);
    axios.interceptors.response.use(
      function (response) {
        // Do something with response data
        return Promise.resolve(response)
      },
      function (error) {
        // Do something with response error
        if (error.response && error.response.status === 401) {
          this.props.history.push('/signin');
          // window.location.pathname = '/auth/login'
        }
        return Promise.reject(error)
      }
    );
  }


  render() {

    const {match} = this.props;
    return (
      <div>
        <Navbar/>
        <Switch>
          <Route path={`${match.url}/dashboard`} component={Dashboard}/>

          <Route exact path={`${match.url}/clients/create`} component={CreateClient}/>
          <Route exact path={`${match.url}/clients/update/:clientId`} component={UpdateClient}/>
          <Route path={`${match.url}/clients`} component={Clients}/>

          <Route exact path={`${match.url}/users/create`} component={CreateUser}/>
          <Route exact path={`${match.url}/users/update/:userId`} component={UpdateUser}/>
          <Route path={`${match.url}/users`} component={Users}/>

          <Route exact path={`${match.url}/grades/create`} component={CreateGrade}/>
          <Route exact path={`${match.url}/grades/update/:gradeId`} component={UpdateGrade}/>
          <Route path={`${match.url}/grades`} component={Grades}/>

          <Route path={`${match.url}/timesheets`} component={Timesheets}/>
          <Route path={`${match.url}/invoices`} component={Dashboard}/>
        </Switch>
      </div>
    );
  }
}


export default withRouter(Main);
