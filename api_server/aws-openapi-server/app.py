import serverless_wsgi
from flask import Flask
from flask_restful import Api

from resources.news import NewsSearchResource

app = Flask(__name__)
api = Api(app)

api.add_resource( NewsSearchResource  , '/news/search')

def handler(event, context) :
    return serverless_wsgi.handle_request(app,event,context)

if __name__ == '__main__':
    app.run()