import React from 'react';
import {Provider} from 'react-redux';
import { Router, Route} from 'react-router-dom';
import store from './store';
import App from './App'
import { createBrowserHistory } from "history";

const history = createBrowserHistory();

const MainApp = () =>
  <Provider store={store}>
      <Router history={history}>
        <Route path="/" component={App}/>
      </Router>
  </Provider>;


export default MainApp;
