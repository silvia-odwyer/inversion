import  React from 'react';
import styles from './HeaderNav.css';

class HeaderNav extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
    };
  }
  componentDidMount() {
  }

  render() {
    return(
      <header>
        <div id="navbar">
          <div class="nav_container">
          <h4 class="logo">Inversion</h4>
          <nav class="contright">
                <ul>
                  <li>Features</li>
                  <li>Download</li>
                  <li>Contact</li>
                  <li>Switch Theme</li>
                </ul>
              </nav>
            </div>
            
        </div>

      </header>
    )
  }
}

export default HeaderNav