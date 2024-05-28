from flask import request
from flask_restful import Resource 

from datetime import datetime

import boto3

from config import Config

class ObjectDetectionResource(Resource) :

    def post(self) :

        if 'photo' not in request.files:
            return {'result' : 'fail',
                    'error' : '사진은 필수입니다.'}, 400
        
        file = request.files['photo']

        if 'image' not in file.content_type :
            return {'result' : 'fail',
                    'error' : '이미지파일을 업로드하세요.'},400
        
        current_time = datetime.now()
        file_name = \
            current_time.isoformat().replace(':', '_') + '.jpg' 
        
        file.filename = file_name

        # rekognition 서비스를 이용하려면,
        # 먼저, S3에 이미지파일을 업로드 해놔야 한다.

        client = boto3.client('s3',
                     aws_access_key_id = Config.AWS_ACCESS_KEY,
                     aws_secret_access_key = Config.AWS_SECRET_ACCESS_KEY)
        try:
            client.upload_fileobj(file,
                                  Config.S3_BUCKET,
                                  file_name,
                                  ExtraArgs = {'ACL' : 'public-read',
                                               'ContentType':'image/jpeg'})
        except Exception as e:
            return {'result' : 'fail',
                    'error' : str(e)}, 500
        
        # 리코그니션을 이용한다.
        label_list = self.detect_labels(file_name, Config.S3_BUCKET)

        return {'result' : 'success',
                'labels' : label_list}

    def detect_labels(self, photo, bucket):

        client = boto3.client('rekognition',
                     'ap-northeast-2',
                     aws_access_key_id = Config.AWS_ACCESS_KEY,
                     aws_secret_access_key = Config.AWS_SECRET_ACCESS_KEY)

        response = client.detect_labels(Image={'S3Object':{'Bucket':bucket,'Name':photo}},
        MaxLabels=10,
        # Uncomment to use image properties and filtration settings
        #Features=["GENERAL_LABELS", "IMAGE_PROPERTIES"],
        #Settings={"GeneralLabels": {"LabelInclusionFilters":["Cat"]},
        # "ImageProperties": {"MaxDominantColors":10}}
        )

        print('Detected labels for ' + photo)
        print()
        print(response['Labels'])

        label_list = []

        for label in response['Labels']:
            print("Label: " + label['Name'])
            label_list.append(label['Name'])            
         
        return label_list







