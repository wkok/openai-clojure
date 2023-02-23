# openai-clojure

![CI](https://github.com/wkok/openai-clojure/workflows/CI/badge.svg) [![cljdoc badge](https://cljdoc.org/badge/net.clojars.wkok/openai-clojure)](https://cljdoc.org/d/net.clojars.wkok/openai-clojure)

Clojure functions to drive the [OpenAI API](https://platform.openai.com/docs/introduction)
and [Azure OpenAI API](https://learn.microsoft.com/en-us/azure/cognitive-services/openai/reference)

This library aims to hide the small differences between the 2 APIs, and allows therefore to develop
tools and applications which can work with both variants.

## Documentation

* [Usage - OpenAI](/doc/01-usage-openai.md)
* [Usage - Azure OpenAI](/doc/02-usage-azure.md)
* [Streaming Tokens](/doc/03-streaming.md)
* [API Reference](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.3.0/api/wkok.openai-clojure.api)

## Supported APIs

|             | OpenAI | Azure OpenAI |
| ----------- | :----: | :----------: |
| Version     | v1     | v2022-12-01  |
| Completion  | X      | X            |
| Embeddings  | X      | X            |
| Models      | X      |              |
| Edits       | X      |              |
| Images      | X      |              |
| Files       | X      |              |
| Fine-tunes  | X      |              |
| Moderations | X      |              |

### OpenAI v1

* [Models](https://platform.openai.com/docs/api-reference/models)
* [Completion](https://platform.openai.com/docs/api-reference/completions)
* [Edits](https://platform.openai.com/docs/api-reference/edits)
* [Images](https://platform.openai.com/docs/api-reference/images)
* [Embeddings](https://platform.openai.com/docs/api-reference/embeddings)
* [Files](https://platform.openai.com/docs/api-reference/files)
* [Fine-tunes](https://platform.openai.com/docs/api-reference/fine-tunes)
* [Moderations](https://platform.openai.com/docs/api-reference/moderations)

### Azure OpenAI v2022-12-01
* [Completion](https://learn.microsoft.com/en-us/azure/cognitive-services/openai/reference#completions)
* [Embeddings](https://learn.microsoft.com/en-us/azure/cognitive-services/openai/reference#embeddings)

## Configuration

[![Clojars Project](https://img.shields.io/clojars/v/net.clojars.wkok/openai-clojure.svg)](https://clojars.org/net.clojars.wkok/openai-clojure)

Add the `openai-clojure` dependency

### deps.edn

```
net.clojars.wkok/openai-clojure {:mvn/version "0.3.0"}
```

### Leiningen project.clj

```
[net.clojars.wkok/openai-clojure "0.3.0"]
```

## Java

Minimum Java 11 required

## Authentication

### OpenAI

#### API Key

Set the environment variable `OPENAI_API_KEY` to your OpenAI API key.

An API key can be generated in your [OpenAI account](https://platform.openai.com/account/api-keys)

#### Organization

*Optional* - If your OpenAI account uses multiple organizations, set the environment variable `OPENAI_ORGANIZATION` to the one used for your app.

### Azure OpenAI

#### API Key

Set the environment variable `AZURE_OPENAI_API_KEY` to your Azure OpenAI key.

#### API endpoint

Set the environment variable `AZURE_OPENAI_API_ENDPOINT` to your Azure OpenAI endpoint, example: *https://myendpoint.openai.azure.com*

## Quickstart

See the full [API Reference](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.3.0/api/wkok.openai-clojure.api) for examples of all the supported OpenAI APIs.

Require the `api` namespace

```
(:require [wkok.openai-clojure.api :as api])
```

A simple completion prompt for OpenAI could be:

```
(api/create-completion {:model "text-davinci-003"
                        :prompt "Say this is a test"
                        :max_tokens 7
                        :temperature 0})
```

or for Azure OpenAI:

```
(api/create-completion :azure {:model "text-davinci-003"
                               :prompt "Say this is a test"
                               :max_tokens 7
                               :temperature 0})
```


Result:
```
{:id "cmpl-6jY1xInJeGGpzUgsZtkuxDsf5DdBa",
 :object "text_completion",
 :created 1676313593,
 :model "text-davinci-003",
 :choices
 [{:text "\n\nThis is indeed a test",
   :index 0,
   :logprobs nil,
   :finish_reason "length"}],
 :usage {:prompt_tokens 5, :completion_tokens 7, :total_tokens 12}}
```

## Issues and features

Please feel free to raise issues on Github or send pull requests

## Acknowledgements

This library uses [Martian](https://github.com/oliyh/martian) - An HTTP abstraction library

## License

**This is an unofficial library, it is not affiliated with nor endorsed by OpenAI**

[MIT License](https://github.com/wkok/re-frame-crux/blob/master/LICENSE)

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
