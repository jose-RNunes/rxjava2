import io.reactivex.Observable
import model.Film
import model.Films
import model.People
import model.Peoples
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import source.ApiService
import source.StarWarsRepository
import source.StarWarsRepositoryImp
import java.util.*
import kotlin.collections.ArrayList

class StarWarsRepositoryTest{

    @Rule
    @JvmField var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    lateinit var apiService: ApiService

    @Mock
    lateinit var people: People

    @Mock
    lateinit var peoples :Peoples

    @Mock
    lateinit var films: Films

    lateinit var starWarsRepository: StarWarsRepository

    @Before
    fun setUp(){
        MockitoAnnotations.initMocks(this)
        starWarsRepository = StarWarsRepositoryImp(apiService)
    }

    @Test
    fun shouldReturnPeopleWhenFilterAnyPeople(){
        //Given
        Mockito.`when`(apiService.getPeople(anyInt())).thenReturn(Observable.just(people))
        //When
        val testObserver = starWarsRepository.getPeople(anyInt()).test()
        //Then
        testObserver.assertValueCount(1)
        testObserver.assertNoErrors()
        testObserver.assertComplete()
        assertEquals(people,testObserver.values()[0])
    }

    @Test
    fun shouldReturnPeopleWhenGetFirstPagesPeoples(){
        //Given
        Mockito.`when`(peoples.results).thenReturn(ArrayList<People>(Arrays.asList(
            People(name = "Luke Skywalker"),
            People(name = "Darth Vader")
        )))
        Mockito.`when`(apiService.getPeoples()).thenReturn(Observable.just(peoples))
        //When
        val testObserver = starWarsRepository.getPeoples().test()
        //Then
        testObserver.assertNoErrors()
        testObserver.assertValue {
            list-> list[1].name == "Darth Vader"
        }
        testObserver.assertComplete()
    }

    @Test
    fun shouldReturnPeoplesMaleWhenGetFilterGenderMale(){
        //Given
        Mockito.`when`(peoples.results).thenReturn(ArrayList<People>(Arrays.asList(
            People(gender = "male"),
            People(gender = "female"),
            People(gender = "n/a"),
            People(gender = "male")
            )))
        Mockito.`when`(apiService.getPeoples()).thenReturn(Observable.just(peoples))
        //When
        val testObserver = starWarsRepository.getPeoplesMale().test()
        //Then
        testObserver.assertNoErrors()
        testObserver.assertValue {
            list -> list.size == 2
        }
    }

    @Test
    fun shouldReturnPeoplesWithMovieNamesWhenGetUrlFromMovies(){
        //Given
        Mockito.`when`(peoples.results).thenReturn(ArrayList<People>(Arrays.asList(
            People(name = "Luke Skywalker" ,
                 films = Arrays.asList("https://swapi.co/api/films/2/",
                                       "https://swapi.co/api/films/6/")),
            People(name = "Darth Vader"),
            People(name = "Leia Organa"),
            People(name = "Owen Lars")
        )))
        val mockFilm = Mockito.mock(Film::class.java)
        Mockito.`when`(mockFilm.title).thenReturn("The Empire Strikes Back")
        Mockito.`when`(apiService.getPeoples()).thenReturn(Observable.just(peoples))
        Mockito.`when`(apiService.getPeopleMovie(ArgumentMatchers.anyInt()))
            .thenReturn(Observable.just(mockFilm))
        //When
        val testObserver = starWarsRepository.getPeopleFilms().test()
        //Then
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
        testObserver.assertValue {
            list-> list.size == 3
        }
        Mockito.verify(apiService,Mockito.times(2)).getPeopleMovie(ArgumentMatchers.anyInt())

    }

    @Test
    fun shouldReturnPeoplesNamesWhenGetListFilms(){
        //Given
       Mockito.`when`(films.results).thenReturn(Arrays.asList(
           Film(title = "A New Hope",characters = Arrays.asList(
               "https://swapi.co/api/people/1/",
               "https://swapi.co/api/people/2/"
           ))
       ))
       Mockito.`when`(people.name).thenReturn("Luke Skywalker")
       Mockito.`when`(apiService.getMovies()).thenReturn(Observable.just(films))
       Mockito.`when`(apiService.getPeople(anyInt())).thenReturn(Observable.just(people))

       //When
       val testObserver = starWarsRepository.getFilmsPeople().test()

        //Then
       testObserver.assertNoErrors()
       testObserver.assertValueCount(2)
       testObserver.assertComplete()
    }

}