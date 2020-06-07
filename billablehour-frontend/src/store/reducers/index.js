import {combineReducers} from 'redux';
import auth from './auth.reducer';
import general from './general.reducer';
import user from './user.reducer';
import client from './client.reducer';
import grade from './grade.reducer';

const createReducer = (asyncReducers) =>
  combineReducers({
    auth,
    general,
    user,
    client,
    grade,
    ...asyncReducers
  });

export default createReducer;