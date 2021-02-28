import React, { Component } from "react";
import axios from "axios";

class LoginTest extends Component {
  handleChange = ({ currentTarget: input }) => {
    const account = { ...this.state.account };
    account["userName"] = input.value;
    this.setState({ account });
  };

  registerUser = () => {
    axios
      .post("http://localhost:3333/user/save", this.state.account)
      .then((response) => console.log(response));
  };

  state = {
    account: {
      userName: "",
    },
  };
  render() {
    return (
      <div>
        <input type="text" onChange={this.handleChange} />
        <input type="password" />
        <button onClick={this.registerUser} className="btn btn-primary">
          login
        </button>
      </div>
    );
  }
}

export default LoginTest;
