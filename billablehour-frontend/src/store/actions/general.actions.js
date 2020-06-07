export const SET_GENERAL_DATA = 'GENERAL DATA';
export const SET_GOTO = 'GOTO';
export const DO_NOTHING = 'DO_NOTHING';

export function showOrHidePreloader(showOrHide) {
  return (dispatch) => {
    return dispatch({
      type: SET_GENERAL_DATA,
      payload: {showPreloader: showOrHide}
    });
  };
}

export function setGoto(page) {
  return (dispatch) => {
    return dispatch({
      type: SET_GOTO,
      payload: {goTo: page}
    });
  };
}


export function clearOverlays(){

  let overlays = document.getElementsByClassName("sidenav-overlay");
  if (overlays.length > 0) {
    for (let i = 0; i < overlays.length; i++) {
      overlays[i].style.display = 'none';
    }
  }

  let bodyTagNames = document.getElementsByTagName("body");
  // console.log(bodyTagNames);
  if (bodyTagNames.length > 0) {
    for (let i = 0; i < bodyTagNames.length; i++) {
      bodyTagNames[i].style.overflow = 'unset';
    }
  }

  return (dispatch) => {
    return dispatch({
      type: DO_NOTHING,
      payload: {}
    });
  };
}

