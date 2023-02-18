(ns wkok.openai-clojure.api
  (:require [wkok.openai-clojure.core :as core]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Models
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn list-models
  "Lists the currently available models, and provides basic information about each one such as the owner and availability.

  Example:
  ```
  (list-models)
  ```
  Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/models/list)
  "
  {:doc/format :markdown}
  []
  (core/response-for :openai :list-models {}))

(defn retrieve-model
  "Retrieves a model instance, providing basic information about the model such as the owner and permissioning.

  Example:
  ```
  (retrieve-model {:model \"text-davinci-003\"})
  ```
  Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/models/retrieve)
  "
  {:doc/format :markdown}
  [params]
  (core/response-for :openai :retrieve-model params))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Completion
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn create-completion
  "Creates a completion for the provided prompt and parameters

  Example:
  ```
  (create-completion {:model \"text-davinci-003\"
                      :prompt \"Say this is a test\"
                      :max_tokens 7
                      :temperature 0})
  ```

  For Azure OpenAI pass `:azure` for the `impl` argument

  Streaming of token events is supported via the `:stream` param, see [Streaming Token Events](sse.html)

  Also see the [OpenAI](https://platform.openai.com/docs/api-reference/completions/create) / [Azure OpenAI](https://learn.microsoft.com/en-us/azure/cognitive-services/openai/reference#completions) documentation
  "
  {:doc/format :markdown}
  ([params]
   (create-completion :openai params))
  ([impl params]
   (core/response-for impl :create-completion params)))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Edit
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn create-edit
  "Creates a new edit for the provided input, instruction, and parameters

  Example:
  ```
  (create-edit {:model \"text-davinci-edit-001\"
                :input \"What day of the wek is it?\"
                :instruction \"Fix the spelling mistakes\"})
  ```
  Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/edits/create)
  "
  {:doc/format :markdown}
  [params]
  (core/response-for :openai :create-edit params))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Images
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn create-image
  "Creates an image given a prompt.

  Example:
  ```
  (create-image {:prompt \"A cute baby sea otter\"
                 :n 2
                 :size \"1024x1024\"})
  ```
  Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/images/create)
  "
  {:doc/format :markdown}
  [params]
  (core/response-for :openai :create-image params))


(defn create-image-edit
  "Creates an edited or extended image given an original image and a prompt.

  Example:
  ```
  (create-image-edit {:image (clojure.java.io/file \"path/to/otter.png\")
                      :mask (clojure.java.io/file \"path/to/mask.png\")
                      :prompt \"A cute baby sea otter wearing a beret\"
                      :n 2
                      :size \"1024x1024\"})
  ```
  Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/images/create-edit)
  "
  {:doc/format :markdown}
  [params]
  (core/response-for :openai :create-image-edit params))

(defn create-image-variation
  "Creates a variation of a given image.

  Example:
  ```
  (create-image-variation {:image (clojure.java.io/file \"path/to/otter.png\")
                           :n 2
                           :size \"1024x1024\"})
  ```
  Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/images/create-variation)
  "
  {:doc/format :markdown}
  [params]
  (core/response-for :openai :create-image-variation params))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Embedding
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn create-embedding
  "Creates an embedding vector representing the input text.

  Example:
  ```
  (create-embedding {:model \"text-embedding-ada-002\"
                     :input \"The food was delicious and the waiter...\"})
  ```

  For Azure OpenAI pass `:azure` for the `impl` argument

  Also see the [OpenAI](https://platform.openai.com/docs/api-reference/embeddings/create) / [Azure OpenAI](https://learn.microsoft.com/en-us/azure/cognitive-services/openai/reference#embeddings) documentation
  "
  {:doc/format :markdown}
  ([params]
   (create-embedding :openai params))
  ([impl params]
   (core/response-for impl :create-embedding params)))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Files
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn list-files
  "Returns a list of files that belong to the user's organization.

  Example:
  ```
  (list-files)
  ```
  Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/files/list)
  "
  {:doc/format :markdown}
  []
  (core/response-for :openai :list-files {}))

(defn create-file
  "Upload a file that contains document(s) to be used across various endpoints/features. Currently, the size of all the files uploaded by one organization can be up to 1 GB.

  Example:
  ```
  (create-file {:purpose \"fine-tune\"
                :file (clojure.java.io/file \"path/to/fine-tune.jsonl\")})
  ```
  Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/files/upload)
  "
  {:doc/format :markdown}
  [params]
  (core/response-for :openai :create-file params))

(defn delete-file
  "Delete a file.

  Example:
  ```
  (delete-file {:file-id \"file-wefuhweof\"})
  ```
  Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/files/delete)
  "
  {:doc/format :markdown}
  [params]
  (core/response-for :openai :delete-file params))

(defn retrieve-file
  "Returns information about a specific file.

  Example:
  ```
  (retrieve-file {:file-id \"file-wefuhweof\"})
  ```
  Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/files/retrieve)
  "
  {:doc/format :markdown}
  [params]
  (core/response-for :openai :retrieve-file params))

(defn download-file
  "Returns the contents of the specified file

  Example:
  ```
  (download-file {:file-id \"file-wefuhweof\"})
  ```
  Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/files/retrieve-content)
  "
  {:doc/format :markdown}
  [params]
  (core/response-for :openai :download-file params))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Fine tune
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn create-fine-tune
  "Creates a job that fine-tunes a specified model from a given dataset.\n\nResponse includes details of the enqueued job including job status and the name of the fine-tuned models once complete.

  Example:
  ```
  (create-fine-tune {:training_file \"file-xuhfiwuefb\"})
  ```
  Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/fine-tunes/create)
  "
  {:doc/format :markdown}
  [params]
  (core/response-for :openai :create-fine-tune params))

(defn list-fine-tunes
  "List your organization's fine-tuning jobs

  Example:
  ```
  (list-fine-tunes)
  ```
  Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/fine-tunes/list)
  "
  {:doc/format :markdown}
  []
  (core/response-for :openai :list-fine-tunes {}))

(defn retrieve-fine-tune
  "Gets info about the fine-tune job.

  Example:
  ```
  (retrieve-fine-tune {:fine_tune_id \"ft-1wefweub\"})
  ```
  Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/fine-tunes/retrieve)
  "
  {:doc/format :markdown}
  [params]
  (core/response-for :openai :retrieve-fine-tune params))

(defn cancel-fine-tune
  "Immediately cancel a fine-tune job.

  Example:
  ```
  (cancel-fine-tune {:fine_tune_id \"ft-1wefweub\"})
  ```
  Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/fine-tunes/cancel)
  "
  {:doc/format :markdown}
  [params]
  (core/response-for :openai :cancel-fine-tune params))


(defn list-fine-tune-events
  "Get fine-grained status updates for a fine-tune job.

  Example:
  ```
  (list-fine-tune-events {:fine_tune_id \"ft-1wefweub\"})
  ```
  Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/fine-tunes/events)
  "
  {:doc/format :markdown}
  [params]
  (core/response-for :openai :list-fine-tune-events params))

(defn delete-model
  "Delete a fine-tuned model. You must have the Owner role in your organization.

  Example:
  ```
  (delete-model {:model \"fine-tune\"})
  ```
  Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/fine-tunes/delete-model)
  "
  {:doc/format :markdown}
  [params]
  (core/response-for :openai :delete-model params))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Moderation
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn create-moderation
  "Classifies if text violates OpenAI's Content Policy

  Example:
  ```
  (create-moderation {:input \"I want to kill them\"})
  ```
  Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/moderations/create)
  "
  {:doc/format :markdown}
  [params]
  (core/response-for :openai :create-moderation params))
