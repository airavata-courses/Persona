from flask import Flask
from flask_mysqldb import MySQL
from flask_cors import CORS
from PIL import Image
from PIL.ExifTags import TAGS
import piexif
import subprocess
from sqlalchemy import create_engine, insert
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy_utils import database_exists, create_database


sampleImagePath = "C:\\Users\\Brock\\Persona\\persona-frontend\\src\\assets\images\\3.png"
metadataProcess = "./tools/exiftool(-k).exe"

engine = create_engine("mysql://root@localhost/metadataDB")
if not database_exists(engine.url):
    create_database(engine.url)

app = Flask(__name__)
CORS(app)

app.config["SQLALCHEMY_DATABASE_URI"] = "mysql://root@localhost/metadataDB"

db = SQLAlchemy(app)
# (image_name text, size int, image_type VARCHAR(255), width int, height int, color_type VARCHAR(255), megapixel int);"

class image_metadata(db.Model):
    dataId = db.Column(db.Integer, primary_key=True)
    image_name = db.Column(db.Text)
    size = db.Column(db.Integer, nullable=False)
    image_type = db.Column(db.String(255), nullable=False)
    width  = db.Column(db.Integer, nullable=False)
    height = db.Column(db.Integer, nullable=False)
    megapixel = db.Column(db.Integer)


@app.route('/')
def hello():
    process = subprocess.Popen([metadataProcess,sampleImagePath],
                                           stdout=subprocess.PIPE, stderr=subprocess.STDOUT,
                                           universal_newlines=True)
    infoDict = dict()
    for tag in process.stdout:
        line = tag.strip().split(':')
        infoDict[line[0].strip()] = line[-1].strip()

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

    

    # for k,v in infoDict.items():
    #     print(k,':', v)


    return {"a":"b"}


if __name__ == "__main__":
    db.create_all()
    app.run(debug=False)
