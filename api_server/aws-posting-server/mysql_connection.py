import mysql.connector

from config import Config

# mysql db 에 접속하는 함수
def get_connection() :
    connection = mysql.connector.connect(
        host = Config.HOST  ,
        database = Config.DATABASE,
        user = Config.DB_USER,
        password = Config.DB_PASSWORD
    )
    return connection



