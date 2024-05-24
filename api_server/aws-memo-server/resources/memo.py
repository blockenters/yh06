from flask_jwt_extended import get_jwt_identity, jwt_required
from flask_restful import Resource
from flask import request
from mysql.connector import Error
from mysql_connection import get_connection

class MemoListResource(Resource) :
    
    @jwt_required()
    def post(self):

        # 1. 클라이언트로부터 데이터를 받는다.
        data = request.get_json()
        user_id = get_jwt_identity()

        # 2. 필수인 데이터가 없으면, 응답해준다.

        # 3. DB에 저장한다.
        try :
            connection = get_connection()
            query = '''insert into memo
                    (userId, title, date, content)
                    values
                    ( %s, %s, %s, %s );'''
            record = (user_id, data['title'], data['date'],data['content'])
            cursor = connection.cursor()
            cursor.execute(query, record)
            connection.commit()
            cursor.close()
            connection.close()

        except Error as e:
            if cursor is not None :
                cursor.close()
            if connection is not None :
                connection.close()
            return {'result' : 'fail', 'error' : str(e)}, 500

        # 4. 클라이언트에 응답

        return {'result' : 'success'}

    @jwt_required()
    def get(self):
        # 1. 클라이언트로부터 데이터를 받는다.
        user_id = get_jwt_identity()

        # 2. db 쿼리한다. 
        try :
            connection = get_connection()
            query = '''select * 
                        from memo
                        where userId = %s;'''
            record = (user_id, )
            cursor = connection.cursor(dictionary=True)
            cursor.execute(query, record)
            result_list = cursor.fetchall()
            cursor.close()
            connection.close()

        except Error as e:
            if cursor is not None :
                cursor.close()
            if connection is not None :
                connection.close()
            return {'result' : 'fail', 'error' : str(e)}, 500
                
        # 3. 결과를 json 으로 응답한다.
        i = 0
        for row in result_list:
            result_list[i]['date'] = row['date'].isoformat()
            result_list[i]['createdAt'] = row['createdAt'].isoformat()
            result_list[i]['updatedAt'] = row['updatedAt'].isoformat()
            i = i + 1

        return {'result':'success',
                'items' : result_list,
                'count' : len(result_list)}
    

class MemoResource(Resource) :

    @jwt_required()
    def put(self, memo_id):

        data = request.get_json()
        user_id = get_jwt_identity()

        try :
            connection = get_connection()
            query = '''update memo
                        set title = %s, 
                            date = %s, 
                            content= %s
                        where id = %s and userId = %s;'''
            record = (data['title'],
                      data['date'],
                      data['content'],
                      memo_id, 
                      user_id)
            cursor = connection.cursor()
            cursor.execute(query, record)
            connection.commit()
            cursor.close()
            connection.close()
            
        except Error as e:
            if cursor is not None :
                cursor.close()
            if connection is not None :
                connection.close()
            return {'result' : 'fail', 'error' : str(e)}, 500


        return {'result':'success'}

    @jwt_required()
    def delete(self, memo_id) :

        user_id = get_jwt_identity()

        try :
            connection = get_connection()
            query = '''delete from memo
                        where id = %s and userId = %s;'''
            record = (memo_id, user_id)
            cursor = connection.cursor()
            cursor.execute(query, record)
            connection.commit()
            cursor.close()
            connection.close()

        except Error as e:
            if cursor is not None :
                cursor.close()
            if connection is not None :
                connection.close()
            return {'result' : 'fail', 'error' : str(e)}, 500

        return {'result' : 'success'}



