import axios from 'axios';
import {ERROR} from "./auth.actions";

export const SET_INVOICES_LIST = 'SET_INVOICES_LIST';
export const SET_INVOICE = 'SET_INVOICE';


export function listInvoices(page) {


  return (dispatch) =>
    axios({
      method: 'get',
      url: `${process.env.REACT_APP_BASE_URL}/invoices?page=${page}`,
      headers: {
        Authorization: `Bearer ${localStorage.getItem("BHAccessToken")}`,
        'Content-Type': 'application/json'
      }

    }).then(function (res) {
      return dispatch({
        type: SET_INVOICES_LIST,
        payload: {
          invoices: {
            pages: res.data.page.totalPages,
            data: res.data._embedded ? res.data._embedded.invoices : [],
            pageNumber: res.data.page.number
          }
        }
      });

    }).catch((err) => {
      console.log(err.response != null ? err.response.data : err.message);
    });
}


export function invoiceDetail(invoiceId) {


  return (dispatch) =>
    axios({
      method: 'get',
      url: `${process.env.REACT_APP_BASE_URL}/invoices/${invoiceId}`,
      headers: {
        Authorization: `Bearer ${localStorage.getItem("BHAccessToken")}`,
        'Content-Type': 'application/json'
      }

    }).then(function (res) {
      // console.log(res.data.data);
      return dispatch({
        type: SET_INVOICE,
        payload: {invoice: {...res.data.data}}
      });

    }).catch((err) => {
      console.log(err.response != null ? err.response.data : err.message);
    });
}


export function createInvoice(data, history) {


  return (dispatch) =>
    axios({
      method: 'post',
      url: `${process.env.REACT_APP_BASE_URL}/invoices`,
      headers: {
        Authorization: `Bearer ${localStorage.getItem("BHAccessToken")}`,
        'Content-Type': 'application/json'
      },
      data: { ...data }

    }).then(function (res) {
      // console.log(res);
      history.push({pathname: "/app/invoices"})
      return dispatch(listInvoices(0));

    }).catch((err) => {
      console.log(err.response ? err.response.data : err.message);
      return dispatch({
        type: ERROR,
        payload: err.response ? err.response.data.message : err.message
      });
    });
}



export function updateInvoice(invoiceId, data, history) {


  // console.log(data);
  return (dispatch) =>
    axios({
      method: 'put',
      url: `${process.env.REACT_APP_BASE_URL}/invoices/${invoiceId}`,
      headers: {
        Authorization: `Bearer ${localStorage.getItem("BHAccessToken")}`,
        'Content-Type': 'application/json'
      },
      data: { ...data }

    }).then(function (res) {
      // console.log(res);
      history.push({pathname: "/app/invoices"})
      return dispatch(listInvoices(0));

    }).catch((err) => {
      console.log(err.response ? err.response.data : err.message);
      return dispatch({
        type: ERROR,
        payload: err.response ? err.response.data.message : err.message
      });
    });
}