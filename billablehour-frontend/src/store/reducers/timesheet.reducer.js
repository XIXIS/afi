import * as Actions from '../actions';

const initialState = {
  error: '',
  success: '',
  data: null,
  timesheets: {
    pages: 0,
    pageNumber: 1,
    data: []
  },
  timesheet: null
};

const auth = function (state = initialState, action) {
  switch (action.type) {
    case Actions.SET_TIMESHEETS_LIST: {
      return {
        ...state,
        timesheets: {...action.payload.timesheets}
      };
    }
    case Actions.SET_TIMESHEET: {
      return {
        ...state,
        timesheet: {...action.payload.timesheet}
      };
    }

    default: {
      return state;
    }
  }
};

export default auth;
