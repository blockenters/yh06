from flask import Flask
from flask_jwt_extended import JWTManager
from flask_restful import Api

from config import Config
from resources.movie import MovieListResource
from resources.recommend import MovieRecommendResource
from resources.user import UserLoginResource, UserLogoutResource, UserRegisterResource, jwt_blacklist

app = Flask(__name__)

app.config.from_object(Config)

jwt = JWTManager(app)

# 로그아웃된 토큰으로 요청하는 경우, 처리하는 함수 작성
@jwt.token_in_blocklist_loader
def check_if_token_is_revoked(jwt_header, jwt_payload):
    jti = jwt_payload['jti']
    return jti in jwt_blacklist

api = Api(app)

# 경로와 리소스를 연결하는 코드 작성.
api.add_resource( UserRegisterResource , '/user/register')
api.add_resource( UserLoginResource  , '/user/login')
api.add_resource( UserLogoutResource , '/user/logout')

api.add_resource( MovieListResource, '/movie')
api.add_resource( MovieRecommendResource , '/movie/recommend')


if __name__ == '__main__':
    app.run()



