This application illustrates two ways of creating and interacting with the Okta client SDK. It is based on information found here: https://github.com/okta/okta-sdk-java

Depending on which type of client you create, the application expects the following application arguments:
- In case of a client with an API token:
  - `okta-domain`: your Okta domain, e.g. dev-12345678.okta.com
  - `api-token`: the API token you created in your account, for details see https://developer.okta.com/docs/guides/create-an-api-token/create-the-token/
- In case of a client with OAuth2:
  - `okta-domain`: your Okta domain, e.g. dev-12345678.okta.com
  - `client-id`: the client id of the service app in your Okta account. For more details, see https://developer.okta.com/docs/guides/implement-oauth-for-okta-serviceapp/overview/