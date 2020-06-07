import authService from '../../services/AuthService';
// import axios from 'axios';
import * as generalActions from './general.actions';

export const SET_USER_DATA = '[USER] SET DATA';
export const REMOVE_USER_DATA = '[USER] REMOVE DATA';
export const USER_LOGGED_OUT = '[USER] LOGGED OUT';
export const ERROR = 'LOGIN_ERROR';
export const SUCCESS = 'SUCESS';
export const LOGIN_SUCCESS = 'LOGIN_SUCCESS';


export function login({email, password}) {

  generalActions.showOrHidePreloader(true);

  return (dispatch) =>
    authService.signInWithEmailAndPassword(email, password)
      .then((user) => {


        // return dispatch({
        //   type: LOGIN_SUCCESS
        // });

        generalActions.showOrHidePreloader(false);

        return dispatch(setUserData(user));


      })
      .catch(error => {
        return dispatch({
          type: ERROR,
          payload: error
        });
      });
}

export function logout() {

  return (dispatch) => {

    authService.logout();

    return dispatch({
      type: USER_LOGGED_OUT
    })

  };
}


export function setUserData(user) {
  return (dispatch) => {

    return dispatch({
      type: SET_USER_DATA,
      payload: {user: {...user}, isLoggedIn: true}
    })
  }
}