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
    def get(self):

        offset = request.args.get('offset')
        limit = request.args.get('limit')

        try:
            connection = get_connection()
            query = '''select p.*, u.email, if( l.id is null, 0, 1) as isLike
                        from follow f
                        join posting p
                            on f.followeeId = p.userId
                        join user u
                            on p.userId = u.id
                        left join `like` l
                            on p.id = l.postingId
                        where followerId = 1
                        order by p.createdAt desc
                        limit '''+offset+''', '''+limit+''';'''
            cursor = connection.cursor(dictionary=True)
            cursor.execute(query)
            result_list = cursor.fetchall()
            cursor.close()
            connection.close()
        except Error as e:
            return {'result' : 'fail'}, 500
        
        i = 0
        for row in result_list:
            result_list[i]['createdAt'] = row['createdAt'].isoformat()
            result_list[i]['updatedAt'] = row['updatedAt'].isoformat()
            i = i + 1
        return {'result' : 'success',
                'items' : result_list,
                'count' : len(result_list)}

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

        # 3-2. rekognition 을 이용해서, object detection 하여,
        #      태그로 사용할 label 을 뽑는다.
        label_list = self.detect_labels(file.filename, Config.AWS_S3_BUCKET)

        label_str = ','.join(label_list)

        label_str = self.translate(label_str)

        label_list = label_str.split(', ')


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

            posting_id = cursor.lastrowid

            for label  in label_list :
                # tag 테이블에, label 이 있는지 확인해서, 
                # 있으면, 아이디를 가져오고, 없으면 인서트한 후에 아이디를 가져온다.
                query = '''select *
                        from tag
                        where name = %s;'''
                record = (label, )
                
                cursor = connection.cursor(dictionary=True)
                cursor.execute(query, record)

                result_list = cursor.fetchall()

                if len(result_list) == 1 :
                    tag_id = result_list[0]['id']
                else :
                    query = '''insert into tag
                                (name)
                                values
                                (%s);'''
                    record = (label, )
                    cursor = connection.cursor()
                    cursor.execute(query, record)
                    tag_id = cursor.lastrowid

                # 위의 tagId를 posting_tag 테이블에, postingId 와 함께 넣어준다.
                query = '''insert into posting_tag
                            (postingId, tagId)
                            values
                            ( %s, %s);'''
                record = (posting_id, tag_id)
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


    def detect_labels(self, photo, bucket):

        client = boto3.client('rekognition',
                     'ap-northeast-2',
                     aws_access_key_id = Config.AWS_ACCESS_KEY_ID,
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


    def translate(self, text) :
        client = boto3.client(service_name='translate', 
                              region_name='ap-northeast-2', 
                              aws_access_key_id = Config.AWS_TRANSLATE_ACCESS_KEY,
                              aws_secret_access_key = Config.AWS_TRANSLATE_SECRET_ACCESS)

        result = client.translate_text(Text= text,
                                       SourceLanguageCode="en", 
                                       TargetLanguageCode="ko")
        print('TranslatedText: ' + result.get('TranslatedText'))
        print('SourceLanguageCode: ' + result.get('SourceLanguageCode'))
        print('TargetLanguageCode: ' + result.get('TargetLanguageCode'))

        return result.get('TranslatedText')



