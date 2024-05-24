from flask import Flask 
from flask_jwt_extended import JWTManager
from flask_restful import Api

from config import Config
from resources.memo import MemoListResource, MemoResource
from resources.user import UserLoginResource, UserLogoutResource, UserRegisterResource

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

api.add_resource( UserRegisterResource , '/user/register')
api.add_resource( UserLoginResource , '/user/login')
api.add_resource( UserLogoutResource, '/user/logout')
api.add_resource( MemoListResource , '/memo')
api.add_resource( MemoResource, '/memo/<int:memo_id>')


if __name__ == '__main__':
    app.run()