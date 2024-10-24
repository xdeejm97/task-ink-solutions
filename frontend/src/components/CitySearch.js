import React, {useState, useEffect, useCallback} from 'react';
import Select from 'react-select';
import axios from 'axios';
import {debounce} from 'lodash';
import './styles/CitySearch.css';
import {useNavigate} from 'react-router-dom';

const CitySearch = ({onCitySelect}) => {
    const [cityOptions, setCityOptions] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);
    const [lastSelectedCity, setLastSelectedCity] = useState(null);
    const navigate = useNavigate();

    const fetchCities = useCallback(
        debounce(async (inputValue) => {
            if (!inputValue?.trim()) return;

            setIsLoading(true);
            setError(null);
            try {
                const response = await axios.get('http://localhost:8080/api/cities', {
                    params: {query: inputValue, size: 100}
                });

                const options = formatCityOptions(response.data);
                setCityOptions(options);
            } catch (error) {
                console.error("Error fetching cities:", error);
                setError("Failed to fetch cities");
            } finally {
                setIsLoading(false);
            }
        }, 300),
        []
    );


    const checkAndImportCities = async () => {
        try {
            const response = await axios.get('http://localhost:8080/api/cities');
            if (response.data.length === 0) {
                console.log("Cities not found, triggering import.");
            } else {
                setCityOptions(formatCityOptions(response.data));
            }
        } catch (error) {
            console.error("Error checking cities:", error);
            setError("Error loading cities");
        }
    };
    useEffect(() => {
        checkAndImportCities();
    }, []);

    const formatCityOptions = (cities) => {
        const seenCities = new Set();
        return cities
            .filter(city => {
                const cityKey = `${city.name}-${city.state}`;
                if (!seenCities.has(cityKey)) {
                    seenCities.add(cityKey);
                    return true;
                }
                return false;
            })
            .map(city => ({
                label: `${city.name}, ${city.state}`,
                value: city.name,
                state: city.state
            }));
    };

    const handleInputChange = (inputValue) => {
        if (inputValue.length > 2) {
            fetchCities(inputValue); // Call debounced fetch function
        }
    };

    const handleChange = (selectedOption) => {
        if (selectedOption && selectedOption.value !== lastSelectedCity) {
            setLastSelectedCity(selectedOption.value);
            onCitySelect({name: selectedOption.value, state: selectedOption.state});
            navigate("/news");
        }
    };

    return (
        <div className="city-search-container">
            {error && <p className="error-message">{error}</p>}
            <Select
                placeholder="Search for a city..."
                onInputChange={handleInputChange}
                options={cityOptions}
                isLoading={isLoading}
                isClearable
                onChange={handleChange}
            />
        </div>
    );
};

export default CitySearch;
