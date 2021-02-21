import logo from './logo.svg';
import './App.css';
import LoginPage from './pages/LoginPage';
import Header from './components/Header';

function App() {
  return (
    <div className="App">
      <Header></Header>
      <LoginPage></LoginPage>
    </div>
  );
}

export default App;
