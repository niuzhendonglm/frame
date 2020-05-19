package com.niuzhendong.frame;

import org.apache.commons.collections.MapUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

//jdk8新概念 函数式接口 设计思想 单一责任制
@FunctionalInterface
interface Interface1 {
    //默认方法
    int doubleNum(int i);
    //默认实现方法
    default int add(int i,int j) {
        return i+j;
    }
}

@FunctionalInterface
interface Interface2 {
    //默认方法
    int doubleNum(int i);

    //默认实现方法
    default int add(int i,int j) {
        this.doubleNum(i);
        return i+j;
    }
}

@FunctionalInterface
interface Interface3 extends Interface1,Interface2 {
    @Override
    default int add(int i, int j) {
        return Interface2.super.add(i,j);
    }
}

@FunctionalInterface
interface IMoneyFormat {
    String format(int i);
}

class MyMoney {
    private final int money;
    public MyMoney(int money) {
        this.money = money;
    }
//    public void printMoney(IMoneyFormat iMoneyFormat) {
//        System.out.println("我的存款："+iMoneyFormat.format(money));
//    }
    public void printMoney(Function<Integer,String> iMoneyFormat) {
        System.out.println("我的存款："+iMoneyFormat.apply(money));
    }
}

@RunWith(SpringRunner.class)
//@SpringBootTest
public class FrameApplicationTests {

    @Test
    public void contextLoads() {
        int[] arr = new int[]{-666,1,3,5,7,9};

        //jdk8
        System.out.println(IntStream.of(arr).min().getAsInt());
        System.out.println(IntStream.of(arr).parallel().min().getAsInt());

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("ok");
            }
        }).start();

        //jdk8 lambda 箭头函数
        new Thread(()-> System.out.println("ok")).start();

        //接口实现
        Interface1 i1 = (i) -> i*2;
        Interface1 i2 = i -> i*2;
        Interface1 i3 = (int i) -> i*2;
        Interface1 i4 = (int i) -> {
            System.out.println("ok");
            return i*2;
        };

        System.out.println(i1.doubleNum(20));
        System.out.println(i1.add(10,20));
    }

    @Test
    public void testMoneyFormat() {
        MyMoney myMoney = new MyMoney(99999999);
//        myMoney.printMoney(i->new DecimalFormat("#,###").format(i));
        //函数接口链式操作
        Function<Integer,String> moneyFormat = i -> new DecimalFormat("#,###").format(i);
        myMoney.printMoney(moneyFormat.andThen(s->"人民币："+s));
    }

    /**
     * 方法引用
     */
    @Test
    public void testFunction() {
        Predicate<Integer> predicate = i->i>0;
        System.out.println(predicate.test(-9));
        IntPredicate predicate1 = i->i<0;
        System.out.println(predicate1.test(-9));

        //IntConsumer
        Consumer<String> consumer = i-> System.out.println(i);
        consumer.accept("你好");
    }

    /**
     * 方法引用
     */
    @Test
    public void testMethodReference() {
        Consumer<String> consumer = s-> System.out.println(s);
        consumer.accept("国庆节");

        Consumer<String> consumer1 = System.out::println;
        consumer1.accept("10:1");

        Consumer<Dog> consumer2 = Dog::bark;
        Dog dog = new Dog();
        consumer2.accept(dog);

        dog.eat(1);

        //Function<Integer,Integer> eatFunction = dog::eat;
        //UnaryOperator<Integer> eatFunction = dog::eat;
        IntUnaryOperator eatFunction = dog::eat;
        System.out.println("还剩下"+eatFunction.applyAsInt(2)+"斤");

        BiFunction<Dog,Integer,Integer> eatBiFunction = Dog::eat;
        System.out.println("还剩下"+eatBiFunction.apply(dog,3)+"斤");

        Supplier eatSupplier = Dog::new;
        System.out.println("创建了"+eatSupplier.get());

        Function<String,Dog> eatFunction1 = Dog::new;
        System.out.println("创建了"+eatFunction1.apply("二郎神"));
    }

    /**
     * 类型推断
     */
    @Test
    public void testTypeInfer() {
        IMath iMath = (x,y) -> x+y;
        System.out.println(iMath.add(2,3));
        IMath[] iMath1 = {(x,y)->x+y};
        Object iMath2 = (IMath)(x,y)->x+y;
        IMath iMath3 = createIMath();
        FrameApplicationTests frameApplicationTests = new FrameApplicationTests();
        frameApplicationTests.test((IMath) (x,y)->x+y);
    }

    /**
     * 变量引用
     */
    @Test
    public void testVariableRefer() {
        String ss = "国庆节";
//        ss=""; //JDK8之后 final String ss = "国庆节";
        Consumer<String> consumer = s-> System.out.println(s+ss);
        consumer.accept("10:1 ");
    }



    public void test(IMath iMath) {
    }

    public void test(IMath2 iMath) {
    }

    public static IMath createIMath() {
        return (x,y)->x+y;
    }

    /**
     * 级联表达式和柯里化
     */
    @Test
    public void testCurry() {
        Function<Integer,Function<Integer,Integer>> fun = x->y->x+y;
        System.out.println(fun.apply(2).apply(3));

        Function<Integer,Function<Integer,Function<Integer,Integer>>> fun2 = x->y->z->x+y+z;
        System.out.println(fun2.apply(2).apply(3).apply(4));

        int[] arr= {3,4,5};
        Function f = fun2;
        for (int i : arr) {
            if(f instanceof Function) {
                Object apply = f.apply(i);
                if(apply instanceof Function) {
                    f = (Function) apply;
                }else{
                    System.out.println("调用结束："+apply);
                }
            }
        }
    }

    /**
     * 1、外部迭代和内部迭代
     * 2、map中间操作
     * 3、sum终止操作
     * 4、惰性求值就是终止没有调用的情况下，中间操作不会执行
     */
    @Test
    public void testStream() {
        //外部迭代
        int[] nums = {1,3,5};
        int sum = 0;
        for (int num : nums) {
            sum+=num;
        }
        System.out.println(sum);

        //内部迭代
//        int sum1 = IntStream.of(nums).map(i->i*2).sum();
        int sum1 = IntStream.of(nums).map(FrameApplicationTests::doubleNum).sum();
        System.out.println(sum1);

        System.out.println("你好："+IntStream.of(nums).map(FrameApplicationTests::doubleNum));
    }
    public static int doubleNum(int i) {
        return i*3;
    }

    /**
     * 创建流的几种方式
     */
    @Test
    public void testCreateStream() {
        //集合
        List<Integer> list = new ArrayList<>();
        list.stream();
        list.parallelStream();

        //数组
        Arrays.stream(new int[]{1,2,3});

        //数字流
        IntStream.of(1,2,3);
        IntStream.rangeClosed(1,10);

        //使用random创建一个无限流
        new Random().ints().limit(10);

        //自己产生流
        Random random = new Random();
        Stream.generate(()->random.nextInt()).limit(20);
    }

    /**
     * Stream流编程——中间操作
     * 1、无状态操作
     *  · map/mapToXxx
     *  · flatMap/flatMapToXxx（两重场景）
     *  · filter（过滤）
     *  · peek（跟forEach很像，但是forEach是终止操作）
     *  · unordered（在并行流的时候用到）
     * 2、有状态操作
     *  · distinct（去重）
     *  · sorted（排序）
     *  · limit（限流）/skip（跳过）
     */
    @Test
    public void testStreamProgram1() {
        String str = "my name is 007";
        Stream.of(str.split(" ")).filter(s->s.length()>2).map(s->s.length()).forEach(System.out::println);

        //intStream/longStream 并不是Stream的子类，所有要进行装箱boxed
//        Stream.of(str.split(" ")).flatMap(s->s.chars().boxed()).forEach(System.out::println);
        Stream.of(str.split(" ")).flatMap(s->s.chars().boxed()).forEach(s-> System.out.println((char)s.intValue()));

        //peek 用于debug
        Stream.of(str.split(" ")).peek(System.out::println).forEach(System.out::println);

        new Random().ints().filter(i->i>1000&&i<10000).limit(10).forEach(System.out::println);
    }

    /**
     * Stream流编程——终止操作
     * 1、非短路操作
     *  · forEach/forEachOrdered
     *  · collect（收集器）/toArray
     *  · reduce（合并）
     *  · min/max/count
     * 2、短路操作
     *  · findFirst/findAny
     *  · allMatch/anyMatch/noneMatch
     */
    @Test
    public void testStreamProgram2() {
        String str = "my name is 007";
        str.chars().parallel().forEach(i-> System.out.print((char)i));
        System.out.println();
        str.chars().parallel().forEachOrdered(i-> System.out.print((char)i));
        System.out.println();

        List<String> list = Stream.of(str.split(" ")).collect(Collectors.toList());
        System.out.println(list);

        Optional<String> reduce = Stream.of(str.split(" ")).reduce((s1, s2) -> s1 + "|" + s2);
        System.out.println(reduce.orElse(""));
        String reduce1 = Stream.of(str.split(" ")).reduce("", (s1, s2) -> s1 + "|" + s2);
        System.out.println(reduce1);
        Integer reduce2 = Stream.of(str.split(" ")).map(s -> s.length()).reduce(0, (s1, s2) -> s1 + s2);
        System.out.println(reduce2);

        Optional<String> max = Stream.of(str.split(" ")).max((s1, s2) -> s1.length() - s2.length());
        System.out.println(max.get());

        OptionalInt first = new Random().ints().findFirst();
        System.out.println(first.getAsInt());
    }


    /**
     * 并行流
     */
    @Test
    public void testParallel() {
//        IntStream.range(1,100).peek(FrameApplicationTests::debug).count();
//        IntStream.range(1,100).parallel().peek(FrameApplicationTests::debug).count();

        //多次调用parallel（并行流）/sequentialFor（串行流），以最后一次调用为准
//        IntStream.range(1,100)
//                .parallel().peek(FrameApplicationTests::debug)
//                .sequential().peek(FrameApplicationTests::debug2)
//                .count();

        //修改默认的线程数
//        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism","20");
//        IntStream.range(1,100).parallel().peek(FrameApplicationTests::debug).count();

        //使用自己的线程池，防止任务被阻塞
        ForkJoinPool pool = new ForkJoinPool(20);
        pool.submit(()->IntStream.range(1,100).parallel().peek(FrameApplicationTests::debug).count());
        pool.shutdown();
        synchronized (pool) {
            try {
                pool.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
    public static void debug(int i) {
        System.out.println(Thread.currentThread().getName()+" debug "+i);
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void debug2(int i) {
        System.err.println("debug2 "+i);
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * collect收集器
     */
    @Test
    public void testCollect() {
        // 测试数据
        List<Student> students = Arrays.asList(
                new Student("小明", 10, Gender.MALE, Grade.ONE),
                new Student("大明", 9, Gender.MALE, Grade.THREE),
                new Student("小白", 8, Gender.FEMALE, Grade.TWO),
                new Student("小黑", 13, Gender.FEMALE, Grade.FOUR),
                new Student("小红", 7, Gender.FEMALE, Grade.THREE),
                new Student("小黄", 13, Gender.MALE, Grade.ONE),
                new Student("小青", 13, Gender.FEMALE, Grade.THREE),
                new Student("小紫", 9, Gender.FEMALE, Grade.TWO),
                new Student("小王", 6, Gender.MALE, Grade.ONE),
                new Student("小李", 6, Gender.MALE, Grade.ONE),
                new Student("小马", 14, Gender.FEMALE, Grade.FOUR),
                new Student("小刘", 13, Gender.MALE, Grade.FOUR));

//        List<Integer> collect = students.stream().map(Student::getAge).collect(Collectors.toList());
//        Set<Integer> collect = students.stream().map(Student::getAge).collect(Collectors.toSet());
        TreeSet<Integer> collect = students.stream().map(Student::getAge).collect(Collectors.toCollection(TreeSet::new));
        System.out.println(collect);

        IntSummaryStatistics collect1 = students.stream().collect(Collectors.summarizingInt(Student::getAge));
        System.out.println("年龄汇总信息："+collect1);

        Map<Boolean, List<Student>> collect2 = students.stream().collect(Collectors.partitioningBy(s -> s.getGender() == Gender.MALE));
//        System.out.println("男女学生列表："+collect2);
        MapUtils.verbosePrint(System.out,"男女学生列表：",collect2);

        Map<Grade, List<Student>> collect3 = students.stream().collect(Collectors.groupingBy(Student::getGrade));
        MapUtils.verbosePrint(System.out,"学生班级列表：",collect3);

        Map<Grade, Long> collect4 = students.stream().collect(Collectors.groupingBy(Student::getGrade, Collectors.counting()));
        MapUtils.verbosePrint(System.out,"班级学生个数列表：",collect4);
    }


    /**
     * Stream运行机制
     * 1、所有操作是链式调用，一个元素只迭代一次
     * 2、每一个中间操作返回一个新的流，流里面有一个属性sourceStage，指向同一个地方，就是Head
     * 3、Head->nextStage->nextStage->...->null
     * 4、有状态操作会把无状态操作截断，单独处理
     * 5、并行环境下，有状态的中间操作不一定能并行操作
     * 6、parallel/sequential 这两个操作也是中间操作（也是返回stream），但是他们不创建流，他们只修改HEAD的并行标志
     */
    @Test
    public void testStreamRunMechanism() {
        Random random = new Random();
        // 随机产生数据
        Stream<Integer> stream = Stream.generate(() -> random.nextInt())
                // 产生500个 ( 无限流需要短路操作. )
                .limit(500)
                // 第1个无状态操作
                .peek(s -> print("peek: " + s))
                // 第2个无状态操作
                .filter(s -> {
                    print("filter: " + s);
                    return s > 1000000;
                })
                // 有状态操作
                .sorted((i1, i2) -> {
                    print("排序: " + i1 + ", " + i2);
                    return i1.compareTo(i2);
                })
                // 又一个无状态操作
                .peek(s -> {
                    print("peek2: " + s);
                }).parallel();

        // 终止操作
        stream.count();
    }
    public static void print(String s) {
        System.out.println(Thread.currentThread().getName()+">"+s);
        try {
            TimeUnit.MILLISECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

/**
 * 学生 对象
 */
class Student {
    /**
     * 姓名
     */
    private String name;

    /**
     * 年龄
     */
    private int age;

    /**
     * 性别
     */
    private Gender gender;

    /**
     * 班级
     */
    private Grade grade;

    public Student(String name, int age, Gender gender, Grade grade) {
        super();
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "[name=" + name + ", age=" + age + ", gender=" + gender
                + ", grade=" + grade + "]";
    }

}

/**
 * 性别
 */
enum Gender {
    MALE, FEMALE
}

/**
 * 班级
 */
enum Grade {
    ONE, TWO, THREE, FOUR;
}

class Dog {
    private String name = "哮天犬";
    private int food = 10;
    public Dog() {
    }
    public Dog(String name) {
        this.name = name;
    }
    public static void bark(Dog dog) {
        System.out.println(dog+"叫了");
    }
    public int eat(int count) {
        System.out.println("吃了"+count+"斤");
        this.food-=count;
        return this.food;
    }
    @Override
    public String toString() {
        return this.name;
    }
}

@FunctionalInterface
interface IMath {
    int add(int i,int j);
}

@FunctionalInterface
interface IMath2 {
    int add(int i,int j);
}
