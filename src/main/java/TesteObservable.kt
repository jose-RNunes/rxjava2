
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.internal.operators.observable.ObservableBlockingSubscribe.subscribe
import org.reactivestreams.Subscriber
import source.ApiService
import source.RetrofitConfig
import source.StarWarsRepositoryImp
import javax.swing.text.View
import io.reactivex.observers.DisposableObserver




class TesteObservable {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val apiService = RetrofitConfig.getApiService()
            getPeoples(apiService)
        }

        fun cachePeople(apiService:ApiService){
            val repository = StarWarsRepositoryImp(apiService)
            repository.getPeople(1).flatMap {people ->
                repository.cachePeople(people)
            }.subscribe(getDisposableObserver())
        }

        fun getFilmsPeople(apiService: ApiService){
            val repository = StarWarsRepositoryImp(apiService)
            repository.getFilmsPeople().subscribe(getDisposableObserver())
        }

        fun getPeopleFilms(apiService: ApiService){
            val repository = StarWarsRepositoryImp(apiService)
            repository.getPeopleFilms().subscribe(getDisposableObserver())
        }

        fun getPeopleMale(apiService: ApiService){
            val repository = StarWarsRepositoryImp(apiService)
            repository.getPeoplesMale().subscribe(getDisposableObserver())
        }

        fun getPeople(apiService:ApiService){
            val repository = StarWarsRepositoryImp(apiService)
            repository.getPeople(1).subscribe(getDisposableObserver())
        }

        fun getPeoples(apiService: ApiService){
            val repository = StarWarsRepositoryImp(apiService)
            repository.getPeoples().subscribe(getDisposableObserver())
        }

        private fun <T>getDisposableObserver(): Observer<T> {
            return object : Observer<T> {
                override fun onSubscribe(d: Disposable) {
                    System.out.println("On Subscribe")
                }

                override fun onComplete() {
                    System.out.println("On Complete")
                }

                override fun onError(e: Throwable) {
                    System.out.println("Error: ${e.message}")
                }

                override fun onNext(sucess: T) {
                    System.out.println("Return Sucess: $sucess")
                }
            }
        }
    }





}