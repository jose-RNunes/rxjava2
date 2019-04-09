package source

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import model.Film
import model.People
import model.PeopleFilm
import util.getLastBitFromUrl

class StarWarsRepositoryImp constructor(private val apiService: ApiService):StarWarsRepository{

    private val map = mutableMapOf<String,People>()

    override fun getPeople(idPeople:Int): Observable<People> {//Exemplo de Observable
        return apiService.getPeople(idPeople).
            subscribeOn(Schedulers.io()).
            observeOn(Schedulers.io())
    }

    override fun getPeoples(): Observable<List<People>>{//Exemplo de Map Operator
        return apiService.getPeoples().map {
            it.results
        }
    }

    override fun getPeoplesMale():Observable<List<People>>{//Exemplo FlatMapIterable e Filter
        return apiService.getPeoples().map {
            it.results
        }.flatMapIterable {
            list -> list
        }.filter {
            it.gender == "male"
        }.toList().toObservable()
    }

    override fun getPeopleFilms():Observable<List<PeopleFilm>>{//Exemplo Tak e Zip
        return apiService.getPeoples().map {peoples->
            peoples.results
        }.flatMapIterable {
            list -> list
        }.take(3).flatMap{people->
             Observable.zip(
                Observable.just(people),
                Observable.fromIterable(people.films).flatMap {urlFilm->
                    return@flatMap apiService.getPeopleMovie(urlFilm.getLastBitFromUrl().toInt())
                }.map {
                       film-> film.title
                }.toList().toObservable(), BiFunction<People,List<String>,PeopleFilm> { actor, films ->
                    return@BiFunction PeopleFilm(actor.name,films)
                })
        }.toList().toObservable()
    }

    override fun getFilmsPeople():Observable<People>{//Exemplo de FlatMap
        return apiService.getMovies().flatMap {films->
            Observable.
                fromIterable(films.results).doOnNext {film ->
                    System.out.println("Return Film: ${film.title}")
                }
                .flatMapIterable {film ->
                    film.characters
                }.flatMap {character ->
                    System.out.println("Return Url Character : $character")
                    apiService.getPeople(character.getLastBitFromUrl().toInt())
                }.flatMap {
                    cachePeople(it)
                }
        }
    }

    override fun cachePeople(people: People):Observable<People> {
        return Observable.create { emitter ->
            try{
                map[people.name] = people
                emitter.onNext(people)
                emitter.onComplete()

            }catch (e:Exception){
                emitter.onError(e)
            }
        }
    }

}