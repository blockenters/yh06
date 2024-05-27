from passlib.hash import pbkdf2_sha256

from config import Config

# 원문 비밀번호를, 단방향으로 암호화 하는 함수
def hash_password(original_password) :
    original_password = original_password + Config.SALT
    password = pbkdf2_sha256.hash(original_password)
    return password

# 유저가 로그인할때, 입력한 비밀번호가 맞는지 체크하는 함수.
def check_password(original_password, hashed_password) :
    original_password = original_password + Config.SALT
    return pbkdf2_sha256.verify(original_password, hashed_password)

# hashed_password = hash_password('1234')
# print(hashed_password)

# check = check_password('1234', '$pbkdf2-sha256$29000$k7IWorSWMoYwxngP4RwDoA$0BEydksWmnmJsF0FL4xcpSZLKMG2PEYMBiVYMo/Yp5c' )
# print(check)
