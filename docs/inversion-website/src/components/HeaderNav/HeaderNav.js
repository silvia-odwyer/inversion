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
      <nav>
          <h3>Inversion</h3>

        <ul>
          <li>Pricing</li>
          <li>Download</li>
          <li>Contact</li>
        </ul>
    </nav>
    )
  }
}

export default HeaderNav