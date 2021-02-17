import React, { Component } from "react";
import image from "./../assets/images/0.jpg"

// Panel which appears when a picture on the homepage or
// a user's gallery page is selected.
class ImageViewPanel extends Component {
    constructor(props) {
        super(props);
        this.state = {
            myImageList: [1, 2, 3, 4, 5],
        };

        this.importAll = this.importAll.bind(this);
    }

    importAll(params) {
        console.log(params);
        var value = params.keys().map(params);
        console.log(value);
        return value;
    }

    componentDidMount() {
        this.setState({
            myImageList: this.importAll(
                require.context("./../assets/images", false, /\.(png|jpe?g|svg)$/)
            )
        })

    }

    render() {
        return (
            <div>
                {/* <img src={image}></img> */}
                {this.state.myImageList.map((curImg, index) => (
                    <img key={index} src={curImg.default} alt="not found"></img>
                ))}
            </div>
        );
    }
}

export default ImageViewPanel;
