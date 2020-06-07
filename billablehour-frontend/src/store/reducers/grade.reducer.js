import * as Actions from '../actions';

const initialState = {
  grades: {
    pages: 0,
    pageNumber: 1,
    data: []
  },
  grade: null
};

const auth = function (state = initialState, action) {
  switch (action.type) {

    case Actions.SET_GRADES: {
      return {
        ...state,
        grades: {...action.payload.grades}
      };
    }

    case Actions.SET_GRADE: {
      return {
        ...state,
        grade: {...action.payload.grade}
      };
    }

    default: {
      return state;
    }
  }
};

export default auth;
