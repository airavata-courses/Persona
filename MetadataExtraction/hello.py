from flask import Flask
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


sampleImagePath = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAQAAAAEACAYAAABccqhmAAAACXBIWXMAAAsTAAALEwEAmpwYAAAKT2lDQ1BQaG90b3Nob3AgSUNDIHByb2ZpbGUAAHjanVNnVFPpFj333vRCS4iAlEtvUhUIIFJCi4AUkSYqIQkQSoghodkVUcERRUUEG8igiAOOjoCMFVEsDIoK2AfkIaKOg6OIisr74Xuja9a89+bN/rXXPues852zzwfACAyWSDNRNYAMqUIeEeCDx8TG4eQuQIEKJHAAEAizZCFz/SMBAPh+PDwrIsAHvgABeNMLCADATZvAMByH/w/qQplcAYCEAcB0kThLCIAUAEB6jkKmAEBGAYCdmCZTAKAEAGDLY2LjAFAtAGAnf+bTAICd+Jl7AQBblCEVAaCRACATZYhEAGg7AKzPVopFAFgwABRmS8Q5ANgtADBJV2ZIALC3AMDOEAuyAAgMADBRiIUpAAR7AGDIIyN4AISZABRG8lc88SuuEOcqAAB4mbI8uSQ5RYFbCC1xB1dXLh4ozkkXKxQ2YQJhmkAuwnmZGTKBNA/g88wAAKCRFRHgg/P9eM4Ors7ONo62Dl8t6r8G/yJiYuP+5c+rcEAAAOF0ftH+LC+zGoA7BoBt/qIl7gRoXgugdfeLZrIPQLUAoOnaV/Nw+H48PEWhkLnZ2eXk5NhKxEJbYcpXff5nwl/AV/1s+X48/Pf14L7iJIEyXYFHBPjgwsz0TKUcz5IJhGLc5o9H/LcL//wd0yLESWK5WCoU41EScY5EmozzMqUiiUKSKcUl0v9k4t8s+wM+3zUAsGo+AXuRLahdYwP2SycQWHTA4vcAAPK7b8HUKAgDgGiD4c93/+8//UegJQCAZkmScQAAXkQkLlTKsz/HCAAARKCBKrBBG/TBGCzABhzBBdzBC/xgNoRCJMTCQhBCCmSAHHJgKayCQiiGzbAdKmAv1EAdNMBRaIaTcA4uwlW4Dj1wD/phCJ7BKLyBCQRByAgTYSHaiAFiilgjjggXmYX4IcFIBBKLJCDJiBRRIkuRNUgxUopUIFVIHfI9cgI5h1xGupE7yAAygvyGvEcxlIGyUT3UDLVDuag3GoRGogvQZHQxmo8WoJvQcrQaPYw2oefQq2gP2o8+Q8cwwOgYBzPEbDAuxsNCsTgsCZNjy7EirAyrxhqwVqwDu4n1Y8+xdwQSgUXACTYEd0IgYR5BSFhMWE7YSKggHCQ0EdoJNwkDhFHCJyKTqEu0JroR+cQYYjIxh1hILCPWEo8TLxB7iEPENyQSiUMyJ7mQAkmxpFTSEtJG0m5SI+ksqZs0SBojk8naZGuyBzmULCAryIXkneTD5DPkG+Qh8lsKnWJAcaT4U+IoUspqShnlEOU05QZlmDJBVaOaUt2ooVQRNY9aQq2htlKvUYeoEzR1mjnNgxZJS6WtopXTGmgXaPdpr+h0uhHdlR5Ol9BX0svpR+iX6AP0dwwNhhWDx4hnKBmbGAcYZxl3GK+YTKYZ04sZx1QwNzHrmOeZD5lvVVgqtip8FZHKCpVKlSaVGyovVKmqpqreqgtV81XLVI+pXlN9rkZVM1PjqQnUlqtVqp1Q61MbU2epO6iHqmeob1Q/pH5Z/YkGWcNMw09DpFGgsV/jvMYgC2MZs3gsIWsNq4Z1gTXEJrHN2Xx2KruY/R27iz2qqaE5QzNKM1ezUvOUZj8H45hx+Jx0TgnnKKeX836K3hTvKeIpG6Y0TLkxZVxrqpaXllirSKtRq0frvTau7aedpr1Fu1n7gQ5Bx0onXCdHZ4/OBZ3nU9lT3acKpxZNPTr1ri6qa6UbobtEd79up+6Ynr5egJ5Mb6feeb3n+hx9L/1U/W36p/VHDFgGswwkBtsMzhg8xTVxbzwdL8fb8VFDXcNAQ6VhlWGX4YSRudE8o9VGjUYPjGnGXOMk423GbcajJgYmISZLTepN7ppSTbmmKaY7TDtMx83MzaLN1pk1mz0x1zLnm+eb15vft2BaeFostqi2uGVJsuRaplnutrxuhVo5WaVYVVpds0atna0l1rutu6cRp7lOk06rntZnw7Dxtsm2qbcZsOXYBtuutm22fWFnYhdnt8Wuw+6TvZN9un2N/T0HDYfZDqsdWh1+c7RyFDpWOt6azpzuP33F9JbpL2dYzxDP2DPjthPLKcRpnVOb00dnF2e5c4PziIuJS4LLLpc+Lpsbxt3IveRKdPVxXeF60vWdm7Obwu2o26/uNu5p7ofcn8w0nymeWTNz0MPIQ+BR5dE/C5+VMGvfrH5PQ0+BZ7XnIy9jL5FXrdewt6V3qvdh7xc+9j5yn+M+4zw33jLeWV/MN8C3yLfLT8Nvnl+F30N/I/9k/3r/0QCngCUBZwOJgUGBWwL7+Hp8Ib+OPzrbZfay2e1BjKC5QRVBj4KtguXBrSFoyOyQrSH355jOkc5pDoVQfujW0Adh5mGLw34MJ4WHhVeGP45wiFga0TGXNXfR3ENz30T6RJZE3ptnMU85ry1KNSo+qi5qPNo3ujS6P8YuZlnM1VidWElsSxw5LiquNm5svt/87fOH4p3iC+N7F5gvyF1weaHOwvSFpxapLhIsOpZATIhOOJTwQRAqqBaMJfITdyWOCnnCHcJnIi/RNtGI2ENcKh5O8kgqTXqS7JG8NXkkxTOlLOW5hCepkLxMDUzdmzqeFpp2IG0yPTq9MYOSkZBxQqohTZO2Z+pn5mZ2y6xlhbL+xW6Lty8elQfJa7OQrAVZLQq2QqboVFoo1yoHsmdlV2a/zYnKOZarnivN7cyzytuQN5zvn//tEsIS4ZK2pYZLVy0dWOa9rGo5sjxxedsK4xUFK4ZWBqw8uIq2Km3VT6vtV5eufr0mek1rgV7ByoLBtQFr6wtVCuWFfevc1+1dT1gvWd+1YfqGnRs+FYmKrhTbF5cVf9go3HjlG4dvyr+Z3JS0qavEuWTPZtJm6ebeLZ5bDpaql+aXDm4N2dq0Dd9WtO319kXbL5fNKNu7g7ZDuaO/PLi8ZafJzs07P1SkVPRU+lQ27tLdtWHX+G7R7ht7vPY07NXbW7z3/T7JvttVAVVN1WbVZftJ+7P3P66Jqun4lvttXa1ObXHtxwPSA/0HIw6217nU1R3SPVRSj9Yr60cOxx++/p3vdy0NNg1VjZzG4iNwRHnk6fcJ3/ceDTradox7rOEH0x92HWcdL2pCmvKaRptTmvtbYlu6T8w+0dbq3nr8R9sfD5w0PFl5SvNUyWna6YLTk2fyz4ydlZ19fi753GDborZ752PO32oPb++6EHTh0kX/i+c7vDvOXPK4dPKy2+UTV7hXmq86X23qdOo8/pPTT8e7nLuarrlca7nuer21e2b36RueN87d9L158Rb/1tWeOT3dvfN6b/fF9/XfFt1+cif9zsu72Xcn7q28T7xf9EDtQdlD3YfVP1v+3Njv3H9qwHeg89HcR/cGhYPP/pH1jw9DBY+Zj8uGDYbrnjg+OTniP3L96fynQ89kzyaeF/6i/suuFxYvfvjV69fO0ZjRoZfyl5O/bXyl/erA6xmv28bCxh6+yXgzMV70VvvtwXfcdx3vo98PT+R8IH8o/2j5sfVT0Kf7kxmTk/8EA5jz/GMzLdsAAAAgY0hSTQAAeiUAAICDAAD5/wAAgOkAAHUwAADqYAAAOpgAABdvkl/FRgAAD+1JREFUeNrs3XlYlIeBx/EfQxAQUMNQBaMih+AR8IgVw+NVz2qerU+im9U2jVfVRuPWVGKiOVq1sV2V2F1N6lHvdDVPa7pq4pGqiUfQRDzBeAECQUEqYh5AGJFh/9htN8lmn2WY8533+/k3YeadGX5fZ+aFISDnYm6jAJiShbsAIAAACAAAAgCAAAAgAAAIAAACAIAAACAAAAgAAAIAgAAAIAAACAAAAgCAAAAgAAAIAAACAIAAAPCsh7gL4IvKb5UrPz9feXl5unnzhsrL/6qysjLdqahQVVWV7t+/r9ra2q99TXhEuEKCQ2S1WhVpjVRkpFUdO3ZU586xiouLV2znWIWFhXHnfkUAHwsOb7PV2ZSTk6MzZ87ozOnTys3N1Zdffun6p7sWi+Lj45XaM1UpKSkaMHCgoqOjCQDfgvD4v/Dl5Tp65Ig+/uhjnTx5UjabzfOvfwMteuZHz2heRoYsgeZ8NcxLAHhMTU2NPjzwoXbv3qXsU9lePx57g11bt26V7b5Nr772Gs8AAHe4dvWatmzZogP796uurs73RhAQoD0fvK/Y2FieAQCukpWVpc2bNulE1gmfPs7GxkZ9/NFHmjR5MgEAnHU6O1srV67U+XPnDfSexF95DwBwRt61a8rMfFPHjx0z3LG3i25HAIDmqKur0+/efltbNm9RQ0OD4Y7fEmjR0KFDCQDgqOPHjmnxosUqLS017G2YPft5dejQgQAATWWz2ZS5fIW2b99u6Nsxddo0zZg5w7SPIwGAw65evaoX52WooKDA8ON/4ecvmPqx5JeB4JB9e/fphxMnMn6eAcBM7Ha7Vq9apfXr1hv+tjB+AgAHX++/NH++Dh08xPgJAMykpqZG//z8HH322WeMnwDATCorKzX7uVnKyclh/AQAZlJdVa2pU6Yq79o1xu/HOAuA/+XevXuaMX064ycAMJv6+nq98LO5PO0nADCjXy1ZoqysLMZPAGA2W7ds0Xs732P8BABmk30qW5mZmYyfAMBsKu9U6sWMDNkb7IyfAMBsFi9apNu3bzN+AgCz2bN7jw4ePMj4CQDMpqysTEuXvsH4CQDMaMXy5aquqmb8BABmc+rUKR3Yf4DxEwCYTUNDg5a+sZTxgwCY0Xs7dxr25/wZPwGAE+rr67Vu7TrGDwJgRu/veV9lZWWMHwTAjK/9169bx/hBAMzo8OHD+uKLLxg/CIAZvbvjXcYPAmBGRUVF+vTkScYPAmBGf/rjHxk/CIAZNTY2av++/YwfBMCMci5cMMSpv6nTpjJ+D+NjwU3gwIEPDXGco8eMMe1jVFJSosOHD+tW2S1FRUVpyPeGKC4ujgDAeYcOGeP3/fPz8tS1a1fTPT7r163X6tWrvvaJTG9mZmrc+HF65dVXFRQURADQPDdv3NCNkhvGCEB+vuken5VvrtTGDRu+9b/t/NNOBQeHaMHCBbwHgOY5deqUYY41Ly+f8X/Djh3b3fr+DQHwcycNdO4/Py+P8X+DvcGu48eOEQA0z9mzZw1zrCUlJbLZbIz/G9z5V5oIgB+rrq42zOt/SbLb7bp+/Trj/4YL5y8QADju6pUrhjtmf34Z0JzxS1JBQYFqamoIABxzxYgB8NMzAc0d/9+eGRUVFhEAOOZ6gfGeTvvjmQBnxv/3x/J6AQGAY4z4yT/+9hLAFeOXpEKeAcBRpaWlhjtmfzoT4KrxS3LbB7kQAJ4B+BR/ORPgyvFL0p07FQQAjrl7964hj9voLwNcPX5JqrhNAOCAqqoqwx67kc8EuGP8//UM4A4BQNPV3qs17LEb9UyAu8YvSXW2OgIABwJQe8+4zwAM+BLAneOX5LY/4koA/FSdgd9JN9qZAHeP350IgJ+KCA837LEb6UyAkcdPAMDLABOPnwDAdwPg42cCPD3+0NBQAoCma9W6taGP35fPBHjjX/4WLVoQADRdeHi4275pzPwSwFtP+yMiIggAHNPm4TaGPXZfPBPgzdf81igrAYBjoqKiDHvsvnYmwNtv+LVrF00A4JiYmBhDH7+vvAzwhXf727b9DgGAY+Li4o0dAB84E+Arp/rat3/ELZfLHwbxY507xxr6+Pft3SdJSkhIUEJiouLi4hQcHGy68UtSYmIiAYCjAYgz9PGXlJRo/br1//N01WJRhw4dlJCYqMTEBLeGwdd+yCchIcEtlxuQczG3kan4p9raWvVPS/va35zzR64Og6+Nv1WrVvrkRBbPAOCY0NBQJScl69KlS359O+12u4qLi1VcXKyPDh92Kgy++OO9qampbrtsAuDnUnum+n0AXBWGfXv3auOGjT53O3r36UMA0Mxvnt699e6Od7kjmhAGX9XHjQHgNKCfS+vfXwEBAdwRBhUcHKxHUx4lAGieqKgodevejTvCoPr376+QkBACgOYbPHgId4JBDfmeex87AmACcXFx3AkGNWjQYAKA5rtw4YIWL1rEHWFAfb/bV23btSUAaP74Z06foerqau4MAxo7dqzbr4MAMH74oJCQEI0YOZIAgPGb0ajvf19hYWEEAIzfjCZNmuSR6yEAjB8+5vH0x9UlqQsBAOM3o8lTpnjsuggA44cP6dmrp9LT0wkAGL8ZzZ37gkevjwAwfviIAQMHqu93+xIAMH6zCQwM1Lx5P/f49RIAxg8f8OykZ5XYpQsBAOM3m5iYGM2aPdsr100AGD+87PVfvO7W3/knAIwfPmrixIkaMHCg166fADB+eEl8fLzmZWR49RgIAOOHFwSHBGt55goFhwQTADB+s1my5FdKSkry+nEQAMYPD5s+Y7pGjxntE8dCABg/PGjosGF6fs4cnzkeAsD44SH9+vXTsuXLZLFYCAAYv5mkpKTo31av8uifNycAjB8+ILFLF731u7c98hFfBIDxw8f+5d+2bZsefvhhnzw+AsD44Sbp6elav+H3Co8I99ljJACMH27w5FNPavXbb/nk0/6v4s+DM3648l9Ui0XzMubpWQ99qi8BYPzwEZGRkVqeuUL9+vUzTrB42Bg/nDdkyBD9edd/GGr8PANg/HBSWFiY5r/0kp4a95Qhj58AMH448a/+goUL1P6RRwx7GwgA44eDYjt31ssLXtaAAQMMf1sIAONHE0VFRWnqtGmaMHGCgoKC/OI2EQDGjyYO/+mnn/b6B3gQAMYPD+nRo4cmTJyg0aPH+N3wCQDjx7cIDw/X8BEjNGHiBPXo0cPvby8BYPxeGdm8jHm6mHtRR48dVfmtcq8eT6tWrTR4yGCNHDVK6enpatGihWkei4Cci7mNfEsyfk+Of+36dUpNTZUkNTY26vLlyzqRlaVz587r3LmzqrxT6dZjCAkJUZ8+fdQvrZ/6P/64unXtJkugOX8mjgAwfq+N//9SXFysy5cv63pBgQoLC1VQUKDS0lLdrbyrxsamf7tGRkYqOiZa0dExSkiIV3JyV3XtmqyOnTr51KfyEADGz/ibwG63q7KyUpWVlaqqqtKDBw8kSTabTUFBQWrZsqVCQ0PVsmVLWa1Wn/v0Hd4DYPyM3wkWi0VWq1VWq5U71UV4HsT4DTF+EADGz/hBABg/4wcBYPyMHwSA8TN+EADGz/hBABg/4wcBYPyMHwSA8TN+EADGz/hBABg/4wcBYPyMHwSA8TN+EADGz/gJAONn/IyfADB+MH4CwPjB+AkA42f8jJ8AMH7GDwLA+Bk/CADjZ/wgAIyf8YMAMH7GDwLA+Bk/CADjZ/wgAIyf8YMAMH7GDwLA+Bk/CADjZ/wgAIyf8YMAMH7GDwLA+Bk/CADjZ/wgAIyf8YMAMH7GDwLA+Bk/CADjZ/wgAIyf8cO8AWD8jB8mDQDjZ/wwaQAYP+OHSQPA+Bk/TBoAxs/4YdIAMH7GD5MGgPEzfpg0AAUFBZr93CzGz/hhtgCUl5dr5vQZunv3Lo8I44eZAmBvsOvFjBdVVlbGo8H4YbYArFmzRmdOn+aRYPwwWwDy8/O1du0aHgXGDzMG4NdvLJW9wc6jwPhhtgBkZWXp008/5RFg/DBjADb+fgP3PuOHlz3kjSu9cuWKy//1t1gs6tmzp7r36C6rNUoNDQ9UWlqqc2fPqaCggPEDvhKAvR/sddllBQcH60fPPKMfP/tjRUVFfev/c/XqVa1ds1YfHjjA+IGvCMi5mNvo6SsdMWy4S877JyYm6rf/+lvFdu7cpP//2NGjmj9/vqqrqhk/4I33AK5fv+6S8Xfr1k3b3nmnyeOXpIGDBmnr1m0Kjwhn/IA3AnA6O9vpy2jdurVWvbW6WUPuktRFy5YtY/yANwKQk5Pj9GU8P2eO2rVr1+yvHzhokEaOGsX4QQA8fYVFRcVOfb3VatW48eOcPo6ZP53J+EEAPH2FXxQ7F4DhI4YrKCjI6eNISkpSfHw84wcB8KSqqiqnvr5Xr94uO5ZevXsxfhAAT6qtrXXq62Niol12LDExMYwfBMBIAh9y3c8uBQY+xPhBADypRYsWTn19xe0Klx1LRcVtxg8C4ElhYWFOff3nn1902bF8fvFzxg8C4Ent27d36usPHTzkkuO4ffu2zp8/z/hBADzJkR/d/Tb5+fk6fvy408exbes22e12xg8C4EnJyUlOX8aKZctls9ma/fVFhYX6wzvvMH4QAE9fYd++fZ2+jPz8fC365S/V2Oj4LzJWV1Vr7s/mOhUQxg8C0Ew9Hn1U4eHO/zbent179MrChQ4N+datW5o6ZYry8vIYP+CNAAQGBmrYsGEuuaw9u/fon/7x6f/3PYH6+nrt2L5D4558SpcuXWL8wH/zygeCfPLJJ/rpDNf+Mk5CQoKGDR+m7t17yBplVcODByotLdO5c2d18C8HVVFR4ZbbwvhBABxkt9v1g3/4gYoKCw195zF+8BKgOVdqsWjylMmMHzBjACRp7Nix6tChA+MHzBiAoKAgvbxwAeMHzBgASRo8eLCGuuiMAOMHDBYASVq8ZLFTn+/H+AEDB6B169Za8WamSz7mi/EDBguAJPXq1Uu/WfYvslgsjB8wWwAkaeTIkXrtF6/7VATatGnD+EEAPGX8+PFanrnCJ14OREdHa8u2rYwfBMDTzwQ2bt7k1TcG+zz2mP6w/d+98rHhgKkD8Lf3BHb++T2PnyK0BFr03KxZ2rRpk9q2bct3B/yeV34XwBFHjhzRb5b+WiUlJW69nrS0NC14ZaESEhL4rgAB8CX19fXatWuXNm/a7PJfIEpLS9PUn0xTeno63w0gAL7MbrfrxIkT2vfBXh06dEjV1dXNupzo6GiNeeIJjXlijJKTk/kuAAEwmoaGBl3MzVV2drauXLmqosJC3bx5UzU1Nbp//74kKTQ0VBEREerYqZNiYzspJSVFj/Xtq7i4OB55wMgBAOA8C3cBQAAAEAAABAAAAQBAAAAQAAAEAAABAEAAABAAAAQAAAEAQAAAEAAABAAAAQBAAAAQAAAEAAABAEAAABAAAAQAAAEAQAAAuNB/DgC0kwrGfJGm9gAAAABJRU5ErkJggg=="
# sampleImagePath = BytesIO(base64.b64decode(sampleImagePath))
sampleData = sampleImagePath.split(",")[1]
jpgtxt = base64.encodestring(open("0.jpg","rb").read())
# print(jpgtxt)
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

    with open("imageToSave.png", "wb") as fh:
        fh.write(base64.decodestring(str.encode(sampleData)))

    process = subprocess.Popen([metadataProcess, "./imageToSave.png"],
                                           stdout=subprocess.PIPE, stderr=subprocess.STDOUT,
                                           universal_newlines=True)
    infoDict = dict()
    for tag in process.stdout:
        line = tag.strip().split(':')
        infoDict[line[0].strip()] = line[-1].strip()
    
    for k,v in infoDict.items():
        print(k,':', v)

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

    

    


    return {"a":"b"}


if __name__ == "__main__":
    db.create_all()
    app.run(debug=False)
