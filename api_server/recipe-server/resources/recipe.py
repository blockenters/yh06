from flask import request
from flask_restful import Resource

from mysql_connection import get_connection
from mysql.connector import Error


class RecipePublishResource(Resource):
    def put(self, recipe_id):

        try : 
            connection = get_connection()
            query = '''update recipe
                    set is_publish = 1
                    where id = %s;'''
            record = (recipe_id, )
            cursor = connection.cursor()
            cursor.execute(query, record)
            connection.commit()
            cursor.close()
            connection.close()
        except Error as e:
            if cursor is not None:
                cursor.close()
            if connection is not None:
                connection.close()
            return {'result':'fail',
                    'error' : str(e)}, 500

        return {"result" : "success"}
    
    def delete(self, recipe_id):
        try : 
            connection = get_connection()
            query = '''update recipe
                    set is_publish = 0
                    where id = %s;'''
            record = (recipe_id, )
            cursor = connection.cursor()
            cursor.execute(query, record)
            connection.commit()
            cursor.close()
            connection.close()
        except Error as e:
            if cursor is not None:
                cursor.close()
            if connection is not None:
                connection.close()
            return {'result':'fail',
                    'error' : str(e)}, 500

        return {"result" : "success"}




class RecipeResource(Resource):
    def get(self, recipe_id):
        
        # 1. 클라이언트로부터 데이터를 받는다.
        print(recipe_id)

        # 2. DB 로 부터 데이터를 가져온다.
        # 위의 recipe_id 에 해당하는 데이터를 가져온다.
        try : 
            connection = get_connection()

            print('커넥션 실행')

            query = '''select *
                    from recipe
                    where id = %s;'''
            record = (recipe_id, )
            print(record)
            cursor = connection.cursor(dictionary=True)

            print('커서 가져오기 성공')

            cursor.execute(query, record)

            print('쿼리문 실행')

            result_list = cursor.fetchall()
            print(result_list)

            cursor.close()
            connection.close()

        except Error as e:
            if cursor is not None:
                cursor.close()
            if connection is not None:
                connection.close()
            return {'result':'fail',
                    'error' : str(e)}, 500

        # 3. 응답할 데이터를 JSON으로 만든다.
        i = 0
        for row in result_list :
            result_list[i]['created_at'] = row['created_at'].isoformat()
            result_list[i]['updated_at'] = row['updated_at'].isoformat()
            i = i + 1
        
        if len(result_list) == 1 :
            return {"item" : result_list[0],
                    "result" : "success"}
        else :
            return {"result" : "fail", 
                    "error" : "해당 아이디는 존재하지 않습니다."},400

    def put(self, recipe_id):

        # 1. 클라이언트로 부터 데이터를 받아온다.
        print(recipe_id)

        data = request.get_json()

        # 2. DB 에 수정한다.
        try :
            connection = get_connection()

            query = '''update recipe
                        set name = %s, 
                            description = %s,
                            num_of_servings = %s,
                            cook_time = %s,
                            directions = %s
                        where id = %s;'''
            record = (  data['name'],
                        data['description'],
                        data['num_of_servings'],
                        data['cook_time'],
                        data['directions'],
                        recipe_id  ) 

            cursor = connection.cursor()
            cursor.execute(query, record)

            connection.commit()

            cursor.close()
            connection.close()

        except Error as e:
            if cursor is not None:
                cursor.close()
            if connection is not None:
                connection.close()
            return {'result':'fail',
                    'error' : str(e)}, 500

        # 3. 클라이언트에 JSON 리턴한다.

        return {'result' : 'success'}

    def delete(self, recipe_id) :

        try :
            connection = get_connection()
            query = '''delete from recipe
                        where id = %s;'''
            record = (recipe_id , )
            cursor = connection.cursor()
            cursor.execute(query, record)
            connection.commit()
            cursor.close()
            connection.close()
        except Error as e:
            if cursor is not None:
                cursor.close()
            if connection is not None:
                connection.close()
            return {'result':'fail',
                    'error' : str(e)}, 500

        return {"result" : "success"}

class RecipeListResource(Resource) :

    def post(self) :
        
        # 1. 클라이언트가 보내준 데이터가 있으면 
        #    그 데이터를 받아준다.
        data = request.get_json()

        # 2. 이 정보를 DB에 저장한다.
        try :
            ### 1. DB에 연결
            connection = get_connection()
            ### 2. 쿼리문 만들기 
            query = '''insert into recipe 
                        (user_id, name, description, num_of_servings, cook_time, directions)
                        values
                        (%s, %s , %s, %s, %s, %s );'''

            ### 3. 쿼리에 매칭되는 변수 처리 => 튜플로!!
            record = (data['user_id'] , data['name'], data['description'], data['num_of_servings'], data['cook_time'], data['directions'])

            ### 4. 커서를 가져온다.
            cursor = connection.cursor()

            ### 5. 쿼리문을 커서로 실행한다.
            cursor.execute(query, record)

            ### 6. DB에 완전히 반영하기 위해서는 commit한다.
            connection.commit()

            ### 7. 자원 해제
            cursor.close()
            connection.close()
            
        except Error as e :
            if cursor is not None :
                cursor.close()
            if connection is not None :
                connection.close()
            return {'result' : 'fail', 'error' : str(e)}, 500
        
        return {'result' : 'success'}, 200

    def get(self) :

        # 1. 클라이언트가 보낸 데이터가 있으면
        #    받아준다.

        offset = request.args['offset']
        limit = request.args['limit']

        print(offset , limit)

        # 2. DB로부터 데이터를 가져온다. 
        try :
            
            connection = get_connection()

            query = '''select *
                        from recipe
                        limit '''+offset+''', '''+limit+''';'''
            # record = ()

            cursor = connection.cursor(dictionary=True)

            cursor.execute(query)

            result_list = cursor.fetchall()

            print(result_list)

            cursor.close()
            connection.close()

        except Error as e:
            if cursor is not None:
                cursor.close()
            if connection is not None:
                connection.close()
            return {'result' : 'fail', 'error' : str(e)}, 500      

        # 3. 클라이언트에 json 만들어서 응답한다.       
        i = 0
        for row in result_list :
            result_list[i]['created_at'] = row['created_at'].isoformat()
            result_list[i]['updated_at'] = row['updated_at'].isoformat()
            i = i + 1

        print()
        print(result_list)

        return { 'items' : result_list,
                'count' : len(result_list),
                  'result' : 'success'  }

