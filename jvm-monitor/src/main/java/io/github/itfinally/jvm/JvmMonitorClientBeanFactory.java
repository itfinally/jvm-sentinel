package io.github.itfinally.jvm;

import io.github.itfinally.jvm.JvmMonitorProperties;
import io.github.itfinally.jvm.requests.HttpSecurityFactory;
import io.github.itfinally.jvm.requests.VManagerClient;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Strings.isNullOrEmpty;

@Configuration
public class JvmMonitorClientBeanFactory {

  @Bean
  public Retrofit retrofitClient( JvmMonitorProperties properties ) {
    HttpSecurityFactory httpSecurityFactory = properties.getHttpSecurityFactory();

    OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
        .connectTimeout( 5, TimeUnit.SECONDS )
        .readTimeout( 5, TimeUnit.SECONDS );

    if ( isNullOrEmpty( properties.getCentralHost() ) ) {
      throw new NullPointerException( "Require central host name" );
    }

    if ( properties.getCentralHost().startsWith( "https" ) ) {
      if ( null == httpSecurityFactory ) {
        throw new NullPointerException( "Require https factory" );
      }

      okHttpClientBuilder.sslSocketFactory( httpSecurityFactory.getSSLSocketFactory(), httpSecurityFactory.getTrustManager() );
    }

    return new Retrofit.Builder()
        .client( okHttpClientBuilder.build() )
        .baseUrl( properties.getCentralHost() )
        .addConverterFactory( JacksonConverterFactory.create() )
        .build();
  }

  @Bean
  public VManagerClient vManagerClient( Retrofit retrofit ) {
    return retrofit.create( VManagerClient.class );
  }

  @Bean( "taskRetryScheduleWorker" )
  public ScheduledExecutorService taskRetryScheduleWorker() {
    return Executors.newScheduledThreadPool( 1, r -> {
      Thread thread = new Thread( r );
      thread.setName( "schedule-retry-task-worker" );

      return thread;
    } );
  }
}
