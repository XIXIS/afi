import * as Actions from '../actions';

const initialState = {
  error: '',
  success: '',
  data: null,
  clients: {
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
    case Actions.SET_CLIENTS_LIST: {
      return {
        ...state,
        clients: {...action.payload.clients}
      };
    }

    default: {
      return state;
    }
  }
};

export default auth;
