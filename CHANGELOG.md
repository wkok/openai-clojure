# Change Log

## 0.22.0 - 2024-11-05
### Supported API versions
- OpenAI v2.3.0
- Azure OpenAI v2024-06-01
### Changes
- Added support for OpenAI Vector Stores [PR 75](https://github.com/wkok/openai-clojure/pull/75)

## 0.21.2 - 2024-11-03
### Supported API versions
- OpenAI v2.3.0
- Azure OpenAI v2024-06-01
### Changes
- Fixed Azure SSE Streaming Support via the `:stream` parameter [PR 73](https://github.com/wkok/openai-clojure/pull/73)

## 0.21.1 - 2024-09-24
### Supported API versions
- OpenAI v2.3.0
- Azure OpenAI v2024-06-01
### Changes
- Patched the azure schema to properly support multipart file uploads [Issue 71](https://github.com/wkok/openai-clojure/issues/71)

## 0.21.0 - 2024-09-03
### Supported API versions
- OpenAI v2.3.0
- Azure OpenAI v2024-06-01
### Changes
- Added support for a `:stream/close?` parameter to ensure `core.async` channel gets closed. See [this PR](https://github.com/wkok/openai-clojure/pull/63)

## 0.20.0 - 2024-08-28
### Supported API versions
- OpenAI v2.3.0
- Azure OpenAI v2024-06-01
### Changes
- Added support for Azure OpenAI Translations, Transcriptions, Images as supported in the GA release v2024-06-01

## 0.19.0 - 2024-08-08
### Supported API versions
- OpenAI v2.3.0
- Azure OpenAI v2024-06-01
### Changes
- Updated to latest OpenAI spec commit a1bdd5f from 2024-08-08
- Updated to latest Azure OpenAI spec (stable) commit 81a4ee5 from 2024-06-28

## 0.18.1 - 2024-06-15
### Supported API versions
- OpenAI v2.0.0
- Azure OpenAI v2023-12-01-preview
### Changes
- Fixed [Issue 65](https://github.com/wkok/openai-clojure/issues/65) restored `submit-tool-outputs-to-run`


## 0.18.0 - 2024-06-10
### Supported API versions
- OpenAI v2.0.0
- Azure OpenAI v2023-12-01-preview
### Changes
- Updated to latest OpenAI spec commit 4e97768 from 2024-06-07
- Removed deprecated functions
  - list-assistant-files
  - create-assistant-file
  - retrieve-assistant-file
  - delete-assistant-file
  - list-message-files
  - retrieve-message-file
  - submit-tool-outputs-to-run

## 0.17.0 - 2024-05-19
### Supported API versions
- OpenAI v2.0.0
- Azure OpenAI v2023-12-01-preview
### Changes
- Removed LinkedList import to enable Babashka support PR [60](https://github.com/wkok/openai-clojure/pull/60)
- Updated assistants HTTP header PR [61](https://github.com/wkok/openai-clojure/pull/61)

## 0.16.0 - 2024-02-09
### Supported API versions
- OpenAI v2.0.0
- Azure OpenAI v2023-12-01-preview
### Changes
- Added async support PR [53](https://github.com/wkok/openai-clojure/pull/53)
- Updated to latest Azure spec v2023-12-01-preview
- Updated to latest OpenAI spec as at 2024-02-09
- Removed deprecated endpoints - edit
- Removed deprecated endpoints - fine tune

## 0.15.0 - 2024-01-14
### Supported API versions
- OpenAI v2.0.0
- Azure OpenAI v2023-05-15
### Changes
- Added support for a `:trace` function in `:options` to capture raw request details. See [doc](doc/04-debugging.md) and PR [50](https://github.com/wkok/openai-clojure/pull/50)

## 0.14.0 - 2023-11-24
### Supported API versions
- OpenAI v2.0.0
- Azure OpenAI v2023-05-15
### Changes
- Added ability to pass http client request options like the timeout. Issue [44](https://github.com/wkok/openai-clojure/issues/44)
- Added ability to specify the sliding buffer size in an SSE request, or default to infinite buffer. Issue [43](https://github.com/wkok/openai-clojure/issues/43)
- Fixed warning about no matching content-type [42](https://github.com/wkok/openai-clojure/issues/42)

## 0.13.0 - 2023-11-19
### Supported API versions
- OpenAI v2.0.0
- Azure OpenAI v2023-05-15
### Changes
- Added support for the [speech api](https://platform.openai.com/docs/api-reference/audio/createSpeech) (thanks [Stoica George-Ovidiu](https://github.com/ovistoica))
- Fixed schema validation issue [41](https://github.com/wkok/openai-clojure/issues/41) when including the `:tools` parameter in a chat completions request

## 0.12.3 - 2023-11-15
### Supported API versions
- OpenAI v2.0.0
- Azure OpenAI v2023-05-15
### Changes
- Updated to latest openai spec

## 0.12.2 - 2023-11-09
### Supported API versions
- OpenAI v2.0.0
- Azure OpenAI v2023-05-15
### Changes
- (Beta) Added support for Assistants, Threads, Messages & Runs (thanks [Bader Szabolcs](https://github.com/damesek))

## 0.11.1 - 2023-11-04
### Supported API versions
- OpenAI v2.0.0
- Azure OpenAI v2023-05-15
### Changes
- Fixed issue where options was incorrectly added into multipart fields

## 0.11.0 - 2023-09-24
### Supported API versions
- OpenAI v2.0.0
- Azure OpenAI v2023-05-15
### Changes
- Updated to the latest OpenAI API v2.0.0
- Added support for new fine tuning api
- Deprecated old fine tunes api

## 0.10.0 - 2023-09-07
### Supported API versions
- OpenAI v2.0.0
- Azure OpenAI v2023-05-15
### Changes
- Added ability to override the OpenAI `:api-endpoint` either in `options` or as ENV variable `OPENAI_API_ENDPOINT`

## 0.9.0 - 2023-07-26
### Supported API versions
- OpenAI v2.0.0
- Azure OpenAI v2023-05-15
### Changes
- Updated to the latest OpenAI API v2.0.0 spec patches which includes allowing the content field nullable when function calling. Fixes [Issue 30](https://github.com/wkok/openai-clojure/issues/30)

## 0.8.0 - 2023-06-22
### Supported API versions
- OpenAI v2.0.0
- Azure OpenAI v2023-05-15
### Changes
- Fixed issue with spec validation failing when spec instrumentation is enabled [Issue 25](https://github.com/wkok/openai-clojure/issues/25)
- Added support for OpenAI API v2.0.0 which mainly removed unused & deprecated operations

## 0.7.0 - 2023-06-17
### Supported API versions
- OpenAI v1.3.0
- Azure OpenAI v2023-05-15
### Changes
- Added support for OpenAI API v1.3.0 which includes functions

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
