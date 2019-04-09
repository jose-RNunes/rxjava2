# rxjava2
O Projeto utiliza como serviço externo a api The Star Wars API, https://swapi.co/documentation#schema
Libs utilizadas:
Retrofit - para tratar o serviço da Api,
RxJava - para fazer as chamadas de serviços da Api,
Mockito - para testes,
Kotlin.

Para os testes é utlizado a classe Rule(RxImmediateSchedulerRule), para transformar as Threads de io, computation e newThread em Trampolim assim enviando o problema de chamadas simultaneas em testes unitários.

Para utlizar em projetos Android adicione a lib:
    implementation "io.reactivex.rxjava2:rxandroid:$rxandroidVersion"
E quando for atualizar a Thread de ui, será utilizado o Schedulers:AndroidSchedulers.mainThread() exemplo:
 /**
     * Exemplo de um Observable que trabalha na Thread de io.
    * @param idPeople passado é utilizado para trazer somente 1 personagem
     * @return retorna o personagem solicitado
    * */
    override fun getPeople(idPeople:Int): Observable<People> {
        return apiService.getPeople(idPeople).
            subscribeOn(Schedulers.io()).
            observeOn(AndroidSchedulers.mainThread())
    }
  
  E para testes no Android a classe RxImmediateSchedulerRule deve ficar como abaixo:
  class RxImmediateSchedulerRule : TestRule {

    override fun apply(base: Statement, d: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
                RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
                RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
                RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

                try {
                    base.evaluate()
                } finally {
                    RxJavaPlugins.reset()
                    RxAndroidPlugins.reset()
                }
            }
        }
    }
    
    
    

