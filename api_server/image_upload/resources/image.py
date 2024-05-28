from flask import request
from flask_restful import Resource 

from datetime import datetime

import boto3

from config import Config

class FileUploadResource(Resource):

    def post(self):
        
        # 1. 클라이언트로부터 데이터를 받아온다.
        #   파일은 request.files 안에 있고,
        #   텍스트는 request.form 안에 있다. 

        if 'photo' not in request.files :
            return {'result' : 'fail',
                    'error' : '파일을 업로드 하세요.'},400
        if 'content' not in request.form :
            return {'result' : 'fail',
                    'error' : '내용을 작성하세요.'},400
        
        file = request.files['photo']

        print(file)

        content = request.form['content']

        print(content)

        # 파일을 S3 에 업로드해야 하는데,
        # 먼저, 파일명은 유니크 해야 한다. 
        # 따라서 유니크한 파일명으로 바꿔서 업로드 한다.

        # 현재시간과 유저아이디등을 조합해서 만든다.
        current_time = datetime.now()
        file_name = current_time.isoformat().replace(':','_') + '.jpg'

        print(file_name)

        # 유저가 업로드한 파일명을, 내가 만든파일명으로 바꾼다.
        file.filename = file_name

        # S3 에 파일을 업로드한다.
        # aws의 서비스들을 파이썬코드로 작성할수 있는
        # boto3 라이브러리를 이용해서 코드를 작성한다.
        # ( 설치는, $ pip install boto3  )

        client = boto3.client('s3' , 
                     aws_access_key_id = Config.AWS_ACCESS_KEY,
                     aws_secret_access_key = Config.AWS_SECRET_ACCESS_KEY)
        
        try :
            client.upload_fileobj(file, 
                                  Config.S3_BUCKET,
                                  file_name,
                                  ExtraArgs = {'ACL':'public-read',
                                               'ContentType' : file.content_type})
        except Exception as e:
            return {'result' : 'fail',
                    'error' : str(e)}, 500
        
        return {'result' : 'success',
                'url' : Config.S3_URL + file_name }