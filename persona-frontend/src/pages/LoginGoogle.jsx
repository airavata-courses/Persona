import React, { Component } from "react";
import { GoogleLogin } from "react-google-login";
import axios from "axios";

class LoginGoogle extends Component {
  responseGoogle = (response) => {
    console.log("Success");
    console.log(response);

    const id = { userName: response["profileObj"]["email"] };
    axios
      .post("http://149.165.172.87/user/save", id)
      .then((response) => {
        localStorage.setItem("username", id);
        console.log(response)
      });
    window.location = "/gallery";
  };

  responseGoogleFailure = (response) => {
    console.log("Failure");
    console.log(response);
  };

  state = {};
  render() {
    return (
      <div>
        <GoogleLogin
          clientId="1036659448607-bdk60uuo2ti0pvp6oe8a00viu24jqf1v.apps.googleusercontent.com"
          buttonText="Log in with Google"
          onSuccess={this.responseGoogle}
          onFailure={this.responseGoogleFailure}
          cookiePolicy={"single_host_origin"}
        />
      </div>
    );
  }
}

export default LoginGoogle;
