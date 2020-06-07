import * as Actions from '../actions';

const initialState = {
  error: '',
  success: '',
  data: null,
  clients: {
    pages: 0,
    pageNumber: 1,
    data: []
  },
  client: null
};

const auth = function (state = initialState, action) {
  switch (action.type) {
    case Actions.SET_CLIENTS_LIST: {
      return {
        ...state,
        clients: {...action.payload.clients}
      };
    }
    case Actions.SET_CLIENT: {
      return {
        ...state,
        client: {...action.payload.client}
      };
    }

    default: {
      return state;
    }
  }
};

export default auth;
