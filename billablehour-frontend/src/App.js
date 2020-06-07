import React, {Component} from 'react';
import {Redirect, Route, Switch} from 'react-router-dom';
import './App.css';
import Error404 from "./components/error/404";
import Signin from "./components/auth/Signin";
import Main from "./components/Main";
import {connect} from 'react-redux';
import axios from 'axios';

const RestrictedRoute = ({component: Component, authToken, ...rest}) => {
  return (
    <Route
      {...rest}
      render={props =>
        authToken
          ? <Component {...props} />
          : <Redirect
            to={{pathname: '/signin'}}
          />}

    />)
};

class App extends Component {

  componentDidMount() {
    if (this.props.authToken) {
      axios.defaults.headers.common['Authorization'] = this.props.authToken;
    }
  }

  render() {

    const {match, location, authToken} = this.props;
    if (location.pathname === '/') {
      if (!authToken) {
        return (<Redirect to={'/signin'}/>);
      } else {
        return (<Redirect to={'/app/dashboard'}/>);
      }
    }


    return (

      <div>
        <Switch>
          <RestrictedRoute path={`${match.url}app`} authToken={authToken} component={Main}/>
          <Route path="/signin" component={Signin}/>
          <Route component={Error404}/>
        </Switch>
      </div>

    );
  }
}

const mapStateToProps = ({auth}) => {
  const {authToken} = auth;
  return {authToken}
};

export default connect(mapStateToProps, {})(App);
