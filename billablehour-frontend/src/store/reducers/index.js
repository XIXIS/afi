import {combineReducers} from 'redux';
import auth from './auth.reducer';
import general from './general.reducer';
import user from './user.reducer';
import client from './client.reducer';
import grade from './grade.reducer';
import timesheet from './timesheet.reducer';

const createReducer = (asyncReducers) =>
  combineReducers({
    auth,
    general,
    user,
    client,
    grade,
    timesheet,
    ...asyncReducers
  });

export default createReducer;