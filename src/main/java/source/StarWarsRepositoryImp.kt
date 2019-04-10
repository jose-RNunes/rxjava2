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

    /**
     * Exemplo de um Observable que trabalha na Thread de io.
    * @param idPeople passado é utilizado para trazer somente 1 personagem
     * @return retorna o personagem solicitado
    * */
    override fun getPeople(idPeople:Int): Observable<People> {
        return apiService.getPeople(idPeople).
            subscribeOn(Schedulers.io()).
            observeOn(Schedulers.io())
    }

    /**
     * Exemplo utilizando o operador Map, que é utilizado para transformar objetos
     * Nesse caso ele transforma a classe Peoples que contém um atributo chamado results que retorna a
     * lista de personagem da pagina indicada.
     * @return retorna uma lista com os personagens
     * */
    override fun getPeoples(): Observable<List<People>>{//Exemplo de Map Operator
        return apiService.getPeoples().map {
            it.results
        }
    }

    /**
     * Exemplo utilizando o operador FlatMapIterable, Filter e o toList
     * O operador FlatMapIterable é utlizado para iterar em uma lista
     * O filter foi utlizado para filtrar  objeto gerado pela a lista trazendo somente os personagens de sexo Masculino
     * O toList transforma todos os objetos iterados em uma lista
     * @return retorna uma lista com os personagens
     * */
    override fun getPeoplesMale():Observable<List<People>>{
        return apiService.getPeoples().map {
            it.results
        }.flatMapIterable {
            list -> list
        }.filter {
            it.gender == "male"
        }.toList().toObservable()
    }

    /**
     * Exemplo utilizando o operador FlatMapIterable, Take,FlatMap, Zip, Just, FromIterable,Map e o toList
     * O operador FlatMapIterable é utlizado para iterar em uma lista
     * O Take foi utilizado para trazer somente os 3 primeiros elementos da lista
     * O FlatMap é utlizado para transformar a stream em outra stream
     * O Zip é utlizado para transfor um conjunto de streams e um nova stream
     * O toList transforma todos os objetos iterados em uma lista
     * O zip foi utilizado para criar um novo objeto o PeopleFilm que contém um personagem e a lista de filmes que ele participou.
     * @return retorna uma lista com os personagens com seus filmes
     * */
    override fun getPeopleFilms():Observable<List<PeopleFilm>>{//Exemplo Take e Zip
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
                }.toList()
                 .toObservable(), BiFunction<People,List<String>,PeopleFilm> { actor, films ->
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
                    apiService.getPeople(character.getLastBitFromUrl().toInt())
                }.flatMap {
                    cachePeople(it)
                }
        }
    }

    /**
     * Exemplo de criação de um Observable que pode ser utlizado para fazer um cache     *
     * @return retorna uma lista com os personagens com seus filmes
     * */
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