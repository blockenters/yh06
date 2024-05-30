from flask import request
from flask_jwt_extended import get_jwt_identity, jwt_required
from flask_restful import Resource

from mysql_connection import get_connection
from mysql.connector import Error

class LikeResource(Resource):
    @jwt_required()
    def post(self, posting_id):
        
        user_id = get_jwt_identity()

        try :
            connection = get_connection()
            query = '''insert into `like`
                    (postingId, userId)
                    values
                    ( %s, %s);'''
            record = (posting_id, user_id)
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

        return {'result':'success'}


    @jwt_required()
    def delete(self, posting_id):
        
        user_id = get_jwt_identity()

        try :
            connection = get_connection()
            query = '''delete from `like`
                    where postingId = %s and userId = %s;'''
            record = (posting_id, user_id)
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

        return {'result':'success'}





