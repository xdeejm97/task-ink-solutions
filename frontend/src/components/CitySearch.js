import React, { useState, useEffect } from 'react';
import Select from 'react-select';
import axios from 'axios';
import { debounce } from 'lodash';
import './styles/CitySearch.css';
import { useNavigate } from 'react-router-dom';

const CitySearch = ({ onCitySelect }) => {
    const [cityOptions, setCityOptions] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [isImporting, setIsImporting] = useState(false);
    const [error, setError] = useState(null);
    const [lastSelectedCity, setLastSelectedCity] = useState(null); // Track the last selected city
    const navigate = useNavigate();

    useEffect(() => {
        checkAndImportCities();
    }, []);

    const checkAndImportCities = async () => {
        try {
            const response = await axios.get('http://localhost:8080/api/cities');
            if (response.data.length === 0) {
                console.log("Cities not found, triggering import.");
                await importCities();
            } else {
                setCityOptions(formatCityOptions(response.data));
            }
        } catch (error) {
            console.error("Error checking cities:", error);
            setError("Error loading cities");
        }
    };

    const importCities = async () => {
        setIsImporting(true);
        try {
            await axios.post('http://localhost:8080/api/import-cities');
            console.log("City import initiated.");
            setTimeout(() => {
                fetchCities('', 0);
            }, 5000);
        } catch (error) {
            console.error("Error importing cities:", error);
            setError("Failed to import cities");
        } finally {
            setIsImporting(false);
        }
    };

    const fetchCities = async (inputValue) => {
        if (!inputValue?.trim()) return;

        setIsLoading(true);
        setError(null);
        try {
            const response = await axios.get('http://localhost:8080/api/cities', {
                params: { query: inputValue, size: 100 }
            });

            const options = formatCityOptions(response.data);
            setCityOptions(options);
        } catch (error) {
            console.error("Error fetching cities:", error);
            setError("Failed to fetch cities");
        } finally {
            setIsLoading(false);
        }
    };

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

    const debouncedFetchCities = debounce(fetchCities, 300);

    const handleInputChange = (inputValue) => {
        if (inputValue.length > 2) {
            debouncedFetchCities(inputValue);
        }
    };

    const handleChange = (selectedOption) => {
        if (selectedOption && selectedOption.value !== lastSelectedCity) {
            setLastSelectedCity(selectedOption.value);
            onCitySelect({ name: selectedOption.value, state: selectedOption.state });
            navigate("/news");
        }
    };

    return (
        <div className="city-search-container">
            {isImporting && <p className="importing-message">Importing cities from external source...</p>}
            {error && <p className="error-message">{error}</p>}
            <Select
                placeholder="Search for a city..."
                onInputChange={handleInputChange}
                options={cityOptions}
                isLoading={isLoading || isImporting}
                isClearable
                onChange={handleChange}
            />
        </div>
    );
};

export default CitySearch;
