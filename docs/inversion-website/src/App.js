import React from 'react';
import './App.css';
import HeaderNav from './components/HeaderNav/HeaderNav';
import LandingHero from './components/LandingHero/LandingHero';
import Features from './components/Features/Features';

function App() {
  return (
    <div className="content">

      <section className="landing">
        <header className="App-header">
          <HeaderNav></HeaderNav>
        </header>
        <LandingHero></LandingHero>
        <Features></Features>
      </section>

    </div>
  );
}

export default App;
