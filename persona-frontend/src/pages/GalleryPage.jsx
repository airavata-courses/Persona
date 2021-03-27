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
      username: localStorage.getItem("username")
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
    const curImg = event.target.files[0]; // original image

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
        let formData = new FormData();

        formData.append("files", curImg);

        var axiosHttp = axios.create({
          baseURL: "http://localhost:2222",
          headers: {
            "Content-type": "application/json",
          },
        });


        axiosHttp.post("/file/upload/" + this.state.username, formData, {
          headers: {
            "Content-Type": "multipart/form-data",
          }
        }).then((res)=>{
          console.log(res);
          axios
          .post("http://metadata_service:5000/extract", { data: reader.result, filename: curImg.name}) // base64 (save it to local)
          .then((res) => {
            console.log(res);
            window.location.reload();
          }).catch((err) => {
            window.location.reload();
          });
          
        });


        // axios
        //   .post("http://127.0.0.1:5000/extract", formData, {
        //     headers: {
        //       "Content-Type": "multipart/form-data",
        //     },
        //   }) // convert the images directly to a string
        //   .then((res) => {
        //     console.log(res);
        //   });
        
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
      <div>
        <Header />
        <div className="gallery-position">
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
      </div>
      
    );
  }
}

export default GalleryPage;
