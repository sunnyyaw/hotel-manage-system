package com.example.dish;

import com.example.dish.services.BillService;
import com.example.dish.services.DishService;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.profile.StackProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 5,time = 1)
@Measurement(iterations = 5,time = 1)
@Fork(1)
@State(Scope.Benchmark)
public class JMHTests {
    private ConfigurableApplicationContext context;
    private DishService dishService;
    public static void main(String[] args)throws Exception {
        Options opt = new OptionsBuilder().include(JMHTests.class.getSimpleName())
                .addProfiler(StackProfiler.class).build();
        new Runner(opt).run();
    }
    @Setup
    public void init() {
        context = SpringApplication.run(DishApplication.class);
        dishService = context.getBean(DishService.class);
    }
    @TearDown
    public void down() {
        context.close();
    }
    @Benchmark
    public void testService(){
        dishService.getAllDishDetails();
    }
}
