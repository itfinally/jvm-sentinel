package io.github.itfinally.jvm.requests;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

public interface HttpSecurityFactory {

  SSLSocketFactory getSSLSocketFactory();

  X509TrustManager getTrustManager();
}
