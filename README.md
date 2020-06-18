# kpuMap
맛동산, 맛있는 동네 산기대


![dd](https://user-images.githubusercontent.com/64695947/84397965-4ab9e980-ac3a-11ea-9e33-79368da06c09.png)
![main](https://user-images.githubusercontent.com/64695947/84969572-f213b080-b153-11ea-8b95-8195bfeafe91.png)
![list](https://user-images.githubusercontent.com/64695947/84969579-f4760a80-b153-11ea-8438-ae69a621fc89.png)
![search](https://user-images.githubusercontent.com/64695947/84969581-f63fce00-b153-11ea-9a52-d7a5b66cb366.png)
![detail](https://user-images.githubusercontent.com/64695947/84969586-f770fb00-b153-11ea-87a3-49fb24b6de95.png)
![call](https://user-images.githubusercontent.com/64695947/84969589-f93abe80-b153-11ea-9aa0-d8fa9c4f8411.png)

20-06-10
1. titlebar 제거
    style - <item name ="windowNoTitle">true</item>
2. myFont 설정 
    font directory
3. Loding,mainActivity

20-06-16
1. SQLite database 생성
2. asset폴더 생성 후 외부 db로부터 데이터를 받아 ArrayList에 저장
3. 리스트뷰 생성
4. getidentifier를 통해 이미지 리소스 저장

Learning Point
- Log.d로 print의 기능 대체 - tag를 필터링하여 확인
- 외부DB 사용법
Todo List
- 리스트뷰에 아이템리스너 생성
- DetailView에서 ACTION_DIAL 인텐트 넘겨주기
- 리스트뷰에서 아이템 클릭 시 상세 정보를 보여주는 뷰 만들기
- DB에서 전체 데이터를 가져와 새로운 store 배열 생성

20-07-17
1. 지도 버튼 추가 - (googleAPI 사용)
2. DB에서 전체 데이터를 가져와 storeArr에 저장    -> 코드 간소화
3. Toolbar를 이용한 BackButton 생성
4. 데이터베이스 영업시간, 메뉴 추가
5. 리스트뷰에 아이템 리스너 생성
6. 리스트뷰 아이템 클릭 시 상세 화면 생성
7. search 기능 구현

Learning Point
- 외부 DB를 asset에 담아 사용 시 갱신할 때 db파일명을 같게 수정하면 갱신 불가
- 리스트뷰의 레이아웃에 버튼이 들어가면 클릭 불가능 
  포커스를 없애기 - kotlin setFocusable(false)
-  *java.lang.RuntimeException: Parcel: unable to marshal value  -> 직렬화 처리 
    사용자가 만든 객체를 bundle로 보낼 때 발생
    객체 직렬화 : 객체 단위로 스트림을 통해서 전송하는 것
    import java.io.Serializable;
    open class name (
    ):Serializable{}	- kotlin에서 해결
TodoList
- search에서 Toast가 else에서도 뜨는 현상 수정
- callIcon을 누르면 Action_dial을 가진 인텐트 전달
Reuse
- DB에 영업시간과 메뉴, 가격 컬럼 추가

20-06-18
1. 가게 전화 기능 추가
TodoList
- search에서 Toast가 else에서도 뜨는 현상 수정
Reuse
- 다이얼로그 형식의 상세 화면을 새로운 액티비티로 전환되도록 수정
