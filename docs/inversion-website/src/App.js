import React from 'react';
import './App.css';
import HeaderNav from './components/HeaderNav/HeaderNav';
import LandingHero from './components/LandingHero/LandingHero';
import Features from './components/Features/Features';
import Filters from './components/Filters/Filters';

function App() {
  return (
    <div className="content">

      <section className="landing">

        <HeaderNav></HeaderNav>
        <LandingHero></LandingHero>
        <Features></Features>
        <Filters></Filters>

      </section>

    </div>
  );
}

export default App;
