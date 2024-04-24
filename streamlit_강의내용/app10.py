# 스트림릿의 내장 차트 함수와 유명한 라이브러리인 plotly 차트

import streamlit as st
import pandas as pd
import matplotlib.pyplot as plt

import plotly.express as px

def main():
    # 스트림릿에서제공해 주는 차트
    # line_chart, area_chart

    df1 = pd.read_csv('./data/lang_data.csv')     
    print(df1)

    print(df1.columns[ 1 : ] )

    column_list = df1.columns[ 1 : ]

    choice_list = st.multiselect('언어를 선택하세요.', column_list)

    print(choice_list)

    if len(choice_list) != 0 :
        df_choice = df1[choice_list]

        st.dataframe(df_choice)

        st.line_chart(df_choice)
        st.area_chart(df_choice)

    df2 = pd.read_csv('./data/iris.csv')

    # 스트림릿이 제공하는 bar_chart
    print(df2.iloc[ : , 0 : -2+1 ])    

    df_iris = df2.iloc[ : , 0 : -2+1 ]

    st.bar_chart( df_iris )


    df3 = pd.read_csv('./data/location.csv', index_col=0)
    print(df3)

    st.map(df3)

    df4 = pd.read_csv('./data/prog_languages_data.csv', index_col=0)
    print(df4)

    # plotly 의 pie 차트
    fig1 = px.pie(data_frame= df4, names='lang', values='Sum', title='각 언어별 파이차트')  
    st.plotly_chart(fig1)

    # plotly 의 bar 차트

    print( df4.sort_values('Sum') )

    df_sorted = df4.sort_values('Sum', ascending=False)

    fig2 = px.bar(data_frame= df_sorted, x='lang', y='Sum')
    st.plotly_chart(fig2)



if __name__ == '__main__':
    main()


