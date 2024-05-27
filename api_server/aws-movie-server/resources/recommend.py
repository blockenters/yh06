from flask import request
from flask_jwt_extended import get_jwt_identity, jwt_required
from flask_restful import Resource
import numpy as np

from mysql_connection import get_connection
from mysql.connector import Error

import pandas as pd

class MovieRecommendResource(Resource):
    @jwt_required()
    def get(self):

        count = request.args.get('count')
        # 쿼리 파라미터로 넘어온 데이터는 문자열이므로
        # 숫자로 사용하려면 캐스팅 한다.
        count = int(count)

        user_id = get_jwt_identity()

        # 영화 리뷰 데이터가 필요하다.
        try :
            connection = get_connection()
            query = '''select m.title, r.userId, r.rating
                        from movie m
                        left join review r
                            on m.id = r.movieId;'''
            cursor = connection.cursor(dictionary=True)
            cursor.execute(query)

            result_list = cursor.fetchall()

            df = pd.DataFrame(data= result_list)

            print(df)

            df = df.pivot_table(index= 'userId', columns='title', values='rating')

            print(df)

            movie_corr = df.corr(min_periods= 50)

            query = '''select m.title, r.rating
                        from review r
                        join movie m
                            on r.movieId = m.id
                        where userId = %s;'''
            record = ( user_id , )
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
        
        # 내 별점정보를 기반으로, 추천영화 목록을 만든다.
        my_review = pd.DataFrame(data= result_list)

        movie_list = pd.DataFrame()

        for  i  in np.arange( my_review.shape[0] ) :
            title = my_review['title'][i]
            recom_movie = movie_corr[title].dropna().sort_values(ascending=False).to_frame()
            recom_movie.columns = ['correlation']
            recom_movie['weight'] = recom_movie['correlation'] * my_review['rating'][i]
            movie_list = pd.concat([movie_list, recom_movie])

        # 중복추천된 영화, 내가 이미 본 영화는 제거한다.
        for title in my_review['title'] :
            if title in movie_list.index :
                movie_list.drop(title, axis = 0, inplace=True)

        movie_list = movie_list.groupby('title')['weight'].max().sort_values(ascending=False)
        
        if movie_list.shape[0] < count :
            return {'result' : 'fail', 'error' : '추천영화갯수가 count 보다 적습니다.'}, 400

        # 판다스의 데이터프레임을, JSON으로 바꾼다.
        movie_list = movie_list.to_frame()
        movie_list = movie_list.reset_index()
        movie_list = movie_list.to_dict('records')

        return {'result' : 'success',
                'items' : movie_list,
                'count' : len(movie_list) }


