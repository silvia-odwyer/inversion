import  React from 'react';
import styles from './FilterDirectory.css';

class FilterDirectory extends React.Component {
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
        <div>
            <div>
                <h1>Filter Directory</h1>
                <p>You'll find all available filters here.</p>
            </div>

            <section>
                <h1>Gradients</h1>
                <p>These gradients add a soft light gradient to your photo, to give it a boost of 
                    light.
                </p>
            </section>

            <section>
                <h1>Grayscale Gradients</h1>
                <p>These gradients add a grayscale gradient effect to your image.
                </p>
            </section>

            <section>
                <h1>Vintage</h1>
                <p>Bring your photos back in time with these vintage 
                    filters, comprising sepia, grayscale, monochrome tints and more.
                </p>
            </section>

            <section>
                <h1>Retro</h1>
                <p>Add a retro effect to your photos, to give them that retro 80s appeal, 
                    from VHS overlays to dust overlays and lens flares. 
                </p>
            </section>
            
        </div>

      </header>
    )
  }
}

export default FilterDirectory