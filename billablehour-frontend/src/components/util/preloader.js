import React from 'react';

function PreLoader(props) {

  return (

    <div className={'preloader-wrapper active ' + props.size}>
      <div className="spinner-layer spinner-blue">
        <div className="circle-clipper left">
          <div className="circle"/>
        </div>
        <div className="gap-patch">
          <div className="circle"/>
        </div>
        <div className="circle-clipper right">
          <div className="circle"/>
        </div>
      </div>

      <div className="spinner-layer spinner-red">
        <div className="circle-clipper left">
          <div className="circle"/>
        </div>
        <div className="gap-patch">
          <div className="circle"/>
        </div>
        <div className="circle-clipper right">
          <div className="circle"/>
        </div>
      </div>

      <div className="spinner-layer spinner-yellow">
        <div className="circle-clipper left">
          <div className="circle"/>
        </div>
        <div className="gap-patch">
          <div className="circle"/>
        </div>
        <div className="circle-clipper right">
          <div className="circle"/>
        </div>
      </div>

      <div className="spinner-layer spinner-green">
        <div className="circle-clipper left">
          <div className="circle"/>
        </div>
        <div className="gap-patch">
          <div className="circle"/>
        </div>
        <div className="circle-clipper right">
          <div className="circle"/>
        </div>
      </div>
    </div>
  );


}


export default PreLoader;