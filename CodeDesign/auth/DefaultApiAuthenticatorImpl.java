package DesignPattern.CodeDesign.auth;

import DesignPattern.CodeDesign.auth.request.ApiRequest;
import DesignPattern.CodeDesign.auth.storage.CredentialStorage;
import DesignPattern.CodeDesign.auth.storage.MysqlCredentialStorage;
import DesignPattern.CodeDesign.auth.token.AuthToken;

public class DefaultApiAuthenticatorImpl implements IApiAuthenticator{

    private CredentialStorage credentialStorage;

    public DefaultApiAuthenticatorImpl() {
        this.credentialStorage = new MysqlCredentialStorage();
    }

    public DefaultApiAuthenticatorImpl(CredentialStorage credentialStorage) {
        this.credentialStorage = credentialStorage;
    }

    @Override
    public void auth(String url) {
        ApiRequest apiRequest = ApiRequest.createFromUrl(url);
        auth(apiRequest);
    }

    @Override
    public void auth(ApiRequest apiRequest) {
        String appId = apiRequest.getAppId();
        String token = apiRequest.getToken();
        long timestamp = apiRequest.getTimestamp();
        String baseUrl = apiRequest.getOriginalUrl();

        AuthToken clientAuthToken = new AuthToken(token, timestamp);
        if (clientAuthToken.isExpired()) {
            throw new RuntimeException("Token is expired");
        }

        String password = credentialStorage.getPasswordById(appId);
        AuthToken serverAuthToken = AuthToken.generate(baseUrl, appId, password, timestamp);
        if (!serverAuthToken.match(clientAuthToken)) {
            throw new RuntimeException("Token verification fail");
        }
    }
}
