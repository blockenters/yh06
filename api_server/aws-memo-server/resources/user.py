from email_validator import EmailNotValidError
from flask_jwt_extended import create_access_token
from flask_restful import Resource
from flask import request
from mysql.connector import Error
from email_validator import validate_email

from mysql_connection import get_connection
from utils import hash_password

class UserRegisterResource(Resource) :

    def post(self):

        # 1. 클라이언트로부터 데이터 받는다.
        data = request.get_json()
        print(data)

        # 2. 이메일, 비번, 닉네임 모두 있는지 확인한다.

        # 3. 이메일 주소 형식이 올바른지 확인한다.
        try:
            validate_email( data['email'] )
        except EmailNotValidError as e:
            return {'result':'fail',
                    'error' : str(e)}, 400
        
        # 4. 만약 비번길이가 정해져 있으면, 처리해준다.

        # 5. 비밀번호를 암호화한다.
        password = hash_password( data['password'] )
        print(password)

        # 6. db에 저장한다.
        try:
            connection = get_connection()
            query = '''insert into user
                        (email, password, nickname)
                        values
                        (%s, %s, %s);'''
            record = (data['email'], password, data['nickname'])
            cursor = connection.cursor()
            cursor.execute(query, record)
            connection.commit()

            user_id = cursor.lastrowid

            cursor.close()
            connection.close()

        except Error as e :
            if cursor is not None:
                cursor.close()
            if connection is not None:
                connection.close()
            return {'result':'fail',
                    'error' : str(e)}, 500
        
        # 7. access token 을 만든다.
        access_token = create_access_token(user_id)

        return {'result' : 'success',
                'accessToken' : access_token}






