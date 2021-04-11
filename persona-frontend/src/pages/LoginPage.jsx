import React, { Component } from "react";
import Header from "./../components/Header";
import axios from "axios";
import LoginGoogle from "./LoginGoogle";
import LoginGithub from "react-login-github";

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
    // https://github.com/login/oauth/access_token?client_id=Iv1.f41f34fd38e613e0&client_secret=16012d7b6ff40a6bc6bef551eb1f1b29c5a09a5d&code=e921b5823791f0256d72
    // alert(code);
    if (code) {
      const GITHUB_AUTH_ACCESSTOKEN_URL =
        "https://cors-anywhere.herokuapp.com/https://github.com/login/oauth/access_token";
      const CLIENT_ID = process.env.REACT_APP_GITHUB_CLIENT_ID;
      const CLIENT_SECRET = process.env.REACT_APP_GITHUB_SECRET;
      const CODE = code;

      axios({
        method: "post",
        url: GITHUB_AUTH_ACCESSTOKEN_URL,
        data: {
          client_id: CLIENT_ID,
          client_secret: CLIENT_SECRET,
          code: CODE,
        },
      })
        .then(function (response) {
          // res.redirect('http://myjsapp/' + response.data)
          console.log(response.data.split("&")[0].split("=")[1]);
          var access_token = response.data.split("&")[0].split("=")[1];
          axios
            .get("https://api.github.com/user", {
              headers: {
                Authorization: `token ${access_token}`,
              },
            })
            .then((response) => {
              console.log(response.data.id);
              var oauth_user_id = response.data.id
              axios
                .post(
                  "http://149.165.172.87/user/save",
                  { userName: response.data.id },
                  {
                    headers: {
                      "Content-type": "application/json",
                    },
                  }
                )
                .then((response) => {
                  console.log(response);
                  // sessionStorage.setItem("username", response.data.id)
                  localStorage.setItem("username", oauth_user_id);
                  alert(localStorage.getItem("username"));
                  alert(oauth_user_id)
                  // alert(sessionStorage.getItem("username"));
                  window.location.href = "/gallery";
                });
            });
        })
        .catch(function (error) {
          console.error("Error " + error.message);
        });
    }
  }

  handleChange = ({ currentTarget: input }) => {
    const account = { ...this.state.account };
    account["userName"] = input.value;
    this.setState({ account });
  };

  handleGithubLogin() {
    axios({
      method: "post",
      url: `https://github.com/login/oauth/access_token?scope=user&client_id=${this.state.client_id}&redirect_uri=${this.state.callback_url}`,
      data: {
        client_id: process.env.REACT_APP_GITHUB_CLIENT_ID,
        client_secret: process.env.REACT_APP_GITHUB_SECRET,
      },
    }).then((response) => {
      console.log(response);
    });
  }

  state = {
    account: {
      userName: "",
      password: "",
    },
    client_id: process.env.REACT_APP_GITHUB_CLIENT_ID,
    callback_url: process.env.REACT_APP_GITHUB_CALLBACK_URL,
    // callback_url: "http://149.165.172.87/",
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
            <a
              class="nav-link"
              href={`https://github.com/login/oauth/authorize?scope=user&client_id=${this.state.client_id}&redirect_uri=${this.state.callback_url}`}
            >
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
