// src/App.js

import React, { useState } from 'react';
import CitySearch from './components/CitySearch';
import NewsList from './components/NewsList';

const App = () => {
    const [selectedCity, setSelectedCity] = useState(null);

    const handleCitySelect = (cityName) => {
        setSelectedCity(cityName); // Update the selected city
    };

    return (
        <div>
            <h1>City News App</h1>
            <CitySearch onCitySelect={handleCitySelect} />
            {selectedCity && <NewsList selectedCity={selectedCity} />}
        </div>
    );
};

export default App;
