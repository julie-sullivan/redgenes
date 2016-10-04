(ns redgenes.routes
  (:require [compojure.core :refer [GET POST defroutes context ANY]]
            [redgenes.api.modelcount :refer [modelcount modelcount-children cache cacheall]]
            [ring.util.response :refer [response]]))



(defroutes routes
  (GET "/version" [] (response {:version "0.1.0"}))

  (context "/model/count" [paths]
    (GET "/cache" [mine] (cache mine)
      (response {:loading (str "We're caching counts for " mine "! Well done.")}))
    (GET "/cacheall" [] (cacheall)
      (response {:loading "We're caching counts for all mines! Please wait."}))
    (GET "/children" [path mine]
         (response (modelcount-children path mine)))
    (POST "/" [paths mine]
      (response (modelcount paths mine)))
    (GET "/" [paths mine]
      (response (modelcount paths mine))
    ))
  )