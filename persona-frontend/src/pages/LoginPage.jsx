import React, { Component } from "react";
import Header from "./../components/Header";
import axios from "axios";
import LoginGoogle from "./LoginGoogle";
import GitHubLogin from 'react-github-login';

// Login page for application
class LoginPage extends Component {
  handleSubmit = (post) => {
    post.title = "USERNAME";

    axios
      .put("http://149.165.172.87/user/save", { userName: "test" })
      .then((response) => this.setState({ status: response }));

    // Call the server
    console.log(this.state.status);
  };

  componentDidMount() {
    var code = window.location.href.split("?code=")[1];
    if (code) {
      axios
        .post(
          "http://149.165.172.87/user/save",
          { userName: code },
          {
            headers: {
              "Content-type": "application/json",
            },
          }
        )
        .then((response) => {
          console.log(response);
          localStorage.setItem("username", code);
          window.location.href = "/gallery";
        });
    }
  }

  handleChange = ({ currentTarget: input }) => {
    const account = { ...this.state.account };
    account["userName"] = input.value;
    this.setState({ account });
  };

  handleGithubLogin() {
    // axios
    //   .get(
    //     "https://github.com/login/oauth/authorize",
    //     {
    //       client_id: process.env.REACT_APP_GITHUB_CLIENT_ID,
    //       redirect_uri: process.env.REACT_APP_GITHUB_CALLBACK_URL,
    //     },
    //     {
    //       headers: {
    //         "Content-type": "application/json",
    //       },
    //     }
    //   )
    //   .then((response) => {
    //     console.log(response);
    //   });

    // console.log(process.env.REACT_APP_GITHUB_CLIENT_ID);
    // alert("I want to login with github", process.env.REACT_APP_GITHUB_CLIENT_ID);
  }

  state = {
    account: {
      userName: "",
      password: "",
    },
    client_id: process.env.REACT_APP_GITHUB_CLIENT_ID,
    // callback_url: process.env.REACT_APP_GITHUB_CALLBACK_URL,
    callback_url: "http://149.165.172.87/",
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
            <LoginGoogle />
          </li>
          <li class="nav-item">
            {/* <a class="nav-link" href="#" onClick={this.handleGithubLogin}>
              Log in with GitHub
            </a> */}
            <GitHubLogin clientId={process.env.REACT_APP_GITHUB_CLIENT_ID}
            onSuccess={(response)=>{
              console.log(response)
            }}
            onFailure={(response)=>{
              console.log(response)
            }}/>
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
