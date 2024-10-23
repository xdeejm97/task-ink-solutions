import React, { useEffect, useState } from 'react';
import axios from 'axios';

const NewsList = ({ selectedCity }) => {
    const [newsArticles, setNewsArticles] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);

    // Function to trigger backend news scraping
    const scrapeNewsForCity = async (city) => {
        try {
            await axios.post(`http://localhost:8080/api/scrape-news/${city}`);
            console.log("Scraping triggered successfully for city:", city);
        } catch (error) {
            console.error("Error triggering scraping:", error);
            throw new Error("Failed to trigger news scraping");
        }
    };

    // Function to fetch news articles from backend after scraping
    const fetchNewsArticles = async (city) => {
        setIsLoading(true);
        setError(null); // Reset error state on new fetch

        try {
            const response = await axios.get(`http://localhost:8080/api/news/city`, {
                params: { city }
            });

            const filteredArticles = response.data.filter(article =>
                article.cityOfUSA.toLowerCase() === city.toLowerCase()
            );

            setNewsArticles(filteredArticles);
        } catch (error) {
            console.error("Error fetching news articles:", error);
            setError("Failed to fetch news articles");
        } finally {
            setIsLoading(false);
        }
    };

    useEffect(() => {
        if (!selectedCity) return;

        // Clear the previous news and errors when a new city is selected
        setNewsArticles([]);
        setError(null);

        // Trigger scraping on backend
        scrapeNewsForCity(selectedCity)
            .then(() => {
                // Wait 5 seconds for scraping to complete
                setTimeout(() => {
                    // Fetch the news articles after scraping
                    fetchNewsArticles(selectedCity);
                }, 1200);
            })
            .catch((error) => {
                setError("Failed to scrape news articles");
            });
    }, [selectedCity]); // Re-run the effect whenever selectedCity changes

    return (
        <div>
            <h2>News for {selectedCity}</h2>
            {isLoading && <p>Loading news articles...</p>}
            {error && <p>{error}</p>}
            <ul>
                {newsArticles.map(article => (
                    <li key={article.id}>
                        <h3>{article.localOrGlobal === 'local' ? 'Local' : 'Global'} news: {article.title}</h3>
                        <p>{article.description}</p>
                        <a href={article.url} target="_blank" rel="noopener noreferrer">
                            If you want to read more, click here!
                        </a>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default NewsList;
