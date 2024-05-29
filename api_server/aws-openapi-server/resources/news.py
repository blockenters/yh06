from flask import request
from flask_restful import Resource

import requests

from config import Config

class NewsSearchResource(Resource) :

    def get(self):

        if 'query' not in request.args :
            return {'result':'fail',
                    'error' : '검색어는 필수입니다.'},400
        
        keyword = request.args['query']

        # 네이버의 API를 호출한다.

        # 파이썬 코드로 GET, POST, PUT, DELETE API를
        # 처리해주는 라이브러리가 requests 다. 

        url = 'https://openapi.naver.com/v1/search/news.json'
        params = {'query' : keyword,
                  'display' : 30,
                  'sort' : 'date'}
        headers = {'X-Naver-Client-Id' : Config.X_NAVER_CLIENT_ID,
                   'X-Naver-Client-Secret' : Config.X_NAVER_CLIENT_SECRET}

        response = requests.get(url, 
                                params= params, 
                                headers= headers)
        # 응답으로부터 데이터를 json 으로 받는다.
        response = response.json()

        print(response)

        return {'result' : 'success',
                'items' : response['items'],
                'count' : len(response['items']) }




