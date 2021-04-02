import React, { Component } from "react";
import axios from "axios";
import LoginGoogle from "./LoginGoogle";

class LoginTest extends Component {
  handleChange = ({ currentTarget: input }) => {
    const account = { ...this.state.account };
    account["userName"] = input.value;
    this.setState({ account });
  };

  registerUser = () => {
    axios
      .post("http://149.165.172.87/user/save", this.state.account)
      .then((response) => console.log(response));
  };

  state = {
    account: {
      userName: "",
    },
  };
  render() {
    return (
      <div className="App">
        <h1>Persona.io</h1>
        <h5>Username</h5>
        <input type="text" onChange={this.handleChange} />
        <h5>Password</h5>
        <input type="password" />
        <br />
        <button onClick={this.registerUser} className="btn btn-primary">
          login
        </button>
        <LoginGoogle />
      </div>
    );
  }
}

export default LoginTest;
