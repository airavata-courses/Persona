import React, { Component } from "react";

// Top section of pages to be used for navigating
// between different parts of web application
class Header extends Component {
  state = {};
  render() {
    return (
      <div>
        <nav class="navbar navbar-expand-lg navbar-light bg-light">
          <a class="navbar-brand" href="#">
            Persona.io
          </a>

          <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav mr-auto">
              <li class="nav-item">
                <a class="nav-link" href="#">
                  Home
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="/gallery">
                  Gallery
                </a>
              </li>
              <li class="nav-item dropdown">
                <a class="nav-link" href="#">
                  Friends
                </a>
              </li>
              <li class="nav-item dropdown">
                <a class="nav-link" href="/" style={{color:"red"}}>
                  Logout
                </a>
              </li>
            </ul>
            <form class="form-inline my-2 my-lg-0">
              <input
                class="form-control mr-sm-2"
                type="search"
                placeholder="Search for images"
                aria-label="Search"
              ></input>
              <button
                class="btn btn-outline-success my-2 my-sm-0"
                type="submit"
              >
                Search
              </button>
            </form>
          </div>
        </nav>
      </div>
    );
  }
}

export default Header;
