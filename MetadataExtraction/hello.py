from flask import Flask
from flask_mysqldb import MySQL
from flask_cors import CORS
from PIL import Image
from PIL.ExifTags import TAGS
import piexif
import subprocess
from sqlalchemy import create_engine
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy_utils import database_exists, create_database


sampleImagePath = "C:\\Users\\Brock\\Persona\\persona-frontend\\src\\assets\images\\3.png"
metadataProcess = "./tools/exiftool(-k).exe"

engine = create_engine("mysql://root@localhost/newTest")
if not database_exists(engine.url):
    create_database(engine.url)

app = Flask(__name__)
CORS(app)

app.config["SQLALCHEMY_DATABASE_URI"] = "mysql://root@localhost/newTest"

db = SQLAlchemy(app)

class User(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(255), unique=True, nullable=False)
    email = db.Column(db.String(255), unique=True, nullable=False)

@app.route('/')
def hello():
    process = subprocess.Popen([metadataProcess,sampleImagePath],
                                           stdout=subprocess.PIPE, stderr=subprocess.STDOUT,
                                           universal_newlines=True)
    infoDict = dict()
    for tag in process.stdout:
        line = tag.strip().split(':')
        infoDict[line[0].strip()] = line[-1].strip()

    for k,v in infoDict.items():
        print(k,':', v)

    print("I am triggered here")
    return {"a":"b"}


if __name__ == "__main__":
    db.create_all()
    app.run(debug=False)
