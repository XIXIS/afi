import * as Actions from '../actions';

const initialState = {
  grades: {
    pages: 0,
    pageNumber: 1,
    data: []
  },
};

const auth = function (state = initialState, action) {
  switch (action.type) {

    case Actions.SET_GRADES: {
      return {
        ...state,
        grades: {...action.payload.grades}
      };
    }

    default: {
      return state;
    }
  }
};

export default auth;
