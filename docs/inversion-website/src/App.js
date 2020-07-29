import React from 'react';
import './App.css';
import HeaderNav from './components/HeaderNav/HeaderNav';
import LandingHero from './components/LandingHero/LandingHero';

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <HeaderNav></HeaderNav>
        <LandingHero></LandingHero>
      </header>
    </div>
  );
}

export default App;
