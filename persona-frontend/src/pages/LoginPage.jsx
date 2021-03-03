import React, { Component } from "react";
import Header from "./../components/Header";
import axios from "axios";

// Login page for application
class LoginPage extends Component {
  handleSubmit = (post) => {
    post.title = "USERNAME";

    axios
      .put("http://localhost:3333/user/save", { userName: "test" })
      .then((response) => this.setState({ status: response }));

    // Call the server
    console.log(this.state.status);
  };

  handleChange = ({ currentTarget: input }) => {
    const account = { ...this.state.account };
    account["userName"] = input.value;
    this.setState({ account });
  };

  state = {
    account: {
      userName: "",
      password: "",
    },
    status: "",
  };
  render() {
    return (
      <div className="App">
        <Header></Header>
        <h1>Persona.io</h1>

        <form onSubmit={this.handleSubmit}>
          <div className="form-group">
            <label htmlFor="username">Username</label>
            <input
              autoFocus
              onChange={this.handleChange}
              id="username"
              type="text"
              className="form-control"
            />
          </div>
          <div className="form-group">
            <label htmlFor="password">Password</label>
            <input id="password" type="password" className="form-control" />
          </div>
          <button className="btn btn-primary">Login</button>
        </form>

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
