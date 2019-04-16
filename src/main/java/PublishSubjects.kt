import io.reactivex.subjects.AsyncSubject
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.ReplaySubject

class PublishSubjects {


    companion object {
        @JvmStatic
        fun main(args: Array<String>) {

            val subject = PublishSubject.create<Int>()

            subject.onNext(2)

            subject.onNext(1)


            subject.subscribe{
                System.out.println("Next: $it")
            }

            subject.onNext(3)

            subject.onNext( 5)

            subject.onNext(8)

            subject.onNext(9)

            subject.onNext(2)

        }
    }
}