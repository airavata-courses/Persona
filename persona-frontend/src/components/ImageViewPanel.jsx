import React, { Component } from "react";
import Gallery from "react-photo-gallery";
// import image from "./../assets/images/0.jpg";
import Popup from "reactjs-popup";
import axios from "axios";
// import SearchBar from "material-ui-search-bar";

// import Carousel, { Modal, ModalGateway } from "react-images";

// Panel which appears when a picture on the homepage or
// a user's gallery page is selected.
class ImageViewPanel extends Component {
  constructor(props) {
    super(props);
    this.state = {
      myImageList: [],
      viewClicked: false,
      viewID: 0,
      selectMultipleClicked: false,
      status: "gallery",
      username: "sure",
    };

    this.importAll = this.importAll.bind(this);
    this.setViewClicked = this.setViewClicked.bind(this);
    this.setViewUnclick = this.setViewUnclick.bind(this);
    this.triggerSelect = this.triggerSelect.bind(this);
    this.downloadAllImg = this.downloadAllImg.bind(this);
  }

  componentDidMount() {
    if (this.state.status == "gallery") {
      axios
        .get(
          "http://localhost:2222/file/getFiles?username=" + this.state.username
        )
        .then((res) => {
          console.log(res);
          this.setState({
            myImageList: this.importAllGoogleDrive(res.data),
          });
        });
    }
  }

  importAll(params) {
    // console.log(params);
    var allModule = params.keys().map(params);
    var resultList = [];
    for (var i = 0; i < allModule.length; i++) {
      var randomWidth = Math.floor(Math.random() * 2) + 3;
      var randomHeight = Math.floor(Math.random() * 2) + 3;
      // console.log(randomWidth, randomHeight, typeof randomWidth, typeof 9);
      resultList.push({
        src: allModule[i].default,
        width: randomWidth,
        height: randomHeight,
      });
    }
    return resultList;
  }

  importAllGoogleDrive(curDict) {
    var resultList = [];
    for (var i = 0; i < curDict.length; i++) {
      var randomWidth = Math.floor(Math.random() * 2) + 3;
      var randomHeight = Math.floor(Math.random() * 2) + 3;
      var nameSplit = curDict[i].fileName.split(".");
      var imgType = nameSplit[nameSplit.length - 1];
      var imgFileHeader = "";
      console.log(imgType);
      if (imgType == "jpg") {
        imgFileHeader = "data:image/jpeg;base64,";
      } else if (imgType == "png") {
        imgFileHeader = "data:image/png;base64,";
      } else {
        continue;
      }
      // console.log(randomWidth, randomHeight, typeof randomWidth, typeof 9);
      resultList.push({
        // src: "https://drive.google.com/open?id=" + curDict[i].fileId,
        // src: "https://drive.google.com/file/d/" + curDict[i].fileId + "/view",
        src: "https://drive.google.com/uc?export=view&id=" + curDict[i].fileId,
        width: randomWidth,
        height: randomHeight,
      });
      // console.log(i);
    }
    // console.log(allModule);
    return resultList;
  }

  setViewClicked(event, { photo, index }) {
    // alert(index);
    this.setState({
      viewClicked: true,
      viewID: index,
    });
  }

  setViewUnclick() {
    // alert("View Mode Closed");
    this.setState({
      viewClicked: false,
      viewID: 0,
    });
  }

  handleChange = (event) => {
    this.setState({
      [event.target.name]: event.target.value,
    });
  };

  triggerSelect() {
    this.setState({
      selectMultipleClicked: !this.state.selectMultipleClicked,
    });
  }

  downloadAllImg() {
    axios
      .post(
        "http://localhost:2222/file/download?username=" + this.state.username,
        [1, 8, 9, 10, 11],
        { responseType: "blob" }
      ) // base64 (save it to local)
      .then((res) => {
        console.log(res);
        var downloadTrigger = document.getElementById("downloadLink");
        var curBlob = new Blob([res.data], { type: "zip" });
        console.log(curBlob.type);
        var fileUrl = window.URL.createObjectURL(curBlob);
        downloadTrigger.href = fileUrl;
        downloadTrigger.download = "all_image.zip";
        downloadTrigger.click();
        window.URL.revokeObjectURL(fileUrl);

        // var curFile = new File([res.data], "alldata.zip");
        // console.log(curFile.type, curFile.size, curFile.);
      });
  }

  render() {
    return (
      <div>
        {/* <img src="https://drive.google.com/uc?export=view&id=1GjQt2RO1s_0HTWZSsiw46SqFMJOuRADd"></img>
        <img src='https://drive.google.com/uc?export=view&amp;id=1GjQt2RO1s_0HTWZSsiw46SqFMJOuRADd'/> */}
        <button
          className="btn btn-primary"
          onClick={this.downloadAllImg}
          style={{ marginRight: "10px" }}
        >
          Download ALL
        </button>
        <button className="btn btn-primary">Share</button>
        <a id="downloadLink" style={{ display: "none" }}></a>
        {/* {!this.state.selectMultipleClicked ? (
          <button className="btn btn-primary" onClick={this.triggerSelect}>
            Select Mutiple
          </button>
        ) : (
          <div>
            <button className="btn btn-secondary" onClick={this.triggerSelect}>
              Select Mutiple
            </button>
            <button className="btn btn-primary">Download</button>
            <button className="btn btn-primary">Share</button>
          </div>
        )} */}
        <Gallery
          photos={this.state.myImageList}
          onClick={this.setViewClicked}
        ></Gallery>
        <Popup
          open={this.state.viewClicked}
          modal
          nested
          closeOnDocumentClick={false}
          position="center"
        >
          {(close) => (
            <div>
              <button
                className="close"
                onClick={() => {
                  this.setViewUnclick();
                  close();
                }}
              >
                &times;
              </button>
              {this.state.myImageList[this.state.viewID] != null ? (
                <img src={this.state.myImageList[this.state.viewID].src}></img>
              ) : (
                <div></div>
              )}
            </div>
          )}
        </Popup>
      </div>
    );
  }
}

export default ImageViewPanel;
