# Streaming Tokens

Completions support the streaming of partial progress via [server-sent events](https://platform.openai.com/docs/api-reference/completions/create#completions/create-stream) using the `:stream` parameter

```
(create-completion {:model "text-davinci-003"
                    :prompt "Say this is a test"
                    :max_tokens 7
                    :temperature 0
                    :stream true})
```

## Reading streamed tokens

Reading streamed tokens can be done either by providing your own callback function, or by taking from the returned [core.async](https://clojure.org/guides/async_walkthrough#_getting_started) channel

### Option 1 - Callback function

Provide your callback function in the `:on-next` parameter for example

```
(create-completion {:model "text-davinci-003"
                    :prompt "Say this is a test"
                    :max_tokens 7
                    :temperature 0
                    :stream true
                    :on-next #(prn %)})
```

### Option 2 - core.async channel

```
  (require '[clojure.core.async :as a])

  (def events (create-completion {:model "text-davinci-003"
                                  :prompt "Say this is a test"
                                  :max_tokens 7
                                  :temperature 0
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

```
{:id "cmpl-6lf3JsE7hsWSikTfbiZ2NZOZKlBcG",
 :object "text_completion",
 :created 1676817241,
 :choices
 [{:text "This", :index 0, :logprobs nil, :finish_reason "length"}],
 :model "text-davinci-003"}
```
