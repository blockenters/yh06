import streamlit as st
import pandas as pd

def run_eda() :
    st.subheader('EDA 화면')

    # iris.csv 파일 읽어와서
    # 여러 컬럼들 선택 가능토록 하여,
    # 선택한 컬럼들만 화면에 보여주고, 
    # 상관계수도 보여주도록 개발.

    df = pd.read_csv('./data/iris.csv')

    st.dataframe(df)

    column_list = df.columns

    choice_list = st.multiselect('컬럼을 선택하세요', column_list)

    print(choice_list)

    if choice_list != [] :

        st.dataframe( df[ choice_list ] )

        st.dataframe( df[choice_list].corr(numeric_only= True) )




