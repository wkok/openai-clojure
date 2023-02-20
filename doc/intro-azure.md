# Introduction - Azure OpenAI

Clojure functions to drive the [Azure OpenAI API](https://learn.microsoft.com/en-us/azure/cognitive-services/openai/reference)

## Configuration

Add the `openai-clojure` dependency

### deps.edn

```
net.clojars.wkok/openai-clojure {:mvn/version "0.3.0"}
```

### Leiningen project.clj

```
[net.clojars.wkok/openai-clojure "0.3.0"]
```

## Authentication

Set the environment variable `AZURE_OPENAI_API_KEY` to your Azure OpenAI API key.

An API key can be retrieved from your [Azure account](https://learn.microsoft.com/en-us/azure/cognitive-services/openai/quickstart?pivots=programming-language-python#retrieve-key-and-endpoint)

## Endpoint

Set the environment variable `AZURE_OPENAI_API_ENDPOINT` to your [Azure OpenAPI endpoint](https://learn.microsoft.com/en-us/azure/cognitive-services/openai/quickstart?pivots=programming-language-python#retrieve-key-and-endpoint), example: *https://myendpoint.openai.azure.com*

## Quickstart

See the full [openai-clojure](wkok.openai-clojure.api.html) api documentation for examples of all the supported OpenAI APIs.

Require the `api` namespace

```
(:require [wkok.openai-clojure.api :as api])
```

A simple completion prompt could be:

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

## Supported Azure OpenAI APIs

### Completions

* [create-completion](wkok.openai-clojure.api.html#var-create-completion)

Also see the [Azure OpenAI documentation](https://learn.microsoft.com/en-us/azure/cognitive-services/openai/reference#completions)

### Embeddings

* [create-embedding](wkok.openai-clojure.api.html#var-create-embedding)

Also see the [Azure OpenAI documentation](https://learn.microsoft.com/en-us/azure/cognitive-services/openai/reference#embeddings)
