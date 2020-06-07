import axios from 'axios';
import {ERROR} from "./auth.actions";

// export const SET_GRADES_LIST = 'SET_GRADES_LIST';
export const SET_GRADES = 'SET_GRADES';
export const SET_GRADE = 'SET_GRADE';



export function gradeDetail(gradeId) {

  // console.log(userId);
  // console.log(localStorage.getItem("BHAccessToken"));

  return (dispatch) =>
    axios({
      method: 'get',
      url: `${process.env.REACT_APP_BASE_URL}/grades/${gradeId}`,
      headers: {
        Authorization: `Bearer ${localStorage.getItem("BHAccessToken")}`,
        'Content-Type': 'application/json'
      }

    }).then(function (res) {
      // console.log(res.data.data);
      return dispatch({
        type: SET_GRADE,
        payload: {grade: {...res.data.data}}
      });

    }).catch((err) => {
      console.log(err.response != null ? err.response.data : err.message);
    });
}


export function createGrade(data, history) {

  // console.log(localStorage.getItem("BHUser"));
  // console.log(localStorage.getItem("BHAccessToken"));

  return (dispatch) =>
    axios({
      method: 'post',
      url: `${process.env.REACT_APP_BASE_URL}/grades`,
      headers: {
        Authorization: `Bearer ${localStorage.getItem("BHAccessToken")}`,
        'Content-Type': 'application/json'
      },
      data: { ...data }

    }).then(function (res) {
      // console.log(res);
      history.push({pathname: "/app/grades"})
      return dispatch(grades(0));

    }).catch((err) => {
      console.log(err.response ? err.response.data : err.message);
      return dispatch({
        type: ERROR,
        payload: err.response ? err.response.data.message : err.message
      });
    });
}



export function updateGrade(userId, data, history) {

  // console.log({...data});
  // console.log(localStorage.getItem("BHAccessToken"));

  return (dispatch) =>
    axios({
      method: 'put',
      url: `${process.env.REACT_APP_BASE_URL}/grades/${userId}`,
      headers: {
        Authorization: `Bearer ${localStorage.getItem("BHAccessToken")}`,
        'Content-Type': 'application/json'
      },
      data: { ...data }

    }).then(function (res) {
      // console.log(res);
      history.push({pathname: "/app/grades"})
      return dispatch(grades(0));

    }).catch((err) => {
      console.log(err.response ? err.response.data : err.message);
      return dispatch({
        type: ERROR,
        payload: err.response ? err.response.data.message : err.message
      });
    });
}


export function grades(page) {

  // console.log(localStorage.getItem("BHUser"));
  // console.log(localStorage.getItem("BHAccessToken"));

  return (dispatch) =>
    axios({
      method: 'get',
      url: `${process.env.REACT_APP_BASE_URL}/grades?page=${page}`,
      headers: {
        Authorization: `Bearer ${localStorage.getItem("BHAccessToken")}`,
        'Content-Type': 'application/json'
      }

    }).then(function (res) {
      // console.log(res);
      return dispatch({
        type: SET_GRADES,
        payload: {
          grades: {
            pages: res.data.page.totalPages,
            data: res.data._embedded ? res.data._embedded.grades : [],
            pageNumber: res.data.page.number
          }
        }
      });

    }).catch((err) => {
      console.log(err.response != null ? err.response.data : err.message);
    });
}


// export function listGrades() {
//
//   // console.log(localStorage.getItem("BHUser"));
//   // console.log(localStorage.getItem("BHAccessToken"));
//
//   return (dispatch) =>
//     axios({
//       method: 'get',
//       url: `${process.env.REACT_APP_BASE_URL}/list/grades`,
//       headers: {
//         Authorization: `Bearer ${localStorage.getItem("BHAccessToken")}`,
//         'Content-Type': 'application/json'
//       }
//
//     }).then(function (res) {
//       // console.log(res);
//       return dispatch({
//         type: SET_GRADES_LIST,
//         payload: {grades: res.data.data}
//       });
//
//     }).catch((err) => {
//       console.log(err.response != null ? err.response.data : err.message);
//     });
// }
