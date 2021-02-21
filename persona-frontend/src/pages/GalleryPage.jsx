import React, { Component } from 'react';
import Header from "./../components/Header";
import SearchBar from "./../components/SearchBar";
import ImageViewPanel from "./../components/ImageViewPanel";
import axios from 'axios';
import "./../css/gallery_page.css"
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

    testingFetch(){
        alert("I am working here");
        axios.get("http://127.0.0.1:5000/").then(res=>{
            console.log(res);
        })
    }

    render() {
        return (
            <div className="gallery-position">
                <Header />
                <SearchBar />
                <div>
                    <h2>Your Gallery</h2>
                    <div className="image-panel"><ImageViewPanel /></div>
                    <br/>
                    <button onClick={this.testingFetch} className="btn btn-primary btn-lg gallery-update-button">upload</button>
                    <br/>
                    <br/>
                </div>
            </div>
        );
    }
}

export default GalleryPage;