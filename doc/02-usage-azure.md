# Usage - Azure OpenAI

Clojure functions to drive the [Azure OpenAI API](https://learn.microsoft.com/en-us/azure/cognitive-services/openai/reference)

## Configuration

Add the `openai-clojure` dependency

### deps.edn

```
net.clojars.wkok/openai-clojure {:mvn/version "0.21.0"}
```

### Leiningen project.clj

```
[net.clojars.wkok/openai-clojure "0.21.0"]
```

## Authentication

### API Key

Set the environment variable `AZURE_OPENAI_API_KEY` to your Azure OpenAI API key.

An API key can be retrieved from your [Azure account](https://learn.microsoft.com/en-us/azure/cognitive-services/openai/quickstart?pivots=programming-language-python#retrieve-key-and-endpoint)

### Endpoint

Set the environment variable `AZURE_OPENAI_API_ENDPOINT` to your [Azure OpenAPI endpoint](https://learn.microsoft.com/en-us/azure/cognitive-services/openai/quickstart?pivots=programming-language-python#retrieve-key-and-endpoint), example: *https://myendpoint.openai.azure.com*

### Options

Alternatively the `api-key` and/or `api-endpoint` can be passed in the `options` argument of each api function

```clojure
(api/create-completion {:model "text-davinci-003"
                        :prompt "Say this is a test"}
                       {:api-key "xxxxx"
                        :api-endpoint "https://myendpoint.openai.azure.com"
                        :impl :azure})
```

## Quickstart

See the full [API Reference](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.21.0/api/wkok.openai-clojure.api) api documentation for examples of all the supported OpenAI APIs.

Require the `api` namespace

```
(:require [wkok.openai-clojure.api :as api])
```

A simple chat conversation with ChatGPT could be:

```clojure
(api/create-chat-completion {:model "gpt-35-turbo"
                             :messages [{:role "system" :content "You are a helpful assistant."}
                                        {:role "user" :content "Who won the world series in 2020?"}
                                        {:role "assistant" :content "The Los Angeles Dodgers won the World Series in 2020."}
                                        {:role "user" :content "Where was it played?"}]}
                            {:impl :azure})
```

Result:
```clojure
{:id "chatcmpl-6srOKLabYTpTRwRUQxjkcBxw3uf1H",
 :object "chat.completion",
 :created 1678532968,
 :model "gpt-35-turbo-0301",
 :usage {:prompt_tokens 56, :completion_tokens 19, :total_tokens 75},
 :choices
 [{:message
   {:role "assistant",
    :content
    "The 2020 World Series was played at Globe Life Field in Arlington, Texas."},
   :finish_reason "stop",
   :index 0}]}
```

## Advanced configuration

### Request options

Request options may be set on the underlying hato http client by adding a `:request` map to `:options` for example setting the request timeout:

```clojure
(api/create-chat-completion {:model "gpt-3.5-turbo"
                             :messages [{:role "system" :content "You are a helpful assistant."}
                                        {:role "user" :content "Who won the world series in 2020?"}
                                        {:role "assistant" :content "The Los Angeles Dodgers won the World Series in 2020."}
                                        {:role "user" :content "Where was it played?"}]}

                            {:request {:timeout 60000}})    ;; <== This
```

Any of these [supported request options](https://github.com/gnarroway/hato#request-options) may be passed.


## Supported Azure OpenAI APIs

### Completions

* [create-completion](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.21.0/api/wkok.openai-clojure.api#create-completion)

Also see the [Azure OpenAI documentation](https://learn.microsoft.com/en-us/azure/ai-services/openai/reference#completions)

### Chat

* [create-chat-completion](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.21.0/api/wkok.openai-clojure.api#create-chat-completion)

Also see the [Azure OpenAI documentation](https://learn.microsoft.com/en-us/azure/ai-services/openai/reference#chat-completions)

### Embeddings

* [create-embedding](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.21.0/api/wkok.openai-clojure.api#create-embedding)

Also see the [Azure OpenAI documentation](https://learn.microsoft.com/en-us/azure/ai-services/openai/reference#embeddings)

### Transcriptions

* [create-transcription](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.21.0/api/wkok.openai-clojure.api#create-transcription)

Also see the [Azure OpenAI documentation](https://learn.microsoft.com/en-us/azure/ai-services/openai/reference#transcriptions)

### Translations

* [create-translation](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.21.0/api/wkok.openai-clojure.api#create-translation)

Also see the [Azure OpenAI documentation](https://learn.microsoft.com/en-us/azure/ai-services/openai/reference#translations)

### Images

* [create-image](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.21.0/api/wkok.openai-clojure.api#create-image)

Also see the [Azure OpenAI documentation](https://learn.microsoft.com/en-us/azure/ai-services/openai/reference#image-generation)
