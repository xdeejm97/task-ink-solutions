import React, { useState } from 'react';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom';
import CitySearch from './components/CitySearch'; // Adjust the path as necessary
import NewsList from './components/NewsList'; // Adjust the path as necessary

const App = () => {
    const [selectedCity, setSelectedCity] = useState(null); // Initially, there is no selected city

    const handleCitySelect = (city) => {
        // Update the selected city with name and state
        setSelectedCity(city);
    };

    return (
        <Router>
            <div>
                <h1>City News Search USA</h1>
                {/* Home Button */}
                <Link to="/">
                    <button style={{ marginBottom: '20px' }}>Home</button>
                </Link>

                <Routes>
                    <Route path="/" element={<CitySearch onCitySelect={handleCitySelect} />} />
                    <Route
                        path="/news"
                        element={<NewsList selectedCity={selectedCity?.name} selectedState={selectedCity?.state} />}
                    />
                </Routes>
            </div>
        </Router>
    );
};

export default App;
