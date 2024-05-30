import serverless_wsgi

from flask import Flask
from flask_restful import Api

from flask_jwt_extended import JWTManager
from config import Config

from resources.follow import FollowResource
from resources.like import LikeResource
from resources.posting import PostingListResource
from resources.user import UserLoginResource, UserLogoutResource, UserRegisterResource, jwt_blacklist

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
api.add_resource( UserLogoutResource , '/user/logout' )

api.add_resource( PostingListResource , '/posting')

api.add_resource( FollowResource, '/follow/<int:followee_id>')
api.add_resource( LikeResource , '/posting/<int:posting_id>/like')

def handler(event, context) :
    return serverless_wsgi.handle_request(app,event,context)

if __name__ == '__main__':
    app.run()