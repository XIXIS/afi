import React, {Component} from 'react';
import M from 'materialize-css';

class FarmsSideNav extends Component {

  constructor(props) {
    super(props);
    this.dropdown = React.createRef();
  }

  componentDidMount() {
    M.Dropdown.init(this.dropdown.current, {
      hover: true,
      coverTrigger: false
    });
  }


  render() {

    return (
      <div style={{width: '100%'}}>
        <div className="card main-green"
             style={{
               borderRadius: 10,
               padding: '12px 40px'
             }}>
          <h5 className='white-text' style={{fontWeight: 300}}>Farms</h5>
          <ul>
            <li>
              <a href='#!' className='white-text'><span style={{fontWeight: 300}}>Standard</span></a>
            </li>
            <li>
              <a href='#!' className='white-text'>
                <span style={{fontWeight: 300}}>Premium</span>
              </a>
            </li>
            <li style={{height: 50}} onClick={this.logout}>
              <a href='#!'><span style={{fontWeight: 300}} className='white-text'>Closed</span></a>
            </li>
          </ul>
        </div>
      </div>

    );

  }

}


export default FarmsSideNav;