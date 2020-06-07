import axios from 'axios';

export const SET_USER_PROFILE = 'SET_USER_PROFILE';
export const SET_USERS_LIST = 'SET_USERS_LIST';


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
