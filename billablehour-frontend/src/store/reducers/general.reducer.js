import * as Actions from '../actions';

const initialState = {
  showPreloader    : false,
  goTo             : '',
};

const auth = function (state = initialState, action) {
  switch ( action.type )
  {
    case Actions.SET_GENERAL_DATA:
    {
      return {
        ...initialState,
        ...action.payload
      };
    }
    case Actions.SET_GOTO:
    {
      return {
        ...state,
        ...action.payload
      };
    }
    case Actions.DO_NOTHING:{
      return state;
    }
    default:
    {
      return state
    }
  }
};

export default auth;
