
import model.People
import source.ApiService
import source.RetrofitConfig
import source.StarWarsRepositoryImp


class TesteObservable {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val apiService = RetrofitConfig.getApiService()


        }

        fun cachePeople(apiService:ApiService){
            val repository = StarWarsRepositoryImp(apiService)
            repository.getPeople(1).flatMap {people ->
                repository.cachePeople(people)
            }.subscribe({
                System.out.println("Return Sucess: $it")
            },{
                System.out.println("Error: ${it.message}")
            },{
                System.out.println("On Complete")
            })
        }

        fun getFilmsPeople(apiService: ApiService){
            val repository = StarWarsRepositoryImp(apiService)
            repository.getFilmsPeople().subscribe({
                System.out.println("Return Sucess: $it")
            },{
                System.out.println("Error: ${it.message}")
            },{
                System.out.println("On Complete")
            })
        }

        fun getPeopleFilms(apiService: ApiService){
            val repository = StarWarsRepositoryImp(apiService)
            repository.getPeopleFilms().subscribe({
                System.out.println("Sucess: $it")
            },{
                System.out.println("Error: ${it.message}")
            },{
                System.out.println("On Complete")
            })
        }

        fun getPeopleMale(apiService: ApiService){
            val repository = StarWarsRepositoryImp(apiService)
            repository.getPeoplesMale().subscribe({
                System.out.println("Sucess: $it")
            },{
                System.out.println("Error: ${it.message}")
            },{
                System.out.println("On Complete")
            })
        }

        fun getPeople(apiService:ApiService){
            val repository = StarWarsRepositoryImp(apiService)
            repository.getPeople(1).subscribe({
                System.out.println("Sucess: $it")
            },{
                System.out.println("Error: ${it.message}")
            },{
                System.out.println("On Complete")
            })
        }

        fun getPeoples(apiService: ApiService){
            val repository = StarWarsRepositoryImp(apiService)
            repository.getPeoples().subscribe({
                System.out.println("Sucess: $it")
            },{
                System.out.println("Error: ${it.message}")
            },{
                System.out.println("On Complete")
            })
        }


    }





}