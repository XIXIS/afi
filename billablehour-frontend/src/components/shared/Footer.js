import React from 'react';
import {Link} from 'react-router-dom';

function Footer() {

  return (

    <footer className="page-footer footer-grey">
      <div className="container">
        <div className="row">
          <div className="col l4">
            <h5 className="grey-text text-lighten-2" style={{fontSize: 20, fontWeight: 300}}>LINKS</h5>
            <ul>
              <li><Link className="grey-text text-lighten-1" to="#!" style={{fontWeight: 300}}>Shop</Link></li>
              <li><Link className="grey-text text-lighten-1" to="#!" style={{fontWeight: 300}}>About</Link></li>
              <li><Link className="grey-text text-lighten-1" to="#!" style={{fontWeight: 300}}>Blog</Link></li>
            </ul>
          </div>
          <div className="col l8 s12">
            <h5 className="grey-text text-lighten-2" style={{fontSize: 20, fontWeight: 300}}>CONTACT US</h5>
            <p className="grey-text text-lighten-1" style={{fontWeight: 300}}>BlueCroft Building, Anyaa-Awoshie Highway, <br/>Accra-Ghana</p>
            <p className="grey-text text-lighten-1" style={{fontWeight: 300}}>(+233) 302-938-310 <br/>(+233) 506776953</p>
            <p className="grey-text text-lighten-1" style={{fontWeight: 300}}>contact@agripoolapp.com</p>
          </div>

        </div>
      </div>
      <div className="footer-copyright footer-grey">
        <div className="container grey-text text-lighten-1">
          © 2019 Copyright Text
          <Link className="grey-text text-lighten-1 right" to="#!">More Links</Link>
        </div>
      </div>
    </footer>

  );


}


export default Footer;