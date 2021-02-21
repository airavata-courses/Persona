import React, { Component } from "react";
import Header from "./../components/Header";
import SearchBar from "./../components/SearchBar";
import ImageViewPanel from "./../components/ImageViewPanel";
import axios from "axios";
import "./../css/gallery_page.css";
// import SearchBar from "material-ui-search-bar";
// import SearchBar from 'react-native-search-bar';
// Page with photos uploaded by the user.
class GalleryPage extends Component {
  constructor(props) {
    super(props);
    this.state = {
      // myImageList: []
    };
  }

  testingFetch() {
    alert("I am working here");
    axios
      .post("http://127.0.0.1:5000/extract", { user: "a", fdfd: "b" })
      .then((res) => {
        console.log(res);
      });
  }

  updateImage(event) {
    console.log("I am triggered here fdafdsafdsa");
    const curImg = event.target.files[0];

    var reader = new FileReader();
    reader.onloadend = () => {
      var imgInfo = {
        img: reader.result,
        name: curImg.name,
        type: curImg.type,
      };

      if (curImg.type != "image/jpeg" && curImg.type != "image/png") {
        alert("Please provide a valid image file");
      } else {
        axios
          .post("http://127.0.0.1:5000/extract", {data: reader.result})
          .then((res) => {
            console.log(res);
          });
      }
    };
    reader.readAsDataURL(curImg);
    // console.log("i am changed here");
  }

  triggerUploadButton() {
    document.getElementById("imageupload").click();
  }

  render() {
    return (
      <div className="gallery-position">
        <Header />
        <SearchBar />
        <div>
          <h2>Your Gallery</h2>
          <div className="image-panel">
            <ImageViewPanel />
          </div>
          <br />
          <button
            onClick={this.triggerUploadButton}
            className="btn btn-primary btn-lg gallery-update-button"
          >
            upload
          </button>
          <input
            type="file"
            id="imageupload"
            onChange={(e) => {
              this.updateImage(e);
            }}
            style={{ visibility: "hidden" }}
          />
          <br />
          <br />
        </div>
      </div>
    );
  }
}

export default GalleryPage;
