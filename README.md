# openai-clojure (Unofficial)

[![Clojars Project](https://img.shields.io/clojars/v/net.clojars.wkok/openai-clojure.svg)](https://clojars.org/net.clojars.wkok/openai-clojure)
![CI](https://github.com/wkok/openai-clojure/workflows/CI/badge.svg)

Clojure functions to drive the [OpenAI API](https://platform.openai.com/docs/introduction)
and [Azure OpenAI API](https://learn.microsoft.com/en-us/azure/cognitive-services/openai/reference)

This unofficial library aims to hide the small differences between the 2 APIs, and allows therefore to develop
tools and applications which can work with both variants.

## Documentation

[![cljdoc badge](https://cljdoc.org/badge/net.clojars.wkok/openai-clojure)](https://cljdoc.org/d/net.clojars.wkok/openai-clojure)

* [Usage - OpenAI](/doc/01-usage-openai.md)
* [Usage - Azure OpenAI](/doc/02-usage-azure.md)
* [Streaming Tokens](/doc/03-streaming.md)
* [API Reference](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.10.0/api/wkok.openai-clojure.api)

## Supported APIs

|             | OpenAI | Azure OpenAI |
| ----------- | :----: | :----------: |
| Version     | v2.0.0 | v2023-05-15  |
| [Chat](https://platform.openai.com/docs/api-reference/chat) | X | X |
| [Audio](https://platform.openai.com/docs/api-reference/audio) | X | |
| [Completion](https://platform.openai.com/docs/api-reference/completions) | X | X |
| [Embeddings](https://platform.openai.com/docs/api-reference/embeddings) | X | X |
| [Models](https://platform.openai.com/docs/api-reference/models) | X | |
| [Edits](https://platform.openai.com/docs/api-reference/edits) | X | |
| [Images](https://platform.openai.com/docs/api-reference/images) | X | |
| [Files](https://platform.openai.com/docs/api-reference/files) | X | |
| [Fine-tuning](https://platform.openai.com/docs/api-reference/fine-tuning) | X | |
| [Moderations](https://platform.openai.com/docs/api-reference/moderations) | X | |
| [Fine-tunes (deprecated)](https://platform.openai.com/docs/api-reference/fine-tunes) | X | |

## Configuration

[![Clojars Project](https://img.shields.io/clojars/v/net.clojars.wkok/openai-clojure.svg)](https://clojars.org/net.clojars.wkok/openai-clojure)

Add the `openai-clojure` dependency

### deps.edn

```
net.clojars.wkok/openai-clojure {:mvn/version "0.10.0"}
```

### Leiningen project.clj

```
[net.clojars.wkok/openai-clojure "0.10.0"]
```

## Java

Minimum Java 11 required

## Authentication

### OpenAI

#### API Key

Set the environment variable `OPENAI_API_KEY` to your OpenAI API key.

(For alternative options to pass the API Key see [options](/doc/01-usage-openai.md#options))

An API key can be generated in your [OpenAI account](https://platform.openai.com/account/api-keys)

#### Organization

*Optional* - If your OpenAI account uses multiple organizations, set the environment variable `OPENAI_ORGANIZATION` to the one used for your app.

### Azure OpenAI

See: [Authentication - Azure OpenAI](/doc/02-usage-azure.md#authentication)

## Quickstart

See the full [API Reference](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.10.0/api/wkok.openai-clojure.api) for examples of all the supported OpenAI APIs.

Require the `api` namespace

```
(:require [wkok.openai-clojure.api :as api])
```

A simple chat conversation with OpenAI's ChatGPT could be:

```
(api/create-chat-completion {:model "gpt-3.5-turbo"
                             :messages [{:role "system" :content "You are a helpful assistant."}
                                        {:role "user" :content "Who won the world series in 2020?"}
                                        {:role "assistant" :content "The Los Angeles Dodgers won the World Series in 2020."}
                                        {:role "user" :content "Where was it played?"}]})
```

Result:
```
{:id "chatcmpl-6srOKLabYTpTRwRUQxjkcBxw3uf1H",
 :object "chat.completion",
 :created 1678532968,
 :model "gpt-3.5-turbo-0301",
 :usage {:prompt_tokens 56, :completion_tokens 19, :total_tokens 75},
 :choices
 [{:message
   {:role "assistant",
    :content
    "The 2020 World Series was played at Globe Life Field in Arlington, Texas."},
   :finish_reason "stop",
   :index 0}]}
```

## Issues and features

Please feel free to raise issues on Github or send pull requests

## Acknowledgements

This library uses [Martian](https://github.com/oliyh/martian) - An HTTP abstraction library

## License

**This is an unofficial library, it is not affiliated with nor endorsed by OpenAI**

[MIT License](https://github.com/wkok/openai-clojure/blob/master/LICENSE)

Copyright (c) 2023 Werner Kok

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
