import axios from 'axios';
import {ERROR} from "./auth.actions";

export const SET_CLIENTS_LIST = 'SET_USERS_LIST';
export const SET_CLIENT = 'SET_CLIENT';


export function listClients(page) {


  return (dispatch) =>
    axios({
      method: 'get',
      url: `${process.env.REACT_APP_BASE_URL}/clients?page=${page}`,
      headers: {
        Authorization: `Bearer ${localStorage.getItem("BHAccessToken")}`,
        'Content-Type': 'application/json'
      }

    }).then(function (res) {
      // console.log(res);
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


export function clientDetail(clientId) {


  return (dispatch) =>
    axios({
      method: 'get',
      url: `${process.env.REACT_APP_BASE_URL}/clients/${clientId}`,
      headers: {
        Authorization: `Bearer ${localStorage.getItem("BHAccessToken")}`,
        'Content-Type': 'application/json'
      }

    }).then(function (res) {
      // console.log(res.data.data);
      return dispatch({
        type: SET_CLIENT,
        payload: {client: {...res.data.data}}
      });

    }).catch((err) => {
      console.log(err.response != null ? err.response.data : err.message);
    });
}


export function createClient(data, history) {


  return (dispatch) =>
    axios({
      method: 'post',
      url: `${process.env.REACT_APP_BASE_URL}/clients`,
      headers: {
        Authorization: `Bearer ${localStorage.getItem("BHAccessToken")}`,
        'Content-Type': 'application/json'
      },
      data: { ...data }

    }).then(function (res) {
      // console.log(res);
      history.push({pathname: "/app/clients"})
      return dispatch(listClients(0));

    }).catch((err) => {
      console.log(err.response ? err.response.data : err.message);
      return dispatch({
        type: ERROR,
        payload: err.response ? err.response.data.message : err.message
      });
    });
}



export function updateClient(clientId, data, history) {


  return (dispatch) =>
    axios({
      method: 'put',
      url: `${process.env.REACT_APP_BASE_URL}/clients/${clientId}`,
      headers: {
        Authorization: `Bearer ${localStorage.getItem("BHAccessToken")}`,
        'Content-Type': 'application/json'
      },
      data: { ...data }

    }).then(function (res) {
      // console.log(res);
      history.push({pathname: "/app/clients"})
      return dispatch(listClients(0));

    }).catch((err) => {
      console.log(err.response ? err.response.data : err.message);
      return dispatch({
        type: ERROR,
        payload: err.response ? err.response.data.message : err.message
      });
    });
}