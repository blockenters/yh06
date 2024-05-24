from email_validator import EmailNotValidError
from flask_jwt_extended import create_access_token, get_jwt, jwt_required
from flask_restful import Resource
from flask import request
from mysql.connector import Error
from email_validator import validate_email

from mysql_connection import get_connection
from utils import check_password, hash_password

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

class UserLoginResource(Resource) :
    def post(self) :
        
        # 1. 클라이언트로부터 데이터를 받는다.
        data = request.get_json()

        if 'email' not in data or 'password' not in data:
            return {'result' : 'fail'}, 400
        if data['email'].strip() == '' or data['password'].strip() == '':  
            return {'result' : 'fail'}, 400
        # 2. DB로부터 이메일에 해당하는 유저 정보를 가져온다.
        try :
            connection = get_connection()
            query = '''select *
                        from user
                        where email = %s ;'''
            record = ( data['email'] ,  )
            cursor = connection.cursor(dictionary=True)
            cursor.execute(query, record)

            result_list = cursor.fetchall()

            print(result_list)

            cursor.close()
            connection.close()

        except Error as e:
            if cursor is not None:
                cursor.close()
            if connection is not None:
                connection.close()
            return {'result':'fail', 'error':str(e)},500

        # 3. 회원인지 확인한다.
        if result_list == [] :
            return {'result' : 'fail'} , 401

        # 4. 비밀번호를 체크한다.
        # 유저가 입력한 비번 data['password']
        # DB에 암호화된 비번 result_list[0]['password']
        isCorrect = check_password(data['password'] , result_list[0]['password'])
        if isCorrect == False :
            return {'result' : 'fail'} , 401

        # 5. 유저아이디를 가져온다.
        user_id = result_list[0]['id']

        # 6. JWT 토큰을 만든다.
        # access_token = create_access_token(user_id,
        #                                    expires_delta= datetime.timedelta(minutes=3))
        access_token = create_access_token(user_id)
        # 7. 클라이언트에 응답한다.

        return {'result' : 'success', 
                'access_token':access_token,
                'test' : 'hello'}

# 로그아웃된 토큰을 저장할, set 을 만든다. 
jwt_blacklist = set()

class UserLogoutResource(Resource) :

    @jwt_required()
    def delete(self):

        jti = get_jwt()['jti']
        jwt_blacklist.add(jti)
        return {'result' : 'success'}


