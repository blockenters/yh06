from datetime import datetime
from flask import request
from flask_jwt_extended import get_jwt_identity, jwt_required
from flask_restful import Resource

from config import Config
from mysql_connection import get_connection
from mysql.connector import Error

import boto3

class PostingListResource(Resource) :

    @jwt_required()
    def post(self):
        
        # 1. 클라언트로부터 데이터 받아온다.
        if 'image' not in request.files :
            return {'result' : 'fail',
                    'error' : '파일을 업로드 하세요.'},400
        if 'content' not in request.form :
            return {'result' : 'fail',
                    'error' : '내용을 작성하세요.'},400
        
        file = request.files['image']
        content = request.form['content']

        user_id = get_jwt_identity()

        # 2. 사진을 S3에 업로드 한다.
        if file is None :
            return {'result' : 'fail',
                    'error' : '파일을 업로드 하세요.'},400
        
        if 'image' not in file.content_type :
            return {'result' : 'fail', 
                    'error':'이미지파일만 업로드 가능합니다.'}, 400
        
        client = boto3.client('s3',
                              aws_access_key_id= Config.AWS_ACCESS_KEY_ID,
                              aws_secret_access_key = Config.AWS_SECRET_ACCESS_KEY)
        
        # 파일 이름을 유니크하게 만들어줘야 한다.
        current_time = datetime.now()
        file_name = current_time.isoformat().replace(':','_') + str(user_id) + '.jpg'
        file.filename = file_name

        try :
            print(Config.AWS_S3_BUCKET)
            client.upload_fileobj(file, 
                                  Config.AWS_S3_BUCKET,
                                  file.filename,
                                  ExtraArgs = {'ACL' : 'public-read', 
                                               'ContentType' : 'image/jpeg'})
        except Exception as e:
            return {'result' : 'fail', 'error':str(e)}, 500

        # 3. 업로드한 사진의 URL을 만든다.
        image_url = Config.AWS_FILE_URL + file.filename

        # 4. DB에 user_id, image_url, content 를 저장한다.
        try :
            connection = get_connection()
            query = '''insert into posting
                        (userId, imageUrl, content)
                        values
                        ( %s, %s, %s);'''
            record = (user_id, image_url, content)
            cursor = connection.cursor()
            cursor.execute(query, record)

            connection.commit()

            cursor.close()
            connection.close()

        except Error as e:
            if cursor is not None:
                cursor.close()
            if connection is not None:
                connection.close()
            return {'result':'fail',
                    'error' : str(e)}, 500

        # 5. 클라언트에 json으로 응답한다.

        return {'result' : 'success'}





