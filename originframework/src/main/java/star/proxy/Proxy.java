package star.proxy;

/**
 * @author keshawn
 * @date 2017/12/21
 */
public interface Proxy {

    Object doProxy(ProxyChain proxyChain) throws Throwable;
}
