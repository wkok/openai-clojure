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
  ([]
   (list-models nil))
  ([options]
   (core/response-for :list-models {} options)))

(defn retrieve-model
  "Retrieves a model instance, providing basic information about the model such as the owner and permissioning.

  Example:
  ```
  (retrieve-model {:model \"text-davinci-003\"})
  ```
  Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/models/retrieve)
  "
  ([params]
   (retrieve-model params nil))
  ([params options]
   (core/response-for :retrieve-model params options)))


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

  For Azure OpenAI pass `{:impl :azure}` for the `options` argument

  Streaming of token events is supported via the `:stream` param, see [Streaming Tokens](/doc/03-streaming.md)

  Also see the [OpenAI](https://platform.openai.com/docs/api-reference/completions/create) / [Azure OpenAI](https://learn.microsoft.com/en-us/azure/cognitive-services/openai/reference#completions) documentation
  "
  ([params]
   (create-completion params nil))
  ([params options]
   (core/response-for :create-completion params options)))

(defn create-chat-completion
  "Creates a completion for the chat message

  Example:
  ```
  (create-chat-completion {:model \"gpt-3.5-turbo\"
                           :messages [{:role \"system\" :content \"You are a helpful assistant.\"}
                                      {:role \"user\" :content \"Who won the world series in 2020?\"}
                                      {:role \"assistant\" :content \"The Los Angeles Dodgers won the World Series in 2020.\"}
                                      {:role \"user\" :content \"Where was it played?\"}]})
  ```

  For Azure OpenAI pass `{:impl :azure}` for the `options` argument

  Streaming of token events is supported via the `:stream` param, see [Streaming Tokens](/doc/03-streaming.md)

  Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/chat/create) / [Azure OpenAI](https://learn.microsoft.com/en-us/azure/cognitive-services/openai/reference#chat-completions) documentation
  "
  ([params]
   (create-chat-completion params nil))
  ([params options]
   (core/response-for :create-chat-completion params options)))


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
  ([params]
   (create-edit params nil))
  ([params options]
   (core/response-for :create-edit params options)))



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
  ([params]
   (create-image params nil))
  ([params options]
   (core/response-for :create-image params options)))


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
  ([params]
   (create-image-edit params nil))
  ([params options]
   (core/response-for :create-image-edit params options)))

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
  ([params]
   (create-image-variation params nil))
  ([params options]
   (core/response-for :create-image-variation params options)))


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

  For Azure OpenAI pass `{:impl :azure}` for the `options` argument

  Also see the [OpenAI](https://platform.openai.com/docs/api-reference/embeddings/create) / [Azure OpenAI](https://learn.microsoft.com/en-us/azure/cognitive-services/openai/reference#embeddings) documentation
  "
  ([params]
   (create-embedding params nil))
  ([params options]
   (let [opt (if (keyword? options)
               {:impl options} ;; backwards compatibility for when 2nd arg was impl
               options)]
     (core/response-for :create-embedding params opt))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Audio
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn create-transcription
  "Transcribes audio into the input language.

  Example:
  ```
  (create-transcription {:file (clojure.java.io/file \"path/to/audio.mp3\")
                         :model \"whisper-1\"})
  ```
  Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/audio/create)
  "
  ([params]
   (create-transcription params nil))
  ([params options]
   (core/response-for :create-transcription params options)))

(defn create-translation
  "Translates audio into English.

  Example:
  ```
  (create-translation {:file (clojure.java.io/file \"path/to/file/german.m4a\")
                       :model \"whisper-1\"})
  ```
  Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/audio/create)
  "
  ([params]
   (create-translation params nil))
  ([params options]
   (core/response-for :create-translation params options)))


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
  ([]
   (list-files nil))
  ([options]
   (core/response-for :list-files {} options)))

(defn create-file
  "Upload a file that contains document(s) to be used across various endpoints/features. Currently, the size of all the files uploaded by one organization can be up to 1 GB.

  Example:
  ```
  (create-file {:purpose \"fine-tune\"
                :file (clojure.java.io/file \"path/to/fine-tune.jsonl\")})
  ```
  Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/files/upload)
  "
  ([params]
   (create-file params nil))
  ([params options]
   (core/response-for :create-file params options)))

(defn delete-file
  "Delete a file.

  Example:
  ```
  (delete-file {:file-id \"file-wefuhweof\"})
  ```
  Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/files/delete)
  "
  ([params]
   (delete-file params nil))
  ([params options]
   (core/response-for :delete-file params options)))

(defn retrieve-file
  "Returns information about a specific file.

  Example:
  ```
  (retrieve-file {:file-id \"file-wefuhweof\"})
  ```
  Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/files/retrieve)
  "
  ([params]
   (retrieve-file params nil))
  ([params options]
   (core/response-for :retrieve-file params options)))

(defn download-file
  "Returns the contents of the specified file

  Example:
  ```
  (download-file {:file-id \"file-wefuhweof\"})
  ```
  Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/files/retrieve-content)
  "
  ([params]
   (download-file params nil))
  ([params options]
   (core/response-for :download-file params options)))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Fine tune (deprecated)
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn ^:deprecated create-fine-tune
  "(Deprecated) Creates a job that fine-tunes a specified model from a given dataset.\n\nResponse includes details of the enqueued job including job status and the name of the fine-tuned models once complete.

  Example:
  ```
  (create-fine-tune {:training_file \"file-xuhfiwuefb\"})
  ```
  Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/fine-tunes/create)
  "
  ([params]
   (create-fine-tune params nil))
  ([params options]
   (core/response-for :create-fine-tune params options)))

(defn ^:deprecated list-fine-tunes
  "(Deprecated) List your organization's fine-tuning jobs

  Example:
  ```
  (list-fine-tunes)
  ```
  Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/fine-tunes/list)
  "
  ([]
   (list-fine-tunes nil))
  ([options]
   (core/response-for :list-fine-tunes {} options)))

(defn ^:deprecated retrieve-fine-tune
  "(Deprecated) Gets info about the fine-tune job.

  Example:
  ```
  (retrieve-fine-tune {:fine_tune_id \"ft-1wefweub\"})
  ```
  Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/fine-tunes/retrieve)
  "
  ([params]
   (retrieve-fine-tune params nil))
  ([params options]
   (core/response-for :retrieve-fine-tune params options)))

(defn ^:deprecated cancel-fine-tune
  "(Deprecated) Immediately cancel a fine-tune job.

  Example:
  ```
  (cancel-fine-tune {:fine_tune_id \"ft-1wefweub\"})
  ```
  Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/fine-tunes/cancel)
  "
  ([params]
   (cancel-fine-tune params nil))
  ([params options]
   (core/response-for :cancel-fine-tune params options)))


(defn ^:deprecated list-fine-tune-events
  "(Deprecated) Get fine-grained status updates for a fine-tune job.

  Example:
  ```
  (list-fine-tune-events {:fine_tune_id \"ft-1wefweub\"})
  ```
  Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/fine-tunes/events)
  "
  ([params]
   (list-fine-tune-events params nil))
  ([params options]
   (core/response-for :list-fine-tune-events params options)))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Fine tuning (replaces Fine-tunes API)
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn create-fine-tuning-job
  "Creates a job that fine-tunes a specified model from a given dataset.\n\nResponse includes details of the enqueued job including job status and the name of the fine-tuned models once complete.

  Example:
  ```
  (create-fine-tuning-job {:training_file \"file-xuhfiwuefb\"
                           :model \"gpt-3.5-turbo\"})
  ```
  Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/fine-tuning/create)
  "
  ([params]
   (create-fine-tuning-job params nil))
  ([params options]
   (core/response-for :create-fine-tuning-job params options)))

(defn list-fine-tuning-jobs
  "List your organization's fine-tuning jobs

  Example:
  ```
  (list-fine-tuning-jobs)
  ```
  Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/fine-tuning/list)
  "
  ([]
   (list-fine-tuning-jobs nil))
  ([options]
   (core/response-for :list-paginated-fine-tuning-jobs {} options)))

(defn retrieve-fine-tuning-job
  "Gets info about a fine-tuning job.

  Example:
  ```
  (retrieve-fine-tuning-job {:fine_tuning_job_id \"ft-1wefweub\"})
  ```
  Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/fine-tuning/retrieve)
  "
  ([params]
   (retrieve-fine-tuning-job params nil))
  ([params options]
   (core/response-for :retrieve-fine-tuning-job params options)))

(defn cancel-fine-tuning-job
  "Immediately cancel a fine-tuning job.

  Example:
  ```
  (cancel-fine-tuning-job {:fine_tuning_job_id \"ft-1wefweub\"})
  ```
  Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/fine-tuning/cancel)
  "
  ([params]
   (cancel-fine-tuning-job params nil))
  ([params options]
   (core/response-for :cancel-fine-tuning-job params options)))

(defn list-fine-tuning-events
  "Get status updates for a fine-tuning job.

  Example:
  ```
  (list-fine-tuning-events {:fine_tuning_job_id \"ft-1wefweub\"})
  ```
  Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/fine-tuning/events)
  "
  ([params]
   (list-fine-tuning-events params nil))
  ([params options]
   (core/response-for :list-fine-tuning-events params options)))

(defn delete-model
  "Delete a fine-tuned model. You must have the Owner role in your organization.

  Example:
  ```
  (delete-model {:model \"fine-tune\"})
  ```
  Also see the [OpenAI documentation](https://platform.openai.com/docs/api-reference/fine-tunes/delete-model)
  "
  ([params]
   (delete-model params nil))
  ([params options]
   (core/response-for :delete-model params options)))



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
  ([params]
   (create-moderation params nil))
  ([params options]
   (core/response-for :create-moderation params options)))
