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

    lateinit var starWarsRepository: StarWarsRepository

    @Before
    fun setUp(){
        MockitoAnnotations.initMocks(this)
        starWarsRepository = StarWarsRepositoryImp(apiService)
    }

    @Test
    fun testGetPeople(){
        Mockito.`when`(apiService.getPeople(1)).thenReturn(Observable.just(people))

        val testObserver = starWarsRepository.getPeople(1).test()

        testObserver.assertValueCount(1)
        testObserver.assertNoErrors()
        testObserver.assertComplete()
        assertEquals(people,testObserver.values()[0])
    }

    @Test
    fun testGetPeoples(){
        Mockito.`when`(apiService.getPeoples()).thenReturn(Observable.just(mockPeoples()))
        val testObserver = starWarsRepository.getPeoples().test()
        testObserver.assertNoErrors()
        testObserver.assertValue {
            list-> list[1].name == "Darth Vader"
        }
        testObserver.assertComplete()
    }

    @Test
    fun testGetPeopleMale(){
        Mockito.`when`(apiService.getPeoples()).thenReturn(Observable.just(mockPeoples()))

        val testObserver = starWarsRepository.getPeoplesMale().test()
        testObserver.assertNoErrors()
        testObserver.assertValue {
            list -> list.size == 3
        }
    }

    @Test
    fun testGetPeopleFilms(){
        val mockFilm = Mockito.mock(Film::class.java)
        Mockito.`when`(mockFilm.title).thenReturn("The Empire Strikes Back")
        Mockito.`when`(apiService.getPeoples()).thenReturn(Observable.just(mockPeoples()))
        Mockito.`when`(apiService.getPeopleMovie(ArgumentMatchers.anyInt()))
            .thenReturn(Observable.just(mockFilm))
        val testObserver = starWarsRepository.getPeopleFilms().test()
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
        testObserver.assertValue {
            list-> list.size == 3
        }
        Mockito.verify(apiService,Mockito.times(2)).getPeopleMovie(ArgumentMatchers.anyInt())

    }

    @Test
    fun testGetFilmsPeople(){
        Mockito.`when`(apiService.getMovies()).thenReturn(Observable.just(Films()))

    }


   private fun mockPeoples() =
        Peoples(0,"","",results =
        ArrayList<People>(Arrays.asList(
            People(name = "Luke Skywalker",
                   gender = "male",
                   films = Arrays.asList("https://swapi.co/api/films/2/",
                                         "https://swapi.co/api/films/6/")),
            People(name = "Darth Vader",gender = "male"),
            People(name = "Leia Organa",gender = "female"),
            People(name = "Owen Lars",gender = "male"),
            People(name = "Beru Whitesun lars",gender = "female"),
            People(name = "C-3PO",gender = "n/a"),
            People(name = "R2-D2",gender = "n/a")
        )))


}