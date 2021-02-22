from flask import Flask, request
from flask_cors import CORS
from PIL import Image
from PIL.ExifTags import TAGS
import subprocess
from sqlalchemy import create_engine, insert
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy.sql import base
from sqlalchemy_utils import database_exists, create_database
from io import BytesIO
import base64


# sampleImagePath = ""
# # sampleImagePath = BytesIO(base64.b64decode(sampleImagePath))
# sampleData = sampleImagePath.split(",")[1]
# # jpgtxt = base64.encodestring(open("0.jpg","rb").read())
# # print(jpgtxt)
# metadataProcess = "./tools/exiftool(-k).exe"

engine = create_engine("mysql://root@localhost/metadataDB")
if not database_exists(engine.url):
    create_database(engine.url)

app = Flask(__name__)
CORS(app)

app.config["SQLALCHEMY_DATABASE_URI"] = "mysql://root@localhost/metadataDB"

db = SQLAlchemy(app)


class image_metadata(db.Model):
    dataId = db.Column(db.Integer, primary_key=True)
    image_name = db.Column(db.Text)
    size = db.Column(db.Float, nullable=False)
    image_type = db.Column(db.String(255), nullable=False)
    width = db.Column(db.Integer, nullable=False)
    height = db.Column(db.Integer, nullable=False)
    megapixel = db.Column(db.Float)


@app.route('/extract', methods=['POST', 'GET'])
def metadata_extraction():
    # print(reques)
    # print(request.json["data"])
    sampleImagePath = request.json["data"]
    # sampleImagePath = BytesIO(base64.b64decode(sampleImagePath))

    # add the following function to a loop
    sampleDataType, sampleData = sampleImagePath.split(",")
    print(sampleDataType)
    # jpgtxt = base64.encodestring(open("0.jpg","rb").read())
    # print(jpgtxt)
    metadataProcess = "./tools/exiftool(-k).exe"

    photoStorage = "imageToSave.png"
    if(sampleDataType == "data:image/jpeg;base64"):
        photoStorage = "imageToSave.jpg"

    with open(photoStorage, "wb") as fh:
        fh.write(base64.decodestring(str.encode(sampleData)))

    process = subprocess.Popen([metadataProcess, photoStorage],
                               stdout=subprocess.PIPE, stderr=subprocess.STDOUT,
                               universal_newlines=True)
    infoDict = dict()
    for tag in process.stdout:
        line = tag.strip().split(':')
        infoDict[line[0].strip()] = line[-1].strip()

    # for k, v in infoDict.items():
    #     print(k, ':', v)

    insertMetadata = image_metadata(
        image_name=infoDict["File Name"],
        size=infoDict["File Size"],
        image_type=infoDict["File Type"],
        width=infoDict["Image Width"],
        height=infoDict["Image Height"],
        megapixel=infoDict["Megapixels"])

    print(str(insertMetadata))
    print(infoDict["File Name"])

    db.session.add(insertMetadata)
    db.session.commit()
    # until here
    return "success"


if __name__ == "__main__":
    db.create_all()
    app.run(debug=False)
