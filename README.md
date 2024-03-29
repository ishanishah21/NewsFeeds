## An Application which demonstrate how MVVM can be used with Android Components
<a href="https://ibb.co/BL1TCL6"><img src="https://i.ibb.co/WnTB2nz/mvv-room.png" alt="mvv-room" border="0"></a>
1. Room Database For Caching
2. Repository With RxJava to Decide Datasource [Fetch from database or fetch from server]
3. ViewModel which process the data received from Repository
4. View with fragment which reflect processed data stream from viewmodel to update UI.
5. MutuableLiveData & Live Data For Data Streams to transmit data with Observer Design Pattern.
6. RxJava for reactive network calls and database operations.
7. DAO classes are made to handle SQL queries.
8. Navigation Component has been used for managing the Navigation of the Fragments/Screens.

## Libraries
1. Retrofit For Networking
2. Rxjava
3. Jetpack [Viewmodel, Livedata, Room Db]
4. Glide for image loading
5. Roboelectric for Unit testing.
