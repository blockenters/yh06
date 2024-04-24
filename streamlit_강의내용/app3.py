# 판다스 데이터 프레임을 웹 화면에 보여주는 방법

import streamlit as st
import pandas as pd

def main():
    df = pd.read_csv('./data/iris.csv')

    # 프린트함수는 디버깅용
    print(df)

    st.dataframe( df )

    # species 컬럼의 유니크 값을 화면에 표시

    print(df['species'].unique())

    st.write(df['species'].unique())

    st.text( '아이리스 꽃은 ' + df['species'].unique() +'로 되어있다.' )



if __name__ == '__main__' :
    main()