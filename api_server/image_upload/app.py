from flask import Flask
from flask_restful import Api

from resources.image import FileUploadResource
from resources.rekognition import ObjectDetectionResource

app = Flask(__name__)

api = Api(app)

# 경로와 리소스를 연결한다.
api.add_resource( FileUploadResource  , '/upload')
api.add_resource( ObjectDetectionResource, '/object_detection')

if __name__ == '__main__':
    app.run()



