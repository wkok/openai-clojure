# Async support

When supplying `{:request {:async? true}}` in `:options` the request will be sent asynchronously and immediately return a Future that can be de-referenced to retrieve the result:

```clojure
@(create-chat-completion {:model    "gpt-3.5-turbo"
                          :messages [{:role "system" :content "You are a helpful assistant."}
                                     {:role "user" :content "Who won the world series in 2020?"}
                                     {:role "assistant" :content "The Los Angeles Dodgers won the World Series in 2020."}
                                     {:role "user" :content "Where was it played?"}]}
                         {:request {:async? true}})
```
