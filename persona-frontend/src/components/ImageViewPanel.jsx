import React, { Component } from "react";
import Gallery from "react-photo-gallery";
// import image from "./../assets/images/0.jpg";
import Popup from "reactjs-popup";
// import SearchBar from "material-ui-search-bar";

// import Carousel, { Modal, ModalGateway } from "react-images";

// Panel which appears when a picture on the homepage or
// a user's gallery page is selected.
class ImageViewPanel extends Component {
  constructor(props) {
    super(props);
    this.state = {
      myImageList: [1, 2, 3, 4, 5],
      viewClicked: false,
      viewID: 0,
      selectMultipleClicked: false,
    };

    this.importAll = this.importAll.bind(this);
    this.setViewClicked = this.setViewClicked.bind(this);
    this.setViewUnclick = this.setViewUnclick.bind(this);
    this.triggerSelect = this.triggerSelect.bind(this);
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
      // console.log(allModule[i].default);
    }
    // console.log(allModule);
    return resultList;
  }

  importAllGoogleDrive(curDict) {
    var resultList = [];
    for (var i = 0; i < curDict.length; i++) {
      var randomWidth = Math.floor(Math.random() * 2) + 3;
      var randomHeight = Math.floor(Math.random() * 2) + 3;
      // console.log(randomWidth, randomHeight, typeof randomWidth, typeof 9);
      resultList.push({
        src: "https://drive.google.com/open?id=" + curDict[i].fileId,
        width: randomWidth,
        height: randomHeight,
      });
      // console.log(allModule[i].default);
    }
    // console.log(allModule);
    return resultList;
  }

  componentDidMount() {
    // axios
    //   .post("http://127.0.0.1:5000/getFile", formData, {
    //     headers: {
    //       "Content-Type": "multipart/form-data",
    //     },
    //   }) // convert the images directly to a string
    //   .then((res) => {
    //     console.log(res);
    //   });

    var sampleImages = [
      {
          "id": 3,
          "fileName": "_MG_0807.JPG",
          "fileId": "1SAlrBNvKo4-M4fO1DTIfo0ogzCBhD4UH",
          "userName": "suresh"
      },
      {
          "id": 2,
          "fileName": "_MG_0829.JPG",
          "fileId": "12RmPxrehGGkeOu4rzIyi9OiQ2KQdX1SS",
          "userName": "suresh"
      }]
    this.setState({
      myImageList: this.importAllGoogleDrive(sampleImages)
    });
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
    // var curSelectState = this.state.selectMultipleClicked;
    this.setState({
      selectMultipleClicked: !this.state.selectMultipleClicked,
    });
  }

  render() {
    return (
      <div>
        {!this.state.selectMultipleClicked ? (
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
        )}
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
              <img src={this.state.myImageList[this.state.viewID].src}></img>
            </div>
          )}
        </Popup>
      </div>
    );
  }
}

export default ImageViewPanel;
