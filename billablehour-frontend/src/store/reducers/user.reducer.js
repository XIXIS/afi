import * as Actions from '../actions';

const initialState = {
  error: '',
  success: '',
  data: null,
  users: {
    pages: 0,
    pageNumber: 1,
    data: []
  }
};

const auth = function (state = initialState, action) {
  switch (action.type) {
    case Actions.SET_USER_PROFILE: {
      return {
        ...state,
        ...action.payload
      };
    }
    case Actions.SET_USERS_LIST: {
      return {
        ...state,
        users: {...action.payload.users}
      };
    }
    case Actions.REMOVE_USER_DATA: {
      return {
        ...initialState
      };
    }
    case Actions.USER_LOGGED_OUT: {
      return {
        ...initialState,
        validSession: false
      };
    }
    case Actions.ERROR: {
      return {
        ...state,
        error: action.payload,
        success: ''
      };
    }
    case Actions.SUCCESS: {
      return {
        ...state,
        success: action.payload,
        error: ''
      };
    }
    case Actions.LOGIN_SUCCESS: {
      return {
        ...state,
        isLoggedIn: true
      };
    }
    default: {
      return state;
    }
  }
};

export default auth;
