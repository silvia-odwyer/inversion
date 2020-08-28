import  React  from 'react';
import styles from './Features.css';
import logo from '../../logo.svg';

class Features extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
    };
  }
  componentDidMount() {
  }

  render() {
    return(
    <section class="features">

        <section className="hero">
          <section className="hero_text">
            <h1>Image Filtering</h1>
            <h2>
                Filter your images, add special effects, text and more.
                Over one hundred filters are available, with new filter packs being 
                added daily.
            </h2>

          </section>

          <section className="hero_image">
          </section>

      </section>

        <section className="hero">
            <section className="hero_text">
            <h1>Add Video Filters</h1>
            <h2>
                Add filters to your videos, to reflect the mood and ambience of the moment.
                These video filters range from glitch effects to vintage tints and a whole lot more.
            </h2>

            </section>

            <section className="hero_image">
            </section>

        </section>

        <section className="hero">
            <section className="hero_text">
            <h1>Get Daily Inspiration</h1>
            <h2>
                Check back daily to view edits of your uploaded photos, or Remixes, 
                and unlock new limited edition filters too!
            </h2>

            </section>

            <section className="hero_image">
            </section>

        </section>

        
        <section className="hero">
            <section className="hero_text">
            <h1>Over 100 Filters</h1>
            <h2>
                No matter how you want to transform your image, choose from a selection of over 100 filters.

                From retro to glitch effects and everything in-between, Inversion contains filters for 
                every ambience.
            </h2>

            </section>

            <section className="hero_image">
            </section>

        </section>
    </section>
    )
  }
}

export default Features