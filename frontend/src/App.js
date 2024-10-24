import React, {useState} from 'react';
import {BrowserRouter as Router, Route, Routes, Link} from 'react-router-dom';
import CitySearch from './components/CitySearch';
import NewsList from './components/NewsList';
import './components/styles/App.css';

const App = () => {
    const [selectedCity, setSelectedCity] = useState(null);

    const handleCitySelect = (city) => {
        setSelectedCity(city);
    };

    return (
        <Router>
            <div className="app-container">
                <h1 className="app-header">City News Search USA</h1>
                <Link to="/">
                    <button className="home-button">Home</button>
                </Link>

                <div className="main-content">
                    <Routes>
                        <Route path="/" element={<CitySearch onCitySelect={handleCitySelect}/>}/>
                        <Route
                            path="/news"
                            element={<NewsList selectedCity={selectedCity?.name} selectedState={selectedCity?.state}/>}
                        />
                    </Routes>
                </div>
            </div>
        </Router>
    );
};

export default App;
