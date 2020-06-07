import axios from 'axios';
import {ERROR} from "./auth.actions";

export const SET_TIMESHEETS_LIST = 'SET_TIMESHEETS_LIST';
export const SET_TIMESHEET = 'SET_TIMESHEET';


export function listTimesheets(page) {


  return (dispatch) =>
    axios({
      method: 'get',
      url: `${process.env.REACT_APP_BASE_URL}/timesheets?page=${page}`,
      headers: {
        Authorization: `Bearer ${localStorage.getItem("BHAccessToken")}`,
        'Content-Type': 'application/json'
      }

    }).then(function (res) {
      return dispatch({
        type: SET_TIMESHEETS_LIST,
        payload: {
          timesheets: {
            pages: res.data.page.totalPages,
            data: res.data._embedded ? res.data._embedded.timeSheets : [],
            pageNumber: res.data.page.number
          }
        }
      });

    }).catch((err) => {
      console.log(err.response != null ? err.response.data : err.message);
    });
}


export function timesheetDetail(timesheetId) {


  return (dispatch) =>
    axios({
      method: 'get',
      url: `${process.env.REACT_APP_BASE_URL}/timesheets/${timesheetId}`,
      headers: {
        Authorization: `Bearer ${localStorage.getItem("BHAccessToken")}`,
        'Content-Type': 'application/json'
      }

    }).then(function (res) {
      // console.log(res.data.data);
      return dispatch({
        type: SET_TIMESHEET,
        payload: {timesheet: {...res.data.data}}
      });

    }).catch((err) => {
      console.log(err.response != null ? err.response.data : err.message);
    });
}


export function createTimesheet(data, history) {


  return (dispatch) =>
    axios({
      method: 'post',
      url: `${process.env.REACT_APP_BASE_URL}/timesheets`,
      headers: {
        Authorization: `Bearer ${localStorage.getItem("BHAccessToken")}`,
        'Content-Type': 'application/json'
      },
      data: { ...data }

    }).then(function (res) {
      // console.log(res);
      history.push({pathname: "/app/timesheets"})
      return dispatch(listTimesheets(0));

    }).catch((err) => {
      console.log(err.response ? err.response.data : err.message);
      return dispatch({
        type: ERROR,
        payload: err.response ? err.response.data.message : err.message
      });
    });
}



export function updateTimesheet(timesheetId, data, history) {


  console.log(data);
  return (dispatch) =>
    axios({
      method: 'put',
      url: `${process.env.REACT_APP_BASE_URL}/timesheets/${timesheetId}`,
      headers: {
        Authorization: `Bearer ${localStorage.getItem("BHAccessToken")}`,
        'Content-Type': 'application/json'
      },
      data: { ...data }

    }).then(function (res) {
      // console.log(res);
      history.push({pathname: "/app/timesheets"})
      return dispatch(listTimesheets(0));

    }).catch((err) => {
      console.log(err.response ? err.response.data : err.message);
      return dispatch({
        type: ERROR,
        payload: err.response ? err.response.data.message : err.message
      });
    });
}