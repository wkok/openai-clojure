# openai-clojure

![CI](https://github.com/wkok/openai-clojure/workflows/CI/badge.svg)

Clojure functions to drive the [OpenAI API](https://platform.openai.com/docs/introduction)

## Documentation

* [Introduction](https://wkok.github.io/openai-clojure/intro.html)
* [API Reference](https://wkok.github.io/openai-clojure/wkok.openai-clojure.api.html)

## Configuration

[![Clojars Project](https://img.shields.io/clojars/v/net.clojars.wkok/openai-clojure.svg)](https://clojars.org/net.clojars.wkok/openai-clojure)

Add the `openai-clojure` dependency

### deps.edn

```
net.clojars.wkok/openai-clojure {:mvn/version "0.1.14"}
```

### Leiningen project.clj

```
[net.clojars.wkok/openai-clojure "0.1.14"]
```

## Authentication

### API Key

Set the environment variable `OPENAI_API_KEY` to your OpenAI API key.

An API key can be generated in your [OpenAI account](https://platform.openai.com/account/api-keys)

### Organization

*Optional* - If your OpenAI account uses multiple organizations, set the environment variable `OPENAI_ORGANIZATION` to the one used for your app.

## Quickstart

See the full [openai-clojure](https://wkok.github.io/openai-clojure/wkok.openai-clojure.api.html) api documentation for examples of all the supported OpenAI APIs.

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

## Development

### Emacs

You can set your OpenAI API key environment variable in `.dir-locals.el`

```
((nil
  (eval . (progn
            (setenv "OPENAI_API_KEY" "your-key-here")))))
```

Use `cider-jack-in-clj` to start Clojure REPL for development.

### Tests

Run the project's tests:

```
clojure -T:build test
```

### Build

Run the project's CI pipeline and build a JAR:

```
clojure -T:build ci
```

This will produce an updated `pom.xml` file with synchronized dependencies inside the `META-INF`
directory inside `target/classes` and the JAR in `target`. You can update the version (and SCM tag)
information in generated `pom.xml` by updating `build.clj`

### Install

Install it locally (requires the `ci` task be run first):

```
clojure -T:build install
```

### Deploy

Deploy it to Clojars -- needs `CLOJARS_USERNAME` and `CLOJARS_PASSWORD` environment
variables (requires the `ci` task be run first):

```
clojure -T:build deploy
```

The library will be deployed to net.clojars.[user]/openai-clojure on clojars.org by default.

### Docs

Build the api docs

```
clojure -X:codox
```

## Issues and features

Please feel free to raise issues on Github or send pull requests

## Acknowledgements

This library uses [Martian](https://github.com/oliyh/martian) - An HTTP abstraction library

## License

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
