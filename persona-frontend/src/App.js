import logo from './logo.svg';
import GalleryPage from "./pages/GalleryPage";
import { BrowserRouter as Router, Route } from "react-router-dom";
import './App.css';
import LoginPage from './pages/LoginPage';


function App() {
  return (
    
    <Router>
    <Route path="/gallery" exact component={GalleryPage} />
    <Route path="/" exact component={LoginPage} />
  </Router>
  );
}

export default App;
