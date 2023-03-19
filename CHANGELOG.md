# Change Log

## 0.5.0 - 2023-03-19
### Supported API versions
- OpenAI v1.2.0
- Azure OpenAI 2022-12-01
### Changes
- Support passing `:api-key` and `:impl` as options to api functions
- Deprecate support for passing `:impl` as 2nd argument, while remaining backward compatible.

## 0.4.0 - 2023-03-11
### Supported API versions
- OpenAI v1.2.0
- Azure OpenAI 2022-12-01
### Changes
- Add support for Chat API
- Add support for Audio API

## 0.3.1 - 2023-02-23
### Supported API versions
- OpenAI v1.1.0
- Azure OpenAI 2022-12-01
### Changes
- Replace codox documentation generation with cljdoc
- Change documentation hosting from github pages to cljdoc

## 0.3.0 - 2023-02-20
### Supported API versions
- OpenAI v1.1.0
- Azure OpenAI 2022-12-01
### Changes
- Implemented server-sent events using `stream` in `create-completion`
- Replaced clj-http with hato, falling back to the native Java 11 http client

## 0.2.0 - 2023-02-15
### Supported API versions
- OpenAI v1.1.0
- Azure OpenAI 2022-12-01
### Changes
- Azure OpenAI API Support (thanks [Carsten Behring](https://github.com/behrica))

## 0.1.14 - 2023-02-14
### Supported API versions
- OpenAI v1.1.0
### Changes
- OpenAI API Support
- Initial release
