# Changes to hosted YAML spec

Some changes are required in the hosted YAML spec to make it compatible with Martian

## CreateChatCompletionRequest/properties/max_tokens/default

* from: `inf`
* to: `16384`

## ChatCompletionFunctions/properties/parameters

More info in this   [PR](https://github.com/wkok/openai-clojure/pull/23#issuecomment-1595764611)

* from: `ChatCompletionFunctionParameters`
* to: `{}`

## /threads/{thread_id}/runs/{run_id}/submit_tool_outputs

Fix the typo:  [PR](https://github.com/openai/openai-openapi/pull/114)

* from: `submitToolOuputsToRun`
* to:   `submitToolOutputsToRun`
