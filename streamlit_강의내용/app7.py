# 파일을 업로드 하는 방법
# 이미지 파일 업로드 , CSV 파일 업로드

import streamlit as st
# 현재시간을 가져와서 유니크한 파일명 만드는데 사용하기 위해서
from datetime import datetime
import pandas as pd

from PIL import Image

# 디렉토리 정보와, 파일을 알려주면, 
# 해당 디렉토리에 파일을 저장하는 함수.
def save_uploaded_file(directory, file):
    # 1. 디렉토리가 있는지 확인하여, 없으면 디렉토리부터 만든다.
    import os 
    if not os.path.exists(directory) :
        os.makedirs(directory)
    # 2. 디렉토리가 존재하면, 파일을 저장한다.
    with open(os.path.join(directory, file.name), 'wb') as f:
        f.write(file.getbuffer())
    # 3. 저장이 완료되면, 유저한테 알린다.
    return st.success(f"{file.name} 이 {directory}에 저장되었습니다.")


def main():
    # 사이드바 만들기
    st.title('파일 업로드 프로젝트')

    menu = ['이미지파일 업로드', 'CSV 업로드', 'About']

    choice = st.sidebar.selectbox('메뉴', menu)

    print(choice)

    if choice == menu[0] :
        st.subheader('이미지 파일 업로드!')

        file = st.file_uploader('이미지 파일 선택하세요.', type= ['jpg', 'png', 'jpeg'] )

        # 유저가 올린 파일이 있을때만, 서버에 저장한다.
        if file is not None :            
            print(file.name)
            print(file.size)
            print(file.type)
            # 파일을 서버에 저장하기 위해서는 먼저!
            # 파일 이름을 유니크하게 만들어서 바꿔줘야 한다.
            # 현재시간과 유저의 아이디를 조합해서 만드는게 실무에서 가장 많이사용
            current_time = datetime.now()
            print( current_time.isoformat().replace(':','_') + '.jpg' )
            new_filename = current_time.isoformat().replace(':','_') + '.jpg'
            file.name = new_filename
            save_uploaded_file('image', file)

            
            st.image(file)

    elif choice == menu[1] :
        
        st.subheader('CSV 파일 업로드')
        file = st.file_uploader('CSV 파일 선택하세요.', type='csv')

        if file is not None :

            # 파일명을 유니크하게 만들어서 저장한다.
            current_time = datetime.now()

            print( current_time.isoformat().replace(':','_') + '.csv' )

            new_filename = current_time.isoformat().replace(':','_') + '.csv'

            file.name = new_filename

            save_uploaded_file('data', file)

            st.dataframe( pd.read_csv(file) )

    elif choice == menu[2] :
        st.subheader('파일 업로드 프로젝트 입니다.')
        


if __name__ == '__main__' :
    main()



