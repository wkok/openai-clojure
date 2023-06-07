# Change Log

## 0.6.1 - 2023-06-07
### Supported API versions
- OpenAI v1.2.0
- Azure OpenAI v2023-05-15
### Changes
- Fixed issue with multibyte characters when streaming events [PR21](https://github.com/wkok/openai-clojure/pull/21)

## 0.6.0 - 2023-05-08
### Supported API versions
- OpenAI v1.2.0
- Azure OpenAI v2023-05-15
### Changes
- Add support for Azure Chat Completions api
- Support passing the Azure `:api-endpoint` in `options` of each api function

## 0.5.1 - 2023-04-15
### Supported API versions
- OpenAI v1.2.0
- Azure OpenAI v2022-12-01
### Changes
- Fix content-type warnings printed to console on first invocation

## 0.5.0 - 2023-03-19
### Supported API versions
- OpenAI v1.2.0
- Azure OpenAI v2022-12-01
### Changes
- Support passing `:api-key` and `:impl` as options to api functions
- Deprecate support for passing `:impl` as 2nd argument, while remaining backward compatible.

## 0.4.0 - 2023-03-11
### Supported API versions
- OpenAI v1.2.0
- Azure OpenAI v2022-12-01
### Changes
- Add support for Chat API
- Add support for Audio API

## 0.3.1 - 2023-02-23
### Supported API versions
- OpenAI v1.1.0
- Azure OpenAI v2022-12-01
### Changes
- Replace codox documentation generation with cljdoc
- Change documentation hosting from github pages to cljdoc

## 0.3.0 - 2023-02-20
### Supported API versions
- OpenAI v1.1.0
- Azure OpenAI v2022-12-01
### Changes
- Implemented server-sent events using `stream` in `create-completion`
- Replaced clj-http with hato, falling back to the native Java 11 http client

## 0.2.0 - 2023-02-15
### Supported API versions
- OpenAI v1.1.0
- Azure OpenAI v2022-12-01
### Changes
- Azure OpenAI API Support (thanks [Carsten Behring](https://github.com/behrica))

## 0.1.14 - 2023-02-14
### Supported API versions
- OpenAI v1.1.0
### Changes
- OpenAI API Support
- Initial release
