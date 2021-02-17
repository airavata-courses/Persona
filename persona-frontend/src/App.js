import logo from './logo.svg';
import GalleryPage from "./pages/GalleryPage";
import { BrowserRouter as Router, Route } from "react-router-dom";
import './App.css';

function App() {
  return (
    <Router>
      <Route path="/gallery" exact component={GalleryPage} />
    </Router>
    // <div className="App">
    //   <header className="App-header">
    //     <img src={logo} className="App-logo" alt="logo" />
    //     <p>
    //       Edit <code>src/App.js</code> and save to reload.
    //     </p>
    //     <a
    //       className="App-link"
    //       href="https://reactjs.org"
    //       target="_blank"
    //       rel="noopener noreferrer"
    //     >
    //       Learn React
    //     </a>
    //   </header>
    // </div>
  );
}

export default App;
