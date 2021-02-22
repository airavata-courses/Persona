import React, { useState, useEffect } from 'react'
import axios from 'axios'
import GalleryPage from "./pages/GalleryPage";
import Search from './components/Search';
import Header from './components/Header'
import { BrowserRouter as Router, Route } from "react-router-dom";
import './App.css';

function App() {
  const [items, setItems] = useState([])
  const [isLoading, setIsLoading] = useState(true)
  const [query, setQuery] = useState('')
   
  useEffect(() => {
    const fetchItems = async () => {
      setIsLoading(true)
      const result = await axios(
        `http://localhost:2222/upload/suresh?file_name=${query}`
      )
      console.log(result.data)
      setItems(result.data)
      setIsLoading(false)
    }

    fetchItems()
  }, [query])
  
  return (
    <div className='container'>
      <Header />
      <Search getQuery={(q) => setQuery(q)} />
    <Router>
      <Route path="/gallery" exact component={GalleryPage} />
    </Router>
    </div>
  );
}

export default App;
