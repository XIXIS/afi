import * as Actions from '../actions';

const initialState = {
  isLoggedIn: false,
  validSession: true,
  error: '',
  success: '',
  user: localStorage.getItem('BHUser'),
  authToken: localStorage.getItem('BHAccessToken')
};

const auth = function (state = initialState, action) {
  switch (action.type) {
    case Actions.SET_USER_DATA: {
      return {
        ...state,
        ...action.payload
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
        validSession: false,
        isLoggedIn: false,
        authToken: null
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
