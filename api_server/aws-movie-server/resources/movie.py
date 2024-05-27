from flask import request
from flask_jwt_extended import get_jwt_identity, jwt_required
from flask_restful import Resource

from mysql_connection import get_connection
from mysql.connector import Error

class MovieListResource(Resource):

    @jwt_required()
    def get(self):

        offset = request.args.get('offset')
        limit = request.args.get('limit')
        order = request.args.get('order')

        user_id = get_jwt_identity()

        try : 
            connection = get_connection()
            query = '''select m.id, m.title ,  
                            count(r.id) as reviewCnt , 
                            ifnull( avg(r.rating) ,  0 ) as avgRating,
                            if( f.id is null, 0, 1 ) as isFavorite
                        from movie m
                        left join review r
                            on m.id = r.movieId
                        left join favorite f 
                            on m.id = f.movieId and f.userId = %s
                        group by m.id
                        order by '''+ order +''' desc
                        limit '''+offset+''', '''+limit+''';'''
            record = (user_id , )
            cursor = connection.cursor(dictionary=True)
            cursor.execute(query, record)

            result_list = cursor.fetchall()

            cursor.close()
            connection.close()

        except Error as e:
            if cursor is not None:
                cursor.close()
            if connection is not None:
                connection.close()
            return {'result':'fail',
                    'error' : str(e)}, 500
        
        print(result_list)

        i = 0
        for row in result_list :
            result_list[i]['avgRating'] = float( row['avgRating'] )
            i = i + 1        
        
        return {'result' : 'success', 
                'items' : result_list,
                'count' : len(result_list)}
    

