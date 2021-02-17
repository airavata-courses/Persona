import React, { Component } from 'react';
import Header from "./../components/Header";
import SearchBar from "./../components/SearchBar";
import ImageViewPanel from "./../components/ImageViewPanel";

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
                    <h1>Your Gallery</h1>
                    <ImageViewPanel />
                    <button>upload</button>
                </div>
            </div>
        );
    }
}
 
export default GalleryPage;