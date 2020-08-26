package DesignPattern.CodeDesign.auth;

import DesignPattern.CodeDesign.auth.request.ApiRequest;
//王争:确实没必要搞个接口，ApiRequest的设计也依赖了具体的url实现，所以也不是很通用。
public interface IApiAuthenticator {

    void auth(String url);

    void auth(ApiRequest apiRequest);

}
