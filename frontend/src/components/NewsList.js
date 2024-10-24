import React, {useEffect, useState} from 'react';
import axios from 'axios';
import './styles/NewsList.css';

const NewsList = ({selectedCity}) => {
    const [newsArticles, setNewsArticles] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);

    const scrapeNewsForCity = async (city) => {
        try {
            await axios.post(`http://localhost:8080/api/scrape-news/${city}`);
            console.log("Scraping triggered successfully for city:", city);
        } catch (error) {
            console.error("Error triggering scraping:", error);
            throw new Error("Failed to trigger news scraping");
        }
    };

    const fetchNewsArticles = async (city) => {
        setIsLoading(true);
        setError(null);

        try {
            const response = await axios.get(`http://localhost:8080/api/news/city`, {
                params: {city}
            });

            const filteredArticles = response.data.filter(article =>
                article.cityOfUSA.toLowerCase() === city.toLowerCase()
            );

            const uniqueArticles = removeDuplicateTitles(filteredArticles);

            setNewsArticles(uniqueArticles);
        } catch (error) {
            console.error("Error fetching news articles:", error);
            setError("Failed to fetch news articles");
        } finally {
            setIsLoading(false);
        }
    };

    const removeDuplicateTitles = (articles) => {
        const seenTitles = new Set();
        return articles.filter((article) => {
            if (!seenTitles.has(article.title)) {
                seenTitles.add(article.title);
                return true;
            }
            return false;
        });
    };

    useEffect(() => {
        if (!selectedCity) return;

        setNewsArticles([]);
        setError(null);

        scrapeNewsForCity(selectedCity)
            .then(() => {
                setTimeout(() => {
                    fetchNewsArticles(selectedCity);
                }, 1200);
            })
            .catch(() => {
                setError("Failed to scrape news articles");
            });
    }, [selectedCity]);

    return (
        <div className="news-container">
            <h2>News for {selectedCity}</h2>
            {isLoading && <div className="loading-spinner"></div>}
            {error && <p className="error-message">{error}</p>}
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
