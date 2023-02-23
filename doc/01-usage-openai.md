# Usage - OpenAI

Clojure functions to drive the [OpenAI API](https://platform.openai.com/docs/introduction)

## Configuration

Add the `openai-clojure` dependency

### deps.edn

```
net.clojars.wkok/openai-clojure {:mvn/version "0.3.1"}
```

### Leiningen project.clj

```
[net.clojars.wkok/openai-clojure "0.3.1"]
```

## Authentication

### API Key

Set the environment variable `OPENAI_API_KEY` to your OpenAI API key.

An API key can be generated in your [OpenAI account](https://platform.openai.com/account/api-keys)

### Organization

*Optional* - If your OpenAI account uses multiple organizations, set the environment variable `OPENAI_ORGANIZATION` to the one used for your app.

## Quickstart

See the full [API Reference](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.3.1/api/wkok.openai-clojure.api) api documentation for examples of all the supported OpenAI APIs.

Require the `api` namespace

```
(:require [wkok.openai-clojure.api :as api])
```

A simple completion prompt could be:

```
(api/create-completion {:model "text-davinci-003"
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

## Supported OpenAI APIs

### Models

* [list-models](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.3.1/api/wkok.openai-clojure.api#list-models)
* [retrieve-model](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.3.1/api/wkok.openai-clojure.api#retrieve-model)

Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/models)

### Completions

* [create-completion](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.3.1/api/wkok.openai-clojure.api#create-completion)

Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/completions)

### Edits

* [create-edit](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.3.1/api/wkok.openai-clojure.api#create-edit)

Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/edits)

### Images

* [create-image](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.3.1/api/wkok.openai-clojure.api#create-image)
* [create-image-edit](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.3.1/api/wkok.openai-clojure.api#create-image-edit)
* [create-image-variation](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.3.1/api/wkok.openai-clojure.api#create-image-variation)

Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/images)

### Embeddings

* [create-embedding](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.3.1/api/wkok.openai-clojure.api#create-embedding)

Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/embeddings)

### Files

* [list-files](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.3.1/api/wkok.openai-clojure.api#list-files)
* [create-file](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.3.1/api/wkok.openai-clojure.api#create-file)
* [delete-file](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.3.1/api/wkok.openai-clojure.api#delete-file)
* [retrieve-file](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.3.1/api/wkok.openai-clojure.api#retrieve-file)
* [download-file](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.3.1/api/wkok.openai-clojure.api#download-file)

Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/files)

### Fine-tunes

* [create-fine-tune](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.3.1/api/wkok.openai-clojure.api#create-fine-tune)
* [list-fine-tunes](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.3.1/api/wkok.openai-clojure.api#list-fine-tunes)
* [retrieve-fine-tune](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.3.1/api/wkok.openai-clojure.api#retrieve-fine-tune)
* [cancel-fine-tune](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.3.1/api/wkok.openai-clojure.api#cancel-fine-tune)
* [list-fine-tune-events](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.3.1/api/wkok.openai-clojure.api#list-fine-tune-events)
* [delete-model](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.3.1/api/wkok.openai-clojure.api#delete-model)

Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/fine-tunes)

### Moderations

* [create-moderation](https://cljdoc.org/d/net.clojars.wkok/openai-clojure/0.3.1/api/wkok.openai-clojure.api#create-moderation)

Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/moderations)
