import  React  from 'react';
import styles from './LandingHero.css';
import logo from '../../logo.svg';
import homescreen from '../../apphomescreen.png';

class LandingHero extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
    };
  }
  componentDidMount() {
  }

  render() {
    return(
      <section class="hero">
          <section class="hero_text">
            <h1>Video Processing for Android</h1>
            <h2>Inversion helps transform your videos, allowing
                you to apply special FX, filters and more to your videos.
                Powered by the GPU.
            </h2>

            <button>Download</button>
            <button>Learn More</button>
          </section>

          <section class="hero_image">
            <img src={homescreen} className="App-logo" alt="logo" />
          </section>

      </section>
    )
  }
}

export default LandingHero