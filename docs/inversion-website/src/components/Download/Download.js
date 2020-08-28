import  React  from 'react';
import styles from './Download.css';
import logo from '../../logo.svg';

class Download extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
    };
  }
  componentDidMount() {
  }

  render() {
    return(
    <section>        
        <article>
            <h1>Download</h1>
            <p>Ready to get started?</p>

            <button>Download</button>
        </article>

    </section>
    )
  }
}

export default Download