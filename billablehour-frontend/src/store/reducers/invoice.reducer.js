import * as Actions from '../actions';

const initialState = {
  error: '',
  success: '',
  data: null,
  invoices: {
    pages: 0,
    pageNumber: 1,
    data: []
  },
  invoice: null
};

const auth = function (state = initialState, action) {
  switch (action.type) {
    case Actions.SET_INVOICES_LIST: {
      return {
        ...state,
        invoices: {...action.payload.invoices}
      };
    }
    case Actions.SET_INVOICE: {
      return {
        ...state,
        invoice: {...action.payload.invoice}
      };
    }

    default: {
      return state;
    }
  }
};

export default auth;
