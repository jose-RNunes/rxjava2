import io.reactivex.BackpressureStrategy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class Flowable {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {

            val observable = PublishSubject.create<Int>()
            observable
                .toFlowable(BackpressureStrategy.LATEST).observeOn(Schedulers.computation())
                .subscribe({ onNext ->
                    println("Number: $onNext")
                }, { onError ->
                    print(onError.message)
                },
                    {
                        println("Completed")
                    }
                )
            for (i in 0..1000000) {
                observable.onNext(i)
            }

        }
    }

}