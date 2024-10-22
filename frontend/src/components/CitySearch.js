import React, {useState} from 'react';
import Select from 'react-select';
import axios from 'axios';
import {debounce} from 'lodash';
import '../styles/CitySearch.css';

const CitySearch = ({onCitySelect}) => {
    const [cityOptions, setCityOptions] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [currentPage, setCurrentPage] = useState(0);

    // Function to fetch cities from API
    const fetchCities = async (inputValue, page = 0) => {
        if (!inputValue) return;

        setIsLoading(true);
        try {
            const response = await axios.get(`http://localhost:8080/api/cities`, {
                params: {query: inputValue, page, size: 100}
            });

            // Format cities for react-select
            const options = response.data.map(city => ({
                label: `${city.name}, ${city.state}`, // Show city and state
                value: city.name, // Assuming city name is unique
            }));

            setCityOptions(options);
        } catch (error) {
            console.error("Error fetching cities: ", error);
        } finally {
            setIsLoading(false);
        }
    };

    // Debounce search requests to avoid too many API calls
    const debouncedFetchCities = debounce(fetchCities, 300);

    // Handle the input change in Select
    const handleInputChange = (inputValue) => {
        if (inputValue.length > 2) {
            debouncedFetchCities(inputValue);
            setCurrentPage(0);
        }
    };

    // Handle when a city is selected
    const handleChange = (selectedOption) => {
        onCitySelect(selectedOption.value); // Pass the city name to the parent
    };


    return (
        <div>
            <Select
                placeholder="Search for a city..."
                onInputChange={handleInputChange}
                options={cityOptions}
                isLoading={isLoading}
                isClearable
                onChange={handleChange} // Call handleChange on city selection
            />
        </div>
    );
};

export default CitySearch;
