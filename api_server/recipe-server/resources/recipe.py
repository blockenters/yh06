from flask import request
from flask_restful import Resource

from mysql_connection import get_connection
from mysql.connector import Error

class RecipeListResource(Resource) :

    def post(self) :
        
        # 1. 클라이언트가 보내준 데이터가 있으면 
        #    그 데이터를 받아준다.
        data = request.get_json()

        # 2. 이 정보를 DB에 저장한다.
        try :
            ### 1. DB에 연결
            connection = get_connection()
            ### 2. 쿼리문 만들기 
            query = '''insert into recipe 
                        (name, description, num_of_servings, cook_time, directions)
                        values
                        ( %s , %s, %s, %s, %s );'''

            ### 3. 쿼리에 매칭되는 변수 처리 => 튜플로!!
            record = (data['name'], data['description'], data['num_of_servings'], data['cook_time'], data['directions'])

            ### 4. 커서를 가져온다.
            cursor = connection.cursor()

            ### 5. 쿼리문을 커서로 실행한다.
            cursor.execute(query, record)

            ### 6. DB에 완전히 반영하기 위해서는 commit한다.
            connection.commit()

            ### 7. 자원 해제
            cursor.close()
            connection.close()
            
        except Error as e :
            if cursor is not None :
                cursor.close()
            if connection is not None :
                connection.close()
            return {'result' : 'fail', 'error' : str(e)}, 500
        
        return {'result' : 'success'}, 200



