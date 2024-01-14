# Debugging

When supplying a `trace` function in `:options` it will be called with the raw maps of the request and response for allowing capturing tracing information like the raw messages sent/received:

```clojure
(create-chat-completion {:model    "gpt-3.5-turbo"
                         :messages [{:role "system" :content "You are a helpful assistant."}
                                    {:role "user" :content "Who won the world series in 2020?"}
                                    {:role "assistant" :content "The Los Angeles Dodgers won the World Series in 2020."}
                                    {:role "user" :content "Where was it played?"}]}
                        {:trace (fn [request response]
                                   (println (:body response)))})
```

Prints:

```json
{
  "id": "chatcmpl-8gHB7Il500UxZPChGAZHtklLWTS8T",
  "object": "chat.completion",
  "created": 1705086501,
  "model": "gpt-3.5-turbo-0613",
  "choices": [
    {
      "index": 0,
      "message": {
        "role": "assistant",
        "content": "The 2020 World Series was played at Globe Life Field in Arlington, Texas."
      },
      "logprobs": null,
      "finish_reason": "stop"
    }
  ],
  "usage": {
    "prompt_tokens": 53,
    "completion_tokens": 17,
    "total_tokens": 70
  },
  "system_fingerprint": null
}

```
