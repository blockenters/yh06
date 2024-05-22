from flask import Flask
from flask_restful import Api

from resources.recipe import RecipeListResource, RecipePublishResource, RecipeResource
from resources.user import UserLoginResource, UserLogoutResource, UserRegisterResource

from flask_jwt_extended import JWTManager
from config import Config

from resources.user import jwt_blacklist

app = Flask(__name__)

# 환경변수 셋팅
app.config.from_object(Config)

# JWT 매니저 초기화
jwt = JWTManager(app)

# 로그아웃된 토큰으로 요청하는 경우, 처리하는 함수 작성
@jwt.token_in_blocklist_loader
def check_if_token_is_revoked(jwt_header, jwt_payload):
    jti = jwt_payload['jti']
    return jti in jwt_blacklist


api = Api(app)

# 경로(path)와 리소스(API 코드)를 연결한다.
api.add_resource( RecipeListResource  , '/recipes')
api.add_resource( RecipeResource, '/recipes/<int:recipe_id>' )
api.add_resource( RecipePublishResource , '/recipes/<int:recipe_id>/publish')
api.add_resource( UserRegisterResource , '/users/register')
api.add_resource( UserLoginResource , '/users/login')
api.add_resource( UserLogoutResource , '/users/logout' )

if __name__ == '__main__' :
    app.run()