# Introduction - OpenAI

Clojure functions to drive the [OpenAI API](https://platform.openai.com/docs/introduction)

## Configuration

Add the `openai-clojure` dependency

### deps.edn

```
net.clojars.wkok/openai-clojure {:mvn/version "0.2.0"}
```

### Leiningen project.clj

```
[net.clojars.wkok/openai-clojure "0.2.0"]
```

## Authentication

### API Key

Set the environment variable `OPENAI_API_KEY` to your OpenAI API key.

An API key can be generated in your [OpenAI account](https://platform.openai.com/account/api-keys)

### Organization

*Optional* - If your OpenAI account uses multiple organizations, set the environment variable `OPENAI_ORGANIZATION` to the one used for your app.

## Quickstart

See the full [openai-clojure](wkok.openai-clojure.api.html) api documentation for examples of all the supported OpenAI APIs.

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

* [list-models](wkok.openai-clojure.api.html#var-list-models)
* [retrieve-model](wkok.openai-clojure.api.html#var-retrieve-model)

Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/models)

### Completions

* [create-completion](wkok.openai-clojure.api.html#var-create-completion)

Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/completions)

### Edits

* [create-edit](wkok.openai-clojure.api.html#var-create-edit)

Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/edits)

### Images

* [create-image](wkok.openai-clojure.api.html#var-create-image)
* [create-image-edit](wkok.openai-clojure.api.html#var-create-image-edit)
* [create-image-variation](wkok.openai-clojure.api.html#var-create-image-variation)

Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/images)

### Embeddings

* [create-embedding](wkok.openai-clojure.api.html#var-create-embedding)

Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/embeddings)

### Files

* [list-files](wkok.openai-clojure.api.html#var-list-files)
* [create-file](wkok.openai-clojure.api.html#var-create-file)
* [delete-file](wkok.openai-clojure.api.html#var-delete-file)
* [retrieve-file](wkok.openai-clojure.api.html#var-retrieve-file)
* [download-file](wkok.openai-clojure.api.html#var-download-file)

Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/files)

### Fine-tunes

* [create-fine-tune](wkok.openai-clojure.api.html#var-create-fine-tune)
* [list-fine-tunes](wkok.openai-clojure.api.html#var-list-fine-tunes)
* [retrieve-fine-tune](wkok.openai-clojure.api.html#var-retrieve-fine-tune)
* [cancel-fine-tune](wkok.openai-clojure.api.html#var-cancel-fine-tune)
* [list-fine-tune-events](wkok.openai-clojure.api.html#var-list-fine-tune-events)
* [delete-model](wkok.openai-clojure.api.html#var-delete-model)

Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/fine-tunes)

### Moderations

* [create-moderation](wkok.openai-clojure.api.html#var-create-moderation)

Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/moderations)
