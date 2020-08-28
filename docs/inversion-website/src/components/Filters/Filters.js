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

        <h1 class="center">Filters</h1>
        
        <section className="filters">
            <article>
                <h1>Retro</h1>
                <p>Retro filters comprise grayscale, sepia, lomo, and monochrome effects, plus lens flares and gradient blends.</p>
            </article>

            <article>
                <h1>Glitch</h1>
                <p>Glitch effects comprise futuristic glitch FX, and more.</p>
            </article>

            
            <article>
                <h1>Gradient Blends</h1>
                <p>Gradient blends comprise a selection of blends, which are mixed with the photo 
                  to create a distinct atmosphere.</p>
            </article>

            <article>
                <h1>Bonus Filters</h1>
                <p>You also get limited edition bonus filters throughout the year,
                  which are available for that time period only. These can be unlocked 
                  by viewing photos in the Remix feature of the app.
                </p>
            </article>
        </section>

    </section>
    )
  }
}

export default Filters