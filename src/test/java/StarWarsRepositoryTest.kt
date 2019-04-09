import io.reactivex.Observable
import model.People
import model.Peoples
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
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
    fun getGetPeoples(){
        Mockito.`when`(apiService.getPeoples()).thenReturn(Observable.just(mockPeoples()))

        val testObserver = starWarsRepository.getPeoples().test()

        testObserver.assertNoErrors()
        testObserver.assertValue {
            list-> list[1].name == "Darth Vader"
        }
        testObserver.assertComplete()
    }


    fun mockPeoples() =
        Peoples(0,"","",results =
        ArrayList<People>(Arrays.asList(
            People(name = "Luke Skywalker"),
            People(name = "Darth Vader"),
            People(name = "Leia Organa")
        )))

}