import React from 'react';
import ReactDOM from 'react-dom';
import Viewer from './Viewer';

function odeforms(objId, objType, urls) {
  ReactDOM.render(
    <Viewer
      objId={ objId }
      objType={ objType }
      urls={ urls }
    />,
    document.getElementById('ode_forms_panel')
  );
}

export default odeforms;