import {combineReducers} from 'redux';
import auth from './auth.reducer';
import general from './general.reducer';
import user from './user.reducer';
import client from './client.reducer';

const createReducer = (asyncReducers) =>
  combineReducers({
    auth,
    general,
    user,
    client,
    ...asyncReducers
  });

export default createReducer;