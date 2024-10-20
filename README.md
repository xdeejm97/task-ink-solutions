# News app

A News Provider App aggregates news articles, allowing users to search and filter news by city. 
It integrates external APIs for news scraping and uses OpenAI to analyze and categorize articles as local or global, storing relevant details in a database for efficient retrieval.

## Features

* Introduced a REST API for managing city and news article data.
* Added functionality to import city data from an online CSV file.
* Implemented news scraping capabilities for local and global articles.
* OpenAI support for analyzing and categorizing aticles

## Download

Ensure that you have installed JDK 21 from  64-bit version. For Windows you can download from oracle.com 



## Local setup

Clone project from my github account - xdeejm97 - and then run in

### Setup backend
1. Run `mvn install` from the repository
2. Run `BackendApplication`
3. Open http://localhost:8080/api/import-cities in the browser. Then put api/cities endpoint, so you should be able to see a list of cities in the USA.
4. To scrape news you should put endpoint api/scrape-news/{city} to get news in proper city. To list the news use api/news/city endpoint
