import React, { useEffect, useState } from 'react';
import axios from 'axios';

const NewsList = ({ selectedCity }) => {
    const [newsArticles, setNewsArticles] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        if (!selectedCity) return;

        const fetchArticles = async () => {
            setIsLoading(true);
            setError(null); // Reset error state on new fetch
            try {
                const response = await axios.get(`http://localhost:8080/api/news/city`, {
                    params: { city: selectedCity }
                });

                const filteredArticles = response.data.filter(article =>
                    article.cityOfUSA.toLowerCase() === selectedCity.toLowerCase()
                );

                setNewsArticles(filteredArticles); // Set the filtered articles
            } catch (error) {
                console.error("Error fetching news articles: ", error);
                setError("Failed to fetch news articles");
            } finally {
                setIsLoading(false);
            }
        };

        // Call the async function
        fetchArticles();
    }, [selectedCity]); // Re-run the effect whenever selectedCity changes

    return (
        <div>
            <h2>News for {selectedCity}</h2>
            {isLoading && <p>Loading news articles...</p>}
            {error && <p>{error}</p>}
            {newsArticles.length === 0 && !isLoading && <p>No news articles found for {selectedCity}.</p>}
            <ul>
                {newsArticles.map(article => (
                    <li key={article.id}>
                        <h3>{article.localOrGlobal === 'local' ? 'Local' : 'Global'} news: {article.title}</h3>
                        <p>{article.description}</p>
                        <a href={article.url} target="_blank" rel="noopener noreferrer">
                            If you want to read more, click me!
                        </a>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default NewsList;
