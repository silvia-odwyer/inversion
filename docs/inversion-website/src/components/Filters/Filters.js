import  React  from 'react';
import styles from './Filters.css';
import logo from '../../logo.svg';

class Filters extends React.Component {
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

        <h1>Filters</h1>
        <section className="filters">
            <article>
                <h1>Retro</h1>
                <p>Retro filters comprise grayscale, sepia, and monochrome effects, plus lens flares and gradient blends.</p>
            </article>

            <article>
                <h1>Glitch</h1>
                <p>Glitch effects comprise futuristic glitch FX, and more.</p>
            </article>
        </section>

    </section>
    )
  }
}

export default Filters