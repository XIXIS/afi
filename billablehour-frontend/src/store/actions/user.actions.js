import axios from 'axios';
import {ERROR} from "./auth.actions";

export const SET_USER_PROFILE = 'SET_USER_PROFILE';
export const SET_USERS_LIST = 'SET_USERS_LIST';
export const SET_USER_TYPES_LIST = 'SET_USER_TYPES_LIST';
export const SET_GRADES_LIST = 'SET_GRADES_LIST';


export function profile() {

  return (dispatch) =>
    axios({
      method: 'get',
      url: `${process.env.REACT_APP_BASE_URL}/users/my/profile`,

    }).then(function (res) {
      console.log(res.data);
      return dispatch({
        type: SET_USER_PROFILE,
        payload: {data: res.data.data, success: '', error: ''}
      });

    }).catch((err) => {
      console.log(err.response != null ? err.response.data : err.message);
    });
}

export function createUser(data, history) {

  // console.log(localStorage.getItem("BHUser"));
  // console.log(localStorage.getItem("BHAccessToken"));

  return (dispatch) =>
    axios({
      method: 'post',
      url: `${process.env.REACT_APP_BASE_URL}/users`,
      headers: {
        Authorization: `Bearer ${localStorage.getItem("BHAccessToken")}`,
        'Content-Type': 'application/json'
      },
      data: { ...data }

    }).then(function (res) {
      // console.log(res);
      history.push({pathname: "/app/users"})
      return dispatch(listUsers(0));

    }).catch((err) => {
      console.log(err.response ? err.response.data : err.message);
      return dispatch({
        type: ERROR,
        payload: err.response ? err.response.data.message : err.message
      });
    });
}



export function updateUser(userId, data, history) {

  console.log({...data});
  // console.log(localStorage.getItem("BHAccessToken"));

  return (dispatch) =>
    axios({
      method: 'put',
      url: `${process.env.REACT_APP_BASE_URL}/users/${userId}`,
      headers: {
        Authorization: `Bearer ${localStorage.getItem("BHAccessToken")}`,
        'Content-Type': 'application/json'
      },
      data: { ...data }

    }).then(function (res) {
      // console.log(res);
      history.push({pathname: "/app/users"})
      return dispatch(listUsers(0));

    }).catch((err) => {
      console.log(err.response ? err.response.data : err.message);
      return dispatch({
        type: ERROR,
        payload: err.response ? err.response.data.message : err.message
      });
    });
}


export function userDetail(userId) {

  // console.log(userId);
  // console.log(localStorage.getItem("BHAccessToken"));

  return (dispatch) =>
    axios({
      method: 'get',
      url: `${process.env.REACT_APP_BASE_URL}/users/${userId}`,
      headers: {
        Authorization: `Bearer ${localStorage.getItem("BHAccessToken")}`,
        'Content-Type': 'application/json'
      }

    }).then(function (res) {
      // console.log(res.data.data);
      return dispatch({
        type: SET_USER_PROFILE,
        payload: {user: {...res.data.data}}
      });

    }).catch((err) => {
      console.log(err.response != null ? err.response.data : err.message);
    });
}


export function listUsers(page) {

  // console.log(localStorage.getItem("BHUser"));
  // console.log(localStorage.getItem("BHAccessToken"));

  return (dispatch) =>
    axios({
      method: 'get',
      url: `${process.env.REACT_APP_BASE_URL}/users?page=${page}`,
      headers: {
        Authorization: `Bearer ${localStorage.getItem("BHAccessToken")}`,
        'Content-Type': 'application/json'
      }

    }).then(function (res) {
      // console.log(res);
      return dispatch({
        type: SET_USERS_LIST,
        payload: {
          users: {
            pages: res.data.page.totalPages,
            data: res.data._embedded ? res.data._embedded.users : [],
            pageNumber: res.data.page.number
          }
        }
      });

    }).catch((err) => {
      console.log(err.response != null ? err.response.data : err.message);
    });
}


export function listUserTypes() {

  // console.log(localStorage.getItem("BHUser"));
  // console.log(localStorage.getItem("BHAccessToken"));

  return (dispatch) =>
    axios({
      method: 'get',
      url: `${process.env.REACT_APP_BASE_URL}/user-types`,
      headers: {
        Authorization: `Bearer ${localStorage.getItem("BHAccessToken")}`,
        'Content-Type': 'application/json'
      }

    }).then(function (res) {
      // console.log(res);

      dispatch({
        type: ERROR,
        payload: ""
      });

      let userTypesObj = {};
      res.data.data.forEach(userType => {
          userTypesObj[userType.id] = userType.name
        })


      return dispatch({
        type: SET_USER_TYPES_LIST,
        payload: {userTypes: res.data.data, userTypesObj}
      });

    }).catch((err) => {
      console.log(err.response != null ? err.response.data : err.message);
    });
}

export function listGrades() {

  // console.log(localStorage.getItem("BHUser"));
  // console.log(localStorage.getItem("BHAccessToken"));

  return (dispatch) =>
    axios({
      method: 'get',
      url: `${process.env.REACT_APP_BASE_URL}/list/grades`,
      headers: {
        Authorization: `Bearer ${localStorage.getItem("BHAccessToken")}`,
        'Content-Type': 'application/json'
      }

    }).then(function (res) {
      // console.log(res);
      return dispatch({
        type: SET_GRADES_LIST,
        payload: {grades: res.data.data}
      });

    }).catch((err) => {
      console.log(err.response != null ? err.response.data : err.message);
    });
}
