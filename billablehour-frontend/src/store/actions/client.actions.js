import axios from 'axios';

export const SET_CLIENTS_LIST = 'SET_USERS_LIST';


export function listClients(page) {

  // console.log(localStorage.getItem("BHUser"));
  // console.log(localStorage.getItem("BHAccessToken"));

  return (dispatch) =>
    axios({
      method: 'get',
      url: `${process.env.REACT_APP_BASE_URL}/clients?page=${page}`,
      headers: {
        Authorization: `Bearer ${localStorage.getItem("BHAccessToken")}`,
        'Content-Type': 'application/json'
      }

    }).then(function (res) {
      console.log(res);
      return dispatch({
        type: SET_CLIENTS_LIST,
        payload: {
          clients: {
            pages: res.data.page.totalPages,
            data: res.data._embedded ? res.data._embedded.clients : [],
            pageNumber: res.data.page.number
          }
        }
      });

    }).catch((err) => {
      console.log(err.response != null ? err.response.data : err.message);
    });
}
