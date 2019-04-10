package source

import io.reactivex.Observable
import model.People
import model.PeopleFilm

interface StarWarsRepository{

    fun getPeople(idPeople:Int): Observable<People>

    fun getPeoples(): Observable<List<People>>

    fun getPeoplesMale():Observable<List<People>>

    fun getPeopleFilms():Observable<List<PeopleFilm>>

    fun getFilmsPeople():Observable<String>

    fun cachePeople(people: People):Observable<People>
}
