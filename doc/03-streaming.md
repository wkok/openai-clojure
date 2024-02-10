# Streaming Tokens

[Chat](https://platform.openai.com/docs/api-reference/chat/create#chat/create-stream) and [Completions](https://platform.openai.com/docs/api-reference/completions/create#completions/create-stream) support the streaming of partial progress via server-sent events using the `:stream` parameter

```clojure
(api/create-chat-completion {:model "gpt-3.5-turbo"
                             :messages [{:role "system" :content "You are a helpful assistant."}
                                        {:role "user" :content "Who won the world series in 2020?"}
                                        {:role "assistant" :content "The Los Angeles Dodgers won the World Series in 2020."}
                                        {:role "user" :content "Where was it played?"}]
                             :stream true})
```

## Reading streamed tokens

Reading streamed tokens can be done either by providing your own callback function, or by taking from the returned [core.async](https://clojure.org/guides/async_walkthrough#_getting_started) channel

### Option 1 - Callback function

Provide your callback function in the `:on-next` parameter for example

```clojure
(api/create-chat-completion {:model "gpt-3.5-turbo"
                             :messages [{:role "system" :content "You are a helpful assistant."}
                                        {:role "user" :content "Who won the world series in 2020?"}
                                        {:role "assistant" :content "The Los Angeles Dodgers won the World Series in 2020."}
                                        {:role "user" :content "Where was it played?"}]
                             :stream true
                             :on-next #(prn %)})
```

### Option 2 - core.async channel

```clojure
  (require '[clojure.core.async :as a])

  (def events (api/create-chat-completion {:model "gpt-3.5-turbo"
                                           :messages [{:role "system" :content "You are a helpful assistant."}
                                                      {:role "user" :content "Who won the world series in 2020?"}
                                                      {:role "assistant" :content "The Los Angeles Dodgers won the World Series in 2020."}
                                                      {:role "user" :content "Where was it played?"}]
                                           :stream true}))

  (a/go
    (loop []
      (let [event (a/<! events)]
        (when (not= :done event)

          ;; Do something with the event token
          (prn event)

          (recur)))))

```

## Example returned token

```clojure
{:id "chatcmpl-6srv5jx3p4I9deNDzU7ucNXKoGS0L"
   :object "chat.completion.chunk"
   :created 1678534999
   :model "gpt-3.5-turbo-0301"
   :choices [{:delta {:content "The"} :index 0, :finish_reason nil}]}
```
