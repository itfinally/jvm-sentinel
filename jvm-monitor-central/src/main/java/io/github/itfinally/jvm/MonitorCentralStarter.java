package io.github.itfinally.jvm;

import io.github.itfinally.mybatis.jpa.JpaPrepareInterceptor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nonnull;
import javax.sql.DataSource;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Runtime.getRuntime;

@EnableAsync
@SpringBootApplication
@MapperScan( basePackages = "io.github.itfinally.jvm.repository.mapper" )
public class MonitorCentralStarter {
  public static void main( String[] args ) {
    new SpringApplication( MonitorCentralStarter.class ).run( args );
  }

  @Bean
  public SqlSessionFactory sqlSessionFactory( Configuration configuration, DataSource dataSource,
                                              JpaPrepareInterceptor jpaPrepareInterceptor ) throws Exception {

    SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
    factory.setConfiguration( configuration );
    factory.setDataSource( dataSource );

    configuration.addInterceptor( jpaPrepareInterceptor );

    return factory.getObject();
  }

  @Bean
  public TransactionTemplate transactionTemplate( PlatformTransactionManager transactionManager ) {
    TransactionTemplate transactionTemplate = new TransactionTemplate( transactionManager );
    transactionTemplate.setTimeout( 60 );

    return transactionTemplate;
  }

  @Bean( "taskElasticExecutorPool" )
  public ExecutorService taskElasticExecutorPool() {
    int availableCpuCore = getRuntime().availableProcessors() > 1 ? getRuntime().availableProcessors() - 1 : 1;

    return new ThreadPoolExecutor( 1, availableCpuCore, 3, TimeUnit.MINUTES,
        new LinkedBlockingDeque<>( 10240 ), new ThreadFactory() {

      private final AtomicInteger counter = new AtomicInteger( 0 );

      @Override
      public Thread newThread( @Nonnull Runnable r ) {
        Thread thread = new Thread( r );
        thread.setName( String.format( "async-task-executor-%d", counter.getAndIncrement() ) );

        return thread;
      }
    } );
  }
}
