# Usage - OpenAI

Clojure functions to drive the [OpenAI API](https://platform.openai.com/docs/introduction)

## Configuration

Add the `openai-clojure` dependency

### deps.edn

```
net.clojars.wkok/openai-clojure {:mvn/version "0.12.0"}
```

### Leiningen project.clj

```
[net.clojars.wkok/openai-clojure "0.12.0"]
```

## Authentication

### API Key

Set the environment variable `OPENAI_API_KEY` to your OpenAI API key.

An API key can be generated in your [OpenAI account](https://platform.openai.com/account/api-keys)

### Organization

*Optional* - If your OpenAI account uses multiple organizations, set the environment variable `OPENAI_ORGANIZATION` to the one used for your app.

### Endpoint

It is not necessary to specify the endpoint url if using the default OpenAI service. If you do need to point to a different endpoint set the environment variable `OPENAI_API_ENDPOINT` for example: *https://myendpoint.my-openai.com*

### Options

Alternatively the `api-key` and/or `organization` and/or `api-endpoint` can be passed in the `options` argument of each api function

```
(api/create-completion {:model "text-davinci-003"
                        :prompt "Say this is a test"}
                       {:api-key "xxxxx"
                        :organization "abcd"
                        :api-endpoint "https://myendpoint.my-openai.com"})
```

## Quickstart

See the full [API Reference](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.12.0/api/wkok.openai-clojure.api) api documentation for examples of all the supported OpenAI APIs.

Require the `api` namespace

```
(:require [wkok.openai-clojure.api :as api])
```

A simple chat conversation with ChatGPT could be:

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

## Supported OpenAI APIs

### Models

* [list-models](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.12.0/api/wkok.openai-clojure.api#list-models)
* [retrieve-model](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.12.0/api/wkok.openai-clojure.api#retrieve-model)

Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/models)

### Completions

* [create-completion](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.12.0/api/wkok.openai-clojure.api#create-completion)

Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/completions)

### Chat

* [create-chat-completion](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.12.0/api/wkok.openai-clojure.api#create-chat-completion)

Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/chat)

### Edits

* [create-edit](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.12.0/api/wkok.openai-clojure.api#create-edit)

Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/edits)

### Images

* [create-image](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.12.0/api/wkok.openai-clojure.api#create-image)
* [create-image-edit](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.12.0/api/wkok.openai-clojure.api#create-image-edit)
* [create-image-variation](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.12.0/api/wkok.openai-clojure.api#create-image-variation)

Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/images)

### Embeddings

* [create-embedding](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.12.0/api/wkok.openai-clojure.api#create-embedding)

Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/embeddings)

### Audio

* [create-transcription](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.12.0/api/wkok.openai-clojure.api#create-transcription)
* [create-translation](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.12.0/api/wkok.openai-clojure.api#create-translation)

Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/audio)

### Files

* [list-files](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.12.0/api/wkok.openai-clojure.api#list-files)
* [create-file](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.12.0/api/wkok.openai-clojure.api#create-file)
* [delete-file](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.12.0/api/wkok.openai-clojure.api#delete-file)
* [retrieve-file](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.12.0/api/wkok.openai-clojure.api#retrieve-file)
* [download-file](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.12.0/api/wkok.openai-clojure.api#download-file)

Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/files)

### Fine-tunes

* [create-fine-tune](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.12.0/api/wkok.openai-clojure.api#create-fine-tune)
* [list-fine-tunes](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.12.0/api/wkok.openai-clojure.api#list-fine-tunes)
* [retrieve-fine-tune](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.12.0/api/wkok.openai-clojure.api#retrieve-fine-tune)
* [cancel-fine-tune](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.12.0/api/wkok.openai-clojure.api#cancel-fine-tune)
* [list-fine-tune-events](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.12.0/api/wkok.openai-clojure.api#list-fine-tune-events)
* [delete-model](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.12.0/api/wkok.openai-clojure.api#delete-model)

Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/fine-tunes)

### Moderations

* [create-moderation](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.12.0/api/wkok.openai-clojure.api#create-moderation)

Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/moderations)
