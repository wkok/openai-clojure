# Changes to hosted YAML spec

Some changes are required in the hosted YAML spec to make it compatible with Martian

## ChatCompletionFunctions/properties/parameters

later: (#/components/schemas/FunctionParameters)

More info in this   [PR](https://github.com/wkok/openai-clojure/pull/23#issuecomment-1595764611)

* from: `ChatCompletionFunctionParameters`
* to: `{}`

## FunctionObject

More info in this [issue](https://github.com/wkok/openai-clojure/issues/41)

* from "#/components/schemas/FunctionParameters"
* to {}

## /threads/{thread_id}/runs/{run_id}/submit_tool_outputs

Fix the typo:  [PR](https://github.com/openai/openai-openapi/pull/114)

* from: `submitToolOuputsToRun`
* to:   `submitToolOutputsToRun`
