package cn.chuangblog.rxsimple;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick1(View v) {
        String[] names = new String[]{"a", "b", "c", "d"};
        List<String> names1 = Arrays.asList(names);
        Observable.from(names1)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String name) {
                        Log.e(TAG, "Item: " + name);
                    }
                });
    }

    public void onClick2(View v) {
        final int drawableRes = R.drawable.ic_launcher;
        final ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Observable.create(new Observable.OnSubscribe<Drawable>() {
            @Override
            public void call(Subscriber<? super Drawable> subscriber) {
                Drawable drawable = getTheme().getDrawable(drawableRes);
                subscriber.onNext(drawable);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Observer<Drawable>() {
                    @Override
                    public void onNext(Drawable drawable) {
                        imageView.setImageDrawable(drawable);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });
    }


    public void Scheduler(View v) {
        Observable.just(1, 2, 3, 4)
                .subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer number) {
                        Log.e(TAG, "Item: " + number);
                    }
                });
    }


    public void student(View v) {
        Student[] students = new Student[]{new Student("zhangsan"), new Student("lisi")};
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String name) {
                Log.e(TAG, "Item: " + name);
            }
        };
        Observable.from(students)
                .map(new Func1<Student, String>() {
                    @Override
                    public String call(Student student) {
                        return student.getName();
                    }
                })
                .subscribe(subscriber);
    }

    public void flatMap(View v) {
        Course course1 = new Course(1);
        Course course2 = new Course(2);
        Course course3 = new Course(3);
        Student student = new Student("zhangsan");
        List<Course> courses = new ArrayList<>();
        courses.add(course1);
        courses.add(course2);
        courses.add(course3);
        student.setCourse(courses);
        Student[] students = new Student[]{student, new Student("lisi")};
        Subscriber<Course> subscriber = new Subscriber<Course>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Course course) {
                Log.e(TAG, "Item: " + course.getId());
            }
        };
        Observable.from(students)
                .flatMap(new Func1<Student, Observable<Course>>() {
                    @Override
                    public Observable<Course> call(Student student) {
                        return Observable.from(student.getCourse());
                    }
                })
                .subscribe(subscriber);
    }

    Observer<String> observer = new Observer<String>() {
        @Override
        public void onNext(String s) {
            Log.e(TAG, "Item: " + s);
        }

        @Override
        public void onCompleted() {
            Log.d(TAG, "Completed!");
        }

        @Override
        public void onError(Throwable e) {
            Log.d(TAG, "Error!");
        }
    };

    Subscriber<String> subscriber = new Subscriber<String>() {
        @Override
        public void onNext(String s) {
            Log.e(TAG, "Item: " + s);
        }

        @Override
        public void onCompleted() {
            Log.e(TAG, "Completed!");
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "Error!");
        }
    };
}
