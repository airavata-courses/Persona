import React, { Component } from "react";
import Header from './../components/Header';
// Login page for application
class LoginPage extends Component {
  state = {};
  render() {
    return (
      <div className="App">
        <Header></Header>
        <h1>Persona.io</h1>
        <h5>Username: </h5>
        <input type="text" />
        <h5>Password: </h5>
        <input type="password" />
        <br />
        <br />
        <button class="btn-primary btn">Log in</button>
        <h6>Log in with another account</h6>

        <ul class="nav flex-column">
          <li class="nav-item">
            <a class="nav-link active" href="#">
              Log in with Google
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="#">
              Log in with GitHub
            </a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="#">
              Log in with IU
            </a>
          </li>
        </ul>
      </div>
    );
  }
}

export default LoginPage;
