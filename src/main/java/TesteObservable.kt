
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import source.RetrofitConfig
import source.StarWarsRepository
import source.StarWarsRepositoryImp
import java.util.stream.Collectors
import java.util.stream.IntStream




class TesteObservable {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val apiService = RetrofitConfig.getApiService()
            val repository = StarWarsRepositoryImp(apiService)


            getFilmsPeople(repository)


        }

        fun cachePeople(repository:StarWarsRepository){
            repository.getPeople(1).flatMap {people ->
                repository.cachePeople(people)
            }.subscribe(getDisposableObserver())
        }

        fun getFilmsPeople(repository:StarWarsRepository){
            repository.getFilmsPeople().subscribe(getDisposableObserver())
        }

        fun getPeopleFilms(repository:StarWarsRepository){
            repository.getPeopleFilms().subscribe(getDisposableObserver())
        }

        fun getPeopleMale(repository:StarWarsRepository){
            repository.getPeoplesMale().subscribe(getDisposableObserver())
        }

        fun getPeople(repository:StarWarsRepository){
            repository.getPeople(1).subscribe(getDisposableObserver())
        }

        fun getPeoples(repository:StarWarsRepository){
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