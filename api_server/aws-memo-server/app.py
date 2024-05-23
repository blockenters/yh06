from flask import Flask 
from flask_jwt_extended import JWTManager
from flask_restful import Api

from config import Config
from resources.user import UserRegisterResource

app = Flask(__name__)

# 환경변수 셋팅
app.config.from_object(Config)

# JWT 매니저 초기화
jwt = JWTManager(app)

api = Api(app)

api.add_resource( UserRegisterResource , '/user/register')

if __name__ == '__main__':
    app.run()