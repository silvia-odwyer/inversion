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
    <section>

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
            </h2>

            </section>

            <section className="hero_image">
            </section>

        </section>

        <section className="hero">
            <section className="hero_text">
            <h1>Get Daily Inspiration</h1>
            <h2>
                Check back daily to view edits of your uploaded photos, and get inspiration!
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