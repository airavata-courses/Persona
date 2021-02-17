import React, { Component } from 'react';
import Header from "./../components/Header";
import SearchBar from "./../components/SearchBar";
import ImageViewPanel from "./../components/ImageViewPanel";
// import SearchBar from "material-ui-search-bar";
// import SearchBar from 'react-native-search-bar';
// Page with photos uploaded by the user.
class GalleryPage extends Component {
    // constructor(props) {
    //     super(props);
    //     this.state = {
    //         myImageList: []
    //     };
    // }

    render() {
        return (
            <div>
                <Header />
                <SearchBar />
                <div>
                    <h2>Your Gallery</h2>
                    <ImageViewPanel />
                    <button>upload</button>
                </div>
            </div>
        );
    }
}

export default GalleryPage;